package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.dto.CartDto;
import com.mycompany.tmdd_java.entity.User;
import com.mycompany.tmdd_java.service.CartService;
import com.mycompany.tmdd_java.service.OrderService;
import com.mycompany.tmdd_java.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    public CheckoutController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String checkoutPage(@AuthenticationPrincipal UserDetails userDetails,
                               HttpSession session,
                               Model model) {
        CartDto cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        return "checkout/index";
    }

    @PostMapping("/process")
    public String processCheckout(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam String recipientName,
                                  @RequestParam String phone,
                                  @RequestParam String address,
                                  @RequestParam(required = false) String note,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        CartDto cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        try {
            orderService.createOrder(user, recipientName, phone, address, note, cart);
            cartService.clearCart(session);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt hàng thành công! Đơn hàng của bạn đã được chuyển tới các Nhà bán hàng.");
            return "redirect:/customer/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/checkout";
        }
    }
}
