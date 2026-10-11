package com.stateflow.system.workflow;

import java.util.EnumSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * One state machine instance per task. Persistence is the database row, not in-memory state.
 */
@Configuration
@EnableStateMachineFactory(name = "workflowStateMachineFactory")
public class WorkflowStateMachineConfig extends StateMachineConfigurerAdapter<WorkflowState, WorkflowEvent>
{
    private final WorkflowStateMachinePersist workflowStateMachinePersist;

    public WorkflowStateMachineConfig(WorkflowStateMachinePersist workflowStateMachinePersist)
    {
        this.workflowStateMachinePersist = workflowStateMachinePersist;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<WorkflowState, WorkflowEvent> config) throws Exception
    {
        config.withConfiguration().autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<WorkflowState, WorkflowEvent> states) throws Exception
    {
        states.withStates()
                .initial(WorkflowState.CREATED)
                .states(EnumSet.allOf(WorkflowState.class))
                .end(WorkflowState.REJECTED)
                .end(WorkflowState.DONE)
                .end(WorkflowState.CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkflowState, WorkflowEvent> transitions) throws Exception
    {
        transitions
                .withExternal().source(WorkflowState.CREATED).target(WorkflowState.SUBMITTED).event(WorkflowEvent.SUBMIT).and()
                .withExternal().source(WorkflowState.CREATED).target(WorkflowState.CANCELLED).event(WorkflowEvent.CANCEL).and()
                .withExternal().source(WorkflowState.SUBMITTED).target(WorkflowState.APPROVED).event(WorkflowEvent.APPROVE).and()
                .withExternal().source(WorkflowState.SUBMITTED).target(WorkflowState.REJECTED).event(WorkflowEvent.REJECT).and()
                .withExternal().source(WorkflowState.SUBMITTED).target(WorkflowState.CANCELLED).event(WorkflowEvent.CANCEL).and()
                .withExternal().source(WorkflowState.APPROVED).target(WorkflowState.DONE).event(WorkflowEvent.FINISH).and()
                .withExternal().source(WorkflowState.APPROVED).target(WorkflowState.CANCELLED).event(WorkflowEvent.CANCEL);
    }

    @Bean(name = "workflowStateMachinePersister")
    public StateMachinePersister<WorkflowState, WorkflowEvent, String> workflowStateMachinePersister()
    {
        return new DefaultStateMachinePersister<>(workflowStateMachinePersist);
    }
}
