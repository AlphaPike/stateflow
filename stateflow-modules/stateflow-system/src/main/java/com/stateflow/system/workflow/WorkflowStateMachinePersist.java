package com.stateflow.system.workflow;

import java.util.Date;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.system.mapper.WorkflowTaskMapper;

@Component
public class WorkflowStateMachinePersist implements StateMachinePersist<WorkflowState, WorkflowEvent, String>
{
    private final WorkflowTaskMapper workflowTaskMapper;

    public WorkflowStateMachinePersist(WorkflowTaskMapper workflowTaskMapper)
    {
        this.workflowTaskMapper = workflowTaskMapper;
    }

    @Override
    public void write(StateMachineContext<WorkflowState, WorkflowEvent> context, String taskId)
    {
        WorkflowTask task = workflowTaskMapper.selectById(taskId);
        if (task == null || context.getState() == null)
        {
            throw new ServiceException("workflow task not found: " + taskId);
        }
        String next = context.getState().name();
        if (!next.equals(task.getState()))
        {
            workflowTaskMapper.updateState(taskId, next, new Date());
        }
    }

    @Override
    public StateMachineContext<WorkflowState, WorkflowEvent> read(String taskId)
    {
        WorkflowTask task = workflowTaskMapper.selectById(taskId);
        if (task == null)
        {
            throw new ServiceException("workflow task not found: " + taskId);
        }
        WorkflowState state = WorkflowState.fromCode(task.getState());
        if (state == null)
        {
            throw new ServiceException("unknown workflow state: " + task.getState());
        }
        return new DefaultStateMachineContext<>(state, null, null, null);
    }
}
