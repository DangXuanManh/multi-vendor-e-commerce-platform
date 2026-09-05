package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.entity.Order;
import com.mycompany.tmdd_java.entity.User;
import com.mycompany.tmdd_java.service.OrderService;
import com.mycompany.tmdd_java.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final OrderService orderService;
    private final UserService userService;

    public CustomerController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public String customerOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        List<Order> orders = orderService.findCustomerOrders(customer.getId());
        model.addAttribute("orders", orders);
        return "customer/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        Order order = orderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id: " + id));

        if (!order.getCustomer().getId().equals(customer.getId()) && !customer.getRole().name().equals("ROLE_ADMIN")) {
            throw new SecurityException("Bạn không có quyền xem đơn hàng này!");
        }

        model.addAttribute("order", order);
        return "customer/order-detail";
    }
}
