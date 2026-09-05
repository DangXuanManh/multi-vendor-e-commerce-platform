package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.entity.Product;
import com.mycompany.tmdd_java.entity.ProductStatus;
import com.mycompany.tmdd_java.repository.ProductRepository;
import com.mycompany.tmdd_java.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllActive() {
        return productRepository.findByStatus(ProductStatus.ACTIVE);
    }

    @Override
    public Page<Product> findActiveProductsPaginated(Pageable pageable) {
        return productRepository.findByStatus(ProductStatus.ACTIVE, pageable);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Page<Product> findByCategoryPaginated(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndStatus(categoryId, ProductStatus.ACTIVE, pageable);
    }

    @Override
    public List<Product> findTop8ByCategory(Long categoryId) {
        return productRepository.findTop8ByCategoryIdAndStatusOrderByIdDesc(categoryId, ProductStatus.ACTIVE);
    }

    @Override
    public Page<Product> searchActiveProductsPaginated(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return findActiveProductsPaginated(pageable);
        }
        return productRepository.findByNameContainingIgnoreCaseAndStatus(keyword.trim(), ProductStatus.ACTIVE, pageable);
    }

    @Override
    public List<Product> findByShop(Long shopId) {
        return productRepository.findByShopId(shopId);
    }

    @Override
    public Page<Product> findByShopPaginated(Long shopId, Pageable pageable) {
        return productRepository.findByShopId(shopId, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId, Long shopId) {
        productRepository.findById(productId).ifPresent(product -> {
            if (product.getShop().getId().equals(shopId)) {
                productRepository.delete(product);
            } else {
                throw new SecurityException("Bạn không có quyền xóa sản phẩm này!");
            }
        });
    }

    @Override
    public long countByShop(Long shopId) {
        return productRepository.countByShopId(shopId);
    }

    @Override
    public long countTotalProducts() {
        return productRepository.count();
    }
}
