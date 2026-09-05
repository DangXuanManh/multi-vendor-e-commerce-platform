package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllActive();
    Page<Product> findActiveProductsPaginated(Pageable pageable);
    
    List<Product> findByCategory(Long categoryId);
    Page<Product> findByCategoryPaginated(Long categoryId, Pageable pageable);
    List<Product> findTop8ByCategory(Long categoryId);
    
    Page<Product> searchActiveProductsPaginated(String keyword, Pageable pageable);
    
    List<Product> findByShop(Long shopId);
    Page<Product> findByShopPaginated(Long shopId, Pageable pageable);
    
    Optional<Product> findById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long productId, Long shopId);
    long countByShop(Long shopId);
    long countTotalProducts();
}
