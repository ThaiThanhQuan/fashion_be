package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.ServiceRequest;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ServiceResponse;
import com.example.fashion_db.entity.Service;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.ServiceMapper;
import com.example.fashion_db.repository.ArtistRepository;
import com.example.fashion_db.repository.ServiceRepository;
import com.example.fashion_db.utils.SlugUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServiceService {

    ServiceRepository serviceRepository;
    ArtistRepository artistRepository;
    ServiceMapper serviceMapper;
    CloudinaryService cloudinaryService;

    public ServiceResponse createService(ServiceRequest request) {
        if (serviceRepository.existsByTitle(request.getTitle()))
            throw new AppException(ErrorCode.SERVICE_EXISTED);

        Service service = serviceMapper.toService(request);
        service.setSlug(SlugUtils.generateSlug(request.getTitle()));
        service.setArtist(artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND)));

        if (request.getThumbnail() != null)
            service.setThumbnail(cloudinaryService.uploadImage(request.getThumbnail()));

        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }

    public PageResponse<ServiceResponse> getAllServices(int page, int size) {
        return PageResponse.of(serviceRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                .map(serviceMapper::toServiceResponse));
    }

    public ServiceResponse getServiceById(String serviceId) {
        return serviceMapper.toServiceResponse(
                serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)));
    }

    public ServiceResponse getServiceBySlug(String slug) {
        return serviceMapper.toServiceResponse(
                serviceRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)));
    }

    public ServiceResponse updateService(String serviceId, ServiceRequest request) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        serviceMapper.updateService(service, request);
        service.setSlug(SlugUtils.generateSlug(request.getTitle()));
        service.setArtist(artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND)));

        if (request.getThumbnail() != null) {
            if (service.getThumbnail() != null)
                cloudinaryService.deleteImage(service.getThumbnail());
            service.setThumbnail(cloudinaryService.uploadImage(request.getThumbnail()));
        }

        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }

    public void deleteService(String serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        if (service.getThumbnail() != null)
            cloudinaryService.deleteImage(service.getThumbnail());

        serviceRepository.deleteById(serviceId);
    }
}