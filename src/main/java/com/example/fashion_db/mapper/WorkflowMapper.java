package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.WorkflowRequest;
import com.example.fashion_db.dto.response.WorkflowResponse;
import com.example.fashion_db.entity.Workflow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkflowMapper {
    @Mapping(target = "service", ignore = true)
    Workflow toWorkflow(WorkflowRequest request);

    @Mapping(target = "serviceId", source = "service.id")
    WorkflowResponse toWorkflowResponse(Workflow workflow);

    @Mapping(target = "service", ignore = true)
    void updateWorkflow(@MappingTarget Workflow workflow, WorkflowRequest request);
}
