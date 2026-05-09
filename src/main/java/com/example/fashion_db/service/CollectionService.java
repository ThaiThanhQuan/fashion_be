package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.CollectionRequest;
import com.example.fashion_db.dto.response.CollectionResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.entity.Collection;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.CollectionMapper;
import com.example.fashion_db.repository.*;
import com.example.fashion_db.specification.CollectionSpecification;
import com.example.fashion_db.utils.SlugUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CollectionService {

    CollectionRepository collectionRepository;
    ProductRepository productRepository;
    SeasonRepository seasonRepository;
    CategoryCollectionRepository categoryCollectionRepository;
    ArtistRepository artistRepository;
    CollectionMapper collectionMapper;
    CloudinaryService cloudinaryService;

    public CollectionResponse createCollection(CollectionRequest request) {
        if (collectionRepository.existsByTitle(request.getTitle()))
            throw new AppException(ErrorCode.COLLECTION_EXISTED);

        Collection collection = collectionMapper.toCollection(request);
        collection.setSlug(SlugUtils.generateSlug(request.getTitle()));
        setRelations(collection, request);
        setProducts(collection, request.getProductIds());

        if (request.getThumbnail() != null)
            collection.setThumbnail(cloudinaryService.uploadImage(request.getThumbnail()));

        return collectionMapper.toCollectionResponse(collectionRepository.save(collection));
    }

    public PageResponse<CollectionResponse> getAllCollections(int page, int size) {
        return PageResponse.of(collectionRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                .map(collectionMapper::toCollectionResponse));
    }

    public CollectionResponse getCollectionById(String collectionId) {
        return collectionMapper.toCollectionResponse(
                collectionRepository.findById(collectionId)
                        .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND)));
    }

    public CollectionResponse getCollectionBySlug(String slug) {
        return collectionMapper.toCollectionResponse(
                collectionRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND)));
    }

    public PageResponse<CollectionResponse> getCollectionsBySeason(String seasonId, int page, int size) {
        return PageResponse.of(collectionRepository.findBySeason_Id(seasonId, PageRequest.of(page, size))
                .map(collectionMapper::toCollectionResponse));
    }

    public PageResponse<CollectionResponse> getCollectionsByCategory(String categoryId, int page, int size) {
        return PageResponse.of(collectionRepository.findByCategoryCollection_Id(categoryId, PageRequest.of(page, size))
                .map(collectionMapper::toCollectionResponse));
    }

    public PageResponse<CollectionResponse> getCollectionsByArtist(String artistId, int page, int size) {
        return PageResponse.of(collectionRepository.findByArtist_Id(artistId, PageRequest.of(page, size))
                .map(collectionMapper::toCollectionResponse));
    }

    public PageResponse<CollectionResponse> getCollectionsByYear(String year, int page, int size) {
        return PageResponse.of(collectionRepository.findByYear(year, PageRequest.of(page, size))
                .map(collectionMapper::toCollectionResponse));
    }

    public CollectionResponse updateCollection(String collectionId, CollectionRequest request) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        collectionMapper.updateCollection(collection, request);
        collection.setSlug(SlugUtils.generateSlug(request.getTitle()));
        setRelations(collection, request);
        setProducts(collection, request.getProductIds());

        if (request.getThumbnail() != null) {
            if (collection.getThumbnail() != null)
                cloudinaryService.deleteImage(collection.getThumbnail());
            collection.setThumbnail(cloudinaryService.uploadImage(request.getThumbnail()));
        }

        return collectionMapper.toCollectionResponse(collectionRepository.save(collection));
    }

    public void deleteCollection(String collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        if (collection.getThumbnail() != null)
            cloudinaryService.deleteImage(collection.getThumbnail());

        collectionRepository.deleteById(collectionId);
    }

    public CollectionResponse addProductToCollection(String collectionId, String productId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (collection.getProducts().contains(product))
            throw new AppException(ErrorCode.COLLECTION_PRODUCT_EXISTED);

        collection.getProducts().add(product);
        return collectionMapper.toCollectionResponse(collectionRepository.save(collection));
    }

    public CollectionResponse removeProductFromCollection(String collectionId, String productId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        collection.getProducts().remove(product);
        return collectionMapper.toCollectionResponse(collectionRepository.save(collection));
    }

    public PageResponse<CollectionResponse> filterCollections(
            String seasonId,
            String categoryId,
            String artistId,
            String year,
            String sortBy,
            int page,
            int size) {

        Sort sort = switch (sortBy != null ? sortBy : "") {
            case "newest" -> Sort.by("year").descending();
            case "oldest" -> Sort.by("year").ascending();
            default       -> Sort.unsorted();
        };

        Specification<Collection> spec = Specification
                .where(CollectionSpecification.hasSeason(seasonId))
                .and(CollectionSpecification.hasCategory(categoryId))
                .and(CollectionSpecification.hasArtist(artistId))
                .and(CollectionSpecification.hasYear(year));

        return PageResponse.of(collectionRepository.findAll(spec, PageRequest.of(page, size, sort))
                .map(collectionMapper::toCollectionResponse));
    }

    // Set relations helper
    private void setRelations(Collection collection, CollectionRequest request) {
        collection.setSeason(seasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new AppException(ErrorCode.SEASON_NOT_FOUND)));

        collection.setCategoryCollection(categoryCollectionRepository.findById(request.getCategoryCollectionId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_COLLECTION_NOT_FOUND)));

        collection.setArtist(artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND)));
    }

    private void setProducts(Collection collection, List<String> productIds) {
        if (productIds != null && !productIds.isEmpty()) {
            List<Product> products = productRepository.findAllById(productIds);
            collection.setProducts(products);
        }
    }

}