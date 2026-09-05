package com.mycompany.tmdd_java.repository;

import com.mycompany.tmdd_java.entity.Product;
import com.mycompany.tmdd_java.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopId(Long shopId);
    Page<Product> findByShopId(Long shopId, Pageable pageable);
    
    List<Product> findByCategoryId(Long categoryId);
    Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);
    List<Product> findTop8ByCategoryIdAndStatusOrderByIdDesc(Long categoryId, ProductStatus status);
    
    List<Product> findByStatus(ProductStatus status);
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    Page<Product> findByNameContainingIgnoreCaseAndStatus(String keyword, ProductStatus status, Pageable pageable);
    
    long countByShopId(Long shopId);
}
