package com.stateflow.system.concurrency;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.system.workflow.WorkflowEvent;
import com.stateflow.system.workflow.WorkflowStateMachineService;

/**
 * Drives the delayed FINISH transition. A failed attempt is put back with backoff.
 */
@Component
public class RetryWatchScheduler
{
    private static final Logger log = LoggerFactory.getLogger(RetryWatchScheduler.class);

    private final RetryWatchQueue retryWatchQueue;
    private final WorkflowStateMachineService workflowStateMachineService;

    public RetryWatchScheduler(RetryWatchQueue retryWatchQueue, WorkflowStateMachineService workflowStateMachineService)
    {
        this.retryWatchQueue = retryWatchQueue;
        this.workflowStateMachineService = workflowStateMachineService;
    }

    @Scheduled(fixedDelay = 2000)
    public void drain()
    {
        List<String> due = retryWatchQueue.claimDue(20);
        for (String taskId : due)
        {
            try
            {
                workflowStateMachineService.sendEvent(taskId, WorkflowEvent.FINISH);
                retryWatchQueue.ack(taskId);
            }
            catch (ServiceException ex)
            {
                boolean again = retryWatchQueue.backoff(taskId);
                log.warn("delayed finish failed for {}, retry={}: {}", taskId, again, ex.getMessage());
            }
        }
    }
}
