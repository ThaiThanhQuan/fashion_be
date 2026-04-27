package com.example.fashion_db.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryService {

    Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "fashion_db/products",
                            "resource_type", "auto"
                    )
            );
            return result.get("secure_url").toString();  // trả về URL
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new AppException(ErrorCode.DELETE_IMAGE_FAILED);
        }
    }

    private String extractPublicId(String imageUrl) {
        // URL: https://res.cloudinary.com/abc/image/upload/v123/fashion_db/products/xyz.jpg
        // publicId: fashion_db/products/xyz
        String[] parts = imageUrl.split("/upload/");
        String afterUpload = parts[1];
        // bỏ version v123/
        String withoutVersion = afterUpload.replaceAll("v\\d+/", "");
        // bỏ extension .jpg
        return withoutVersion.substring(0, withoutVersion.lastIndexOf("."));
    }
}