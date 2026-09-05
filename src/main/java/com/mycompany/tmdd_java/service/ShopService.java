package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;

import java.util.List;
import java.util.Optional;

public interface ShopService {
    Optional<Shop> findByVendorUsername(String username);
    Optional<Shop> findById(Long id);
    List<Shop> findShopsByStatus(ShopStatus status);
    List<Shop> findAllShops();
    Shop approveShop(Long shopId);
    Shop rejectShop(Long shopId);
    Shop updateShop(Long shopId, String name, String description, String address, String phone, String logoUrl);
    long countPendingShops();
    long countApprovedShops();
}
