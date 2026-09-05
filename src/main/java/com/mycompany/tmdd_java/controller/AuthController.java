package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.dto.UserRegistrationDto;
import com.mycompany.tmdd_java.dto.VendorRegistrationDto;
import com.mycompany.tmdd_java.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto dto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.registerCustomer(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/vendor-register")
    public String vendorRegisterForm(Model model) {
        model.addAttribute("vendor", new VendorRegistrationDto());
        return "auth/vendor-register";
    }

    @PostMapping("/vendor-register")
    public String registerVendor(@Valid @ModelAttribute("vendor") VendorRegistrationDto dto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/vendor-register";
        }
        try {
            userService.registerVendor(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký gian hàng thành công! Đơn đăng ký của bạn đang được ADMIN xét duyệt.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/vendor-register";
        }
    }
}
