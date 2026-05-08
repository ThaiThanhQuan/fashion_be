package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.WorkflowRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.WorkflowResponse;
import com.example.fashion_db.service.WorkflowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WorkflowController {

    WorkflowService workflowService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<WorkflowResponse> createWorkflow(@RequestBody WorkflowRequest request) {
        return ApiResponse.<WorkflowResponse>builder()
                .result(workflowService.createWorkflow(request))
                .build();
    }

    @GetMapping("/service/{serviceId}")
    public ApiResponse<List<WorkflowResponse>> getWorkflowsByService(
            @PathVariable String serviceId) {
        return ApiResponse.<List<WorkflowResponse>>builder()
                .result(workflowService.getWorkflowsByService(serviceId))
                .build();
    }

    @GetMapping("/{workflowId}")
    public ApiResponse<WorkflowResponse> getWorkflowById(@PathVariable String workflowId) {
        return ApiResponse.<WorkflowResponse>builder()
                .result(workflowService.getWorkflowById(workflowId))
                .build();
    }

    @PutMapping("/{workflowId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<WorkflowResponse> updateWorkflow(
            @PathVariable String workflowId,
            @RequestBody WorkflowRequest request) {
        return ApiResponse.<WorkflowResponse>builder()
                .result(workflowService.updateWorkflow(workflowId, request))
                .build();
    }

    @DeleteMapping("/{workflowId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteWorkflow(@PathVariable String workflowId) {
        workflowService.deleteWorkflow(workflowId);
        return ApiResponse.<Void>builder()
                .message("Delete workflow successfully")
                .build();
    }
}
