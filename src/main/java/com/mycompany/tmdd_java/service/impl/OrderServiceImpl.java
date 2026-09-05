package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.dto.CartDto;
import com.mycompany.tmdd_java.dto.CartItemDto;
import com.mycompany.tmdd_java.entity.*;
import com.mycompany.tmdd_java.repository.OrderItemRepository;
import com.mycompany.tmdd_java.repository.OrderRepository;
import com.mycompany.tmdd_java.repository.ProductRepository;
import com.mycompany.tmdd_java.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order createOrder(User customer, String recipientName, String phone, String address, String note, CartDto cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng của bạn đang trống!");
        }

        Order order = new Order(customer, recipientName, phone, address, note, cart.getTotalAmount());

        for (CartItemDto cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại!"));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + product.getName() + " không đủ số lượng trong kho!");
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    product,
                    product.getShop(),
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            order.addItem(orderItem);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<OrderItem> findVendorOrderItems(Long shopId) {
        return orderItemRepository.findByShopIdOrderByOrder_OrderDateDesc(shopId);
    }

    @Override
    @Transactional
    public void updateOrderItemStatus(Long orderItemId, Long shopId, OrderStatus status) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết đơn hàng!"));

        if (!item.getShop().getId().equals(shopId)) {
            throw new SecurityException("Bạn không có quyền quản lý đơn hàng này!");
        }

        item.setStatus(status);
        orderItemRepository.save(item);

        // Update main Order status if all order items share same or progressed status
        Order order = item.getOrder();
        boolean allDelivered = order.getItems().stream().allMatch(i -> i.getStatus() == OrderStatus.DELIVERED);
        boolean allCancelled = order.getItems().stream().allMatch(i -> i.getStatus() == OrderStatus.CANCELLED);
        boolean anyShipped = order.getItems().stream().anyMatch(i -> i.getStatus() == OrderStatus.SHIPPED);

        if (allDelivered) {
            order.setStatus(OrderStatus.DELIVERED);
        } else if (allCancelled) {
            order.setStatus(OrderStatus.CANCELLED);
        } else if (anyShipped) {
            order.setStatus(OrderStatus.SHIPPED);
        } else {
            order.setStatus(OrderStatus.PROCESSING);
        }
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    @Override
    public BigDecimal calculateShopRevenue(Long shopId) {
        return orderItemRepository.calculateTotalRevenueByShopId(shopId);
    }

    @Override
    public BigDecimal calculatePlatformRevenue() {
        return orderItemRepository.calculateTotalPlatformRevenue();
    }

    @Override
    public long countShopOrders(Long shopId) {
        return orderItemRepository.countDistinctOrder_IdByShopId(shopId);
    }
}
