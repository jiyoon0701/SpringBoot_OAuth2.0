package com.example.authServerCode.repository;

import com.example.authServerCode.entity.ResourceOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceOwnerRepository extends JpaRepository<ResourceOwner, Long> {
    public ResourceOwner findByUsername(String username);
}
