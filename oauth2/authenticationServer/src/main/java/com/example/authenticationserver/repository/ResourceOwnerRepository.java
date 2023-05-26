package com.example.authenticationserver.repository;

import com.example.authenticationserver.entity.ResourceOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceOwnerRepository extends JpaRepository<ResourceOwner, Long> {
    public ResourceOwner findByUseremail(String useremail);
}
