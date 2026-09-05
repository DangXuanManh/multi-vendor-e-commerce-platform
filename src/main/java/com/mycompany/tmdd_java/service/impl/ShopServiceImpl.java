package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;
import com.mycompany.tmdd_java.entity.User;
import com.mycompany.tmdd_java.repository.ShopRepository;
import com.mycompany.tmdd_java.repository.UserRepository;
import com.mycompany.tmdd_java.service.ShopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public ShopServiceImpl(ShopRepository shopRepository, UserRepository userRepository) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Shop> findByVendorUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return shopRepository.findByVendor(userOpt.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Shop> findById(Long id) {
        return shopRepository.findById(id);
    }

    @Override
    public List<Shop> findShopsByStatus(ShopStatus status) {
        return shopRepository.findByStatus(status);
    }

    @Override
    public List<Shop> findAllShops() {
        return shopRepository.findAll();
    }

    @Override
    @Transactional
    public Shop approveShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Shop!"));
        shop.setStatus(ShopStatus.APPROVED);
        return shopRepository.save(shop);
    }

    @Override
    @Transactional
    public Shop rejectShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Shop!"));
        shop.setStatus(ShopStatus.REJECTED);
        return shopRepository.save(shop);
    }

    @Override
    @Transactional
    public Shop updateShop(Long shopId, String name, String description, String address, String phone, String logoUrl) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Shop!"));
        shop.setName(name);
        shop.setDescription(description);
        shop.setAddress(address);
        shop.setContactPhone(phone);
        if (logoUrl != null && !logoUrl.isBlank()) {
            shop.setLogoUrl(logoUrl);
        }
        return shopRepository.save(shop);
    }

    @Override
    public long countPendingShops() {
        return shopRepository.countByStatus(ShopStatus.PENDING);
    }

    @Override
    public long countApprovedShops() {
        return shopRepository.countByStatus(ShopStatus.APPROVED);
    }
}
