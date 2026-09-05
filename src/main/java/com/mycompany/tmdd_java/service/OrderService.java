package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.dto.CartDto;
import com.mycompany.tmdd_java.entity.Order;
import com.mycompany.tmdd_java.entity.OrderItem;
import com.mycompany.tmdd_java.entity.OrderStatus;
import com.mycompany.tmdd_java.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(User customer, String recipientName, String phone, String address, String note, CartDto cart);
    List<Order> findCustomerOrders(Long customerId);
    Optional<Order> findOrderById(Long id);
    List<OrderItem> findVendorOrderItems(Long shopId);
    void updateOrderItemStatus(Long orderItemId, Long shopId, OrderStatus status);
    List<Order> findAllOrders();
    BigDecimal calculateShopRevenue(Long shopId);
    BigDecimal calculatePlatformRevenue();
    long countShopOrders(Long shopId);
}
