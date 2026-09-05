package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.dto.UserRegistrationDto;
import com.mycompany.tmdd_java.dto.VendorRegistrationDto;
import com.mycompany.tmdd_java.entity.Role;
import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;
import com.mycompany.tmdd_java.entity.User;
import com.mycompany.tmdd_java.repository.ShopRepository;
import com.mycompany.tmdd_java.repository.UserRepository;
import com.mycompany.tmdd_java.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ShopRepository shopRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerCustomer(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng!");
        }

        User user = new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getFullName(),
                dto.getEmail(),
                dto.getPhone(),
                Role.ROLE_CUSTOMER
        );
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerVendor(VendorRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng!");
        }

        User vendor = new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getFullName(),
                dto.getEmail(),
                dto.getPhone(),
                Role.ROLE_VENDOR
        );
        vendor = userRepository.save(vendor);

        Shop shop = new Shop(
                dto.getShopName(),
                dto.getShopDescription(),
                dto.getShopAddress(),
                dto.getEmail(),
                dto.getPhone(),
                vendor,
                ShopStatus.PENDING
        );
        shopRepository.save(shop);

        return vendor;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(!user.isEnabled());
            userRepository.save(user);
        });
    }
}
