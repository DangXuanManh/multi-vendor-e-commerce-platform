package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.dto.UserRegistrationDto;
import com.mycompany.tmdd_java.dto.VendorRegistrationDto;
import com.mycompany.tmdd_java.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerCustomer(UserRegistrationDto dto);
    User registerVendor(VendorRegistrationDto dto);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllUsers();
    void toggleUserStatus(Long userId);
}
