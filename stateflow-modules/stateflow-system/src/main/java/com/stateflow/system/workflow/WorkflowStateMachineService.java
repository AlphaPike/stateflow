package com.stateflow.system.workflow;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.system.concurrency.ActionLock;
import com.stateflow.system.concurrency.RetryWatchQueue;
import com.stateflow.system.mapper.WorkflowTaskMapper;

@Service
public class WorkflowStateMachineService
{
    private final StateMachineFactory<WorkflowState, WorkflowEvent> stateMachineFactory;
    private final StateMachinePersister<WorkflowState, WorkflowEvent, String> persister;
    private final WorkflowTaskMapper workflowTaskMapper;
    private final ActionLock actionLock;
    private final RetryWatchQueue retryWatchQueue;

    public WorkflowStateMachineService(
            @Qualifier("workflowStateMachineFactory") StateMachineFactory<WorkflowState, WorkflowEvent> stateMachineFactory,
            @Qualifier("workflowStateMachinePersister") StateMachinePersister<WorkflowState, WorkflowEvent, String> persister,
            WorkflowTaskMapper workflowTaskMapper,
            ActionLock actionLock,
            RetryWatchQueue retryWatchQueue)
    {
        this.stateMachineFactory = stateMachineFactory;
        this.persister = persister;
        this.workflowTaskMapper = workflowTaskMapper;
        this.actionLock = actionLock;
        this.retryWatchQueue = retryWatchQueue;
    }

    public WorkflowTask create()
    {
        WorkflowTask task = new WorkflowTask();
        task.setId(UUID.randomUUID().toString().replace("-", ""));
        task.setState(WorkflowState.CREATED.name());
        Date now = new Date();
        task.setCreateTime(now);
        task.setUpdateTime(now);
        workflowTaskMapper.insert(task);
        return task;
    }

    public WorkflowTask get(String taskId)
    {
        WorkflowTask task = workflowTaskMapper.selectById(taskId);
        if (task == null)
        {
            throw new ServiceException("workflow task not found: " + taskId);
        }
        return task;
    }

    @Transactional(rollbackFor = Exception.class)
    public WorkflowTask sendEvent(String taskId, WorkflowEvent event)
    {
        if (event == null)
        {
            throw new ServiceException("unknown workflow event");
        }
        if (!actionLock.tryLock(taskId))
        {
            throw new ServiceException("workflow task is being updated: " + taskId);
        }
        actionLock.unlockAfterCompletion(taskId);
        StateMachine<WorkflowState, WorkflowEvent> machine = stateMachineFactory.getStateMachine();
        try
        {
            machine.start();
            persister.restore(machine, taskId);
            boolean accepted = machine.sendEvent(event);
            if (!accepted)
            {
                throw new ServiceException("transition rejected: " + event.name());
            }
            persister.persist(machine, taskId);
        }
        catch (ServiceException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ServiceException("workflow transition failed: " + ex.getMessage());
        }
        finally
        {
            machine.stop();
        }
        if (event == WorkflowEvent.APPROVE)
        {
            retryWatchQueue.enqueue(taskId);
        }
        return get(taskId);
    }
}
