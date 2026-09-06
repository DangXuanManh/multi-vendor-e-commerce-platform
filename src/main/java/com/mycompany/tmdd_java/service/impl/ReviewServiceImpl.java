package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.entity.Review;
import com.mycompany.tmdd_java.repository.ReviewRepository;
import com.mycompany.tmdd_java.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByShopId(Long shopId) {
        return reviewRepository.findByProductShopIdOrderByCreatedAtDesc(shopId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByProductId(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByProductId(Long productId) {
        Double avg = reviewRepository.findAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 5.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByShopId(Long shopId) {
        Double avg = reviewRepository.findAverageRatingByShopId(shopId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 5.0;
    }
}
