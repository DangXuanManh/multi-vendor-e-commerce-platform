package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.dto.CartDto;
import com.mycompany.tmdd_java.dto.CartItemDto;
import com.mycompany.tmdd_java.entity.Product;
import com.mycompany.tmdd_java.service.CartService;
import com.mycompany.tmdd_java.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_KEY = "CURRENT_CART";
    private final ProductService productService;

    public CartServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public CartDto getCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new CartDto();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    @Override
    public void addToCart(HttpSession session, Long productId, int quantity) {
        CartDto cart = getCart(session);
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id: " + productId));

        cart.addItem(new CartItemDto(product, quantity));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        CartDto cart = getCart(session);
        cart.updateQuantity(productId, quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void removeFromCart(HttpSession session, Long productId) {
        CartDto cart = getCart(session);
        cart.removeItem(productId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void clearCart(HttpSession session) {
        CartDto cart = getCart(session);
        cart.clear();
        session.setAttribute(CART_SESSION_KEY, cart);
    }
}
