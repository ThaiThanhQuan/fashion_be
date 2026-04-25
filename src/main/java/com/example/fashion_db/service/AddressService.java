package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.AddressRequest;
import com.example.fashion_db.dto.response.AddressResponse;
import com.example.fashion_db.entity.Address;
import com.example.fashion_db.entity.User;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.AddressMapper;
import com.example.fashion_db.repository.AddressRepository;
import com.example.fashion_db.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressService {
    AddressRepository addressRepository;
    UserRepository userRepository;
    AddressMapper addressMapper;

    public AddressResponse createAddress(AddressRequest request) {
        String name  = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Address address = addressMapper.toAddress(request);
        address.setUser(user);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public List<AddressResponse> getAllAddress() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    public List<AddressResponse> getAddressesByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        return addressRepository.findAllByUserId(userId).stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    public AddressResponse updateAddress(String addressId, AddressRequest request) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm đúng cái địa chỉ có ID đó VÀ thuộc về User đó
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        addressMapper.updateAddress(address, request);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public void deleteAddress(String addressId) {
        // 1. Lấy thông tin User đang đăng nhập
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 3. KIỂM TRA: Địa chỉ này có thuộc về User này không?
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        // 4. Nếu đúng chủ sở hữu thì mới xóa
        addressRepository.delete(address);
    }
}
