package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.dto.CartDto;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    CartDto getCart(HttpSession session);
    void addToCart(HttpSession session, Long productId, int quantity);
    void updateQuantity(HttpSession session, Long productId, int quantity);
    void removeFromCart(HttpSession session, Long productId);
    void clearCart(HttpSession session);
}
