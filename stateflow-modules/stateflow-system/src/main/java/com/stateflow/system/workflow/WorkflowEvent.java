package com.stateflow.system.workflow;

public enum WorkflowEvent
{
    SUBMIT,
    APPROVE,
    REJECT,
    CANCEL,
    FINISH;

    public static WorkflowEvent fromCode(String code)
    {
        if (code == null)
        {
            return null;
        }
        for (WorkflowEvent event : values())
        {
            if (event.name().equalsIgnoreCase(code))
            {
                return event;
            }
        }
        return null;
    }
}
