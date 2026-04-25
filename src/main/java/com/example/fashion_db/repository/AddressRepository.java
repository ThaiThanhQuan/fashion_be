package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Address;
import com.example.fashion_db.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Page<Address> findAllByUserId(String userId, Pageable pageable);

    Optional<Address> findByIdAndUser(String id, User user);
}
