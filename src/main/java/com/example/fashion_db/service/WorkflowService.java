package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.WorkflowRequest;
import com.example.fashion_db.dto.response.WorkflowResponse;
import com.example.fashion_db.entity.Workflow;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.WorkflowMapper;
import com.example.fashion_db.repository.ServiceRepository;
import com.example.fashion_db.repository.WorkflowRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WorkflowService {

    WorkflowRepository workflowRepository;
    ServiceRepository serviceRepository;
    WorkflowMapper workflowMapper;

    public WorkflowResponse createWorkflow(WorkflowRequest request) {
        Workflow workflow = workflowMapper.toWorkflow(request);
        workflow.setService(serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)));

        return workflowMapper.toWorkflowResponse(workflowRepository.save(workflow));
    }

    public List<WorkflowResponse> getWorkflowsByService(String serviceId) {
        return workflowRepository.findByService_IdOrderByNoAsc(serviceId)
                .stream()
                .map(workflowMapper::toWorkflowResponse)
                .toList();
    }

    public WorkflowResponse getWorkflowById(String workflowId) {
        return workflowMapper.toWorkflowResponse(
                workflowRepository.findById(workflowId)
                        .orElseThrow(() -> new AppException(ErrorCode.WORKFLOW_NOT_FOUND)));
    }

    public WorkflowResponse updateWorkflow(String workflowId, WorkflowRequest request) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new AppException(ErrorCode.WORKFLOW_NOT_FOUND));

        workflowMapper.updateWorkflow(workflow, request);
        return workflowMapper.toWorkflowResponse(workflowRepository.save(workflow));
    }

    public void deleteWorkflow(String workflowId) {
        workflowRepository.deleteById(workflowId);
    }
}