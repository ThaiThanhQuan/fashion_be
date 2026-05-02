package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.ArtistRequest;
import com.example.fashion_db.dto.response.ArtistResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.entity.Artist;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.ArtistMapper;
import com.example.fashion_db.repository.ArtistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ArtistService {

    ArtistRepository artistRepository;
    ArtistMapper artistMapper;
    CloudinaryService cloudinaryService;

    public ArtistResponse createArtist(ArtistRequest request) {
        if (artistRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.ARTIST_EXISTED);

        Artist artist = artistMapper.toArtist(request);

        // Upload thumbnail lên Cloudinary
        if (request.getThumbnail() != null)
            artist.setThumbnail(cloudinaryService.uploadImage(request.getThumbnail()));

        return artistMapper.toArtistResponse(artistRepository.save(artist));
    }

    public PageResponse<ArtistResponse> getAllArtists(int page, int size) {
        return PageResponse.of(artistRepository.findAll(PageRequest.of(page, size))
                .map(artistMapper::toArtistResponse));
    }

    public ArtistResponse getArtistById(String artistId) {
        return artistMapper.toArtistResponse(
                artistRepository.findById(artistId)
                        .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND)));
    }

    public ArtistResponse updateArtist(String artistId, ArtistRequest request) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND));

        artistMapper.updateArtist(artist, request);

        // Upload thumbnail mới nếu có
        if (request.getThumbnail() != null) {
            // Xóa ảnh cũ trên Cloudinary
            if (artist.getThumbnail() != null)
                cloudinaryService.deleteImage(artist.getThumbnail());

            artist.setThumbnail(cloudinaryService.uploadImage(request.getThumbnail()));
        }

        return artistMapper.toArtistResponse(artistRepository.save(artist));
    }

    public void deleteArtist(String artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND));

        // Xóa thumbnail trên Cloudinary
        if (artist.getThumbnail() != null)
            cloudinaryService.deleteImage(artist.getThumbnail());

        artistRepository.deleteById(artistId);
    }
}