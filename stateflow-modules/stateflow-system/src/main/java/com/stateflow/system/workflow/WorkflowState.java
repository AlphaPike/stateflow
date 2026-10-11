package com.stateflow.system.workflow;

public enum WorkflowState
{
    CREATED,
    SUBMITTED,
    APPROVED,
    REJECTED,
    DONE,
    CANCELLED;

    public boolean isTerminal()
    {
        return this == REJECTED || this == DONE || this == CANCELLED;
    }

    public static WorkflowState fromCode(String code)
    {
        if (code == null)
        {
            return null;
        }
        for (WorkflowState state : values())
        {
            if (state.name().equals(code))
            {
                return state;
            }
        }
        return null;
    }
}
