package com.mycompany.tmdd_java.repository;

import com.mycompany.tmdd_java.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByShopIdOrderByOrder_OrderDateDesc(Long shopId);
    long countDistinctOrder_IdByShopId(Long shopId);

    @Query("SELECT COALESCE(SUM(oi.subtotal), 0) FROM OrderItem oi WHERE oi.shop.id = :shopId AND oi.status != 'CANCELLED'")
    BigDecimal calculateTotalRevenueByShopId(@Param("shopId") Long shopId);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status != 'CANCELLED'")
    BigDecimal calculateTotalPlatformRevenue();
}
