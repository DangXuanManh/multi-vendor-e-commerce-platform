package com.mycompany.tmdd_java.repository;

import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;
import com.mycompany.tmdd_java.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByVendor(User vendor);
    Optional<Shop> findByVendorId(Long vendorId);
    List<Shop> findByStatus(ShopStatus status);
    long countByStatus(ShopStatus status);
}
