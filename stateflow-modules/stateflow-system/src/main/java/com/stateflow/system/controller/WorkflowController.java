package com.stateflow.system.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stateflow.common.core.web.domain.AjaxResult;
import com.stateflow.system.workflow.WorkflowEvent;
import com.stateflow.system.workflow.WorkflowStateMachineService;
import com.stateflow.system.workflow.WorkflowTask;

@RestController
@RequestMapping("/workflow/tasks")
public class WorkflowController
{
    private final WorkflowStateMachineService workflowStateMachineService;

    public WorkflowController(WorkflowStateMachineService workflowStateMachineService)
    {
        this.workflowStateMachineService = workflowStateMachineService;
    }

    @PostMapping
    public AjaxResult create()
    {
        return AjaxResult.success(workflowStateMachineService.create());
    }

    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") String id)
    {
        return AjaxResult.success(workflowStateMachineService.get(id));
    }

    @PostMapping("/{id}/events")
    public AjaxResult fire(@PathVariable("id") String id, @RequestBody Map<String, String> body)
    {
        WorkflowEvent event = WorkflowEvent.fromCode(body == null ? null : body.get("event"));
        WorkflowTask task = workflowStateMachineService.sendEvent(id, event);
        return AjaxResult.success(task);
    }
}
