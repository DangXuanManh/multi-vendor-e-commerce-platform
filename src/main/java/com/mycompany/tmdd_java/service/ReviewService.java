package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.entity.Review;
import java.util.List;

public interface ReviewService {
    Review save(Review review);
    List<Review> findByProductId(Long productId);
    List<Review> findByShopId(Long shopId);
    long countByProductId(Long productId);
    Double getAverageRatingByProductId(Long productId);
    Double getAverageRatingByShopId(Long shopId);
}
