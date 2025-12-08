package com.myfinbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{

    // Fetches an admin record based on the provided email ID
    Admin findByEmail(String email);

}
