package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.entity.*;
import com.mycompany.tmdd_java.service.CategoryService;
import com.mycompany.tmdd_java.service.OrderService;
import com.mycompany.tmdd_java.service.ProductService;
import com.mycompany.tmdd_java.service.ShopService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/vendor")
public class VendorController {

    private final ShopService shopService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;

    public VendorController(ShopService shopService, ProductService productService, OrderService orderService, CategoryService categoryService) {
        this.shopService = shopService;
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

    private Shop getVendorShop(UserDetails userDetails) {
        return shopService.findByVendorUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Cửa hàng không tồn tại cho tài khoản này!"));
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Shop shop = getVendorShop(userDetails);

        long productCount = productService.countByShop(shop.getId());
        long orderCount = orderService.countShopOrders(shop.getId());
        BigDecimal totalRevenue = orderService.calculateShopRevenue(shop.getId());

        model.addAttribute("shop", shop);
        model.addAttribute("productCount", productCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("totalRevenue", totalRevenue);
        return "vendor/dashboard";
    }

    @GetMapping("/products")
    public String listProducts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Shop shop = getVendorShop(userDetails);
        List<Product> products = productService.findByShop(shop.getId());

        model.addAttribute("shop", shop);
        model.addAttribute("products", products);
        return "vendor/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Shop shop = getVendorShop(userDetails);

        model.addAttribute("shop", shop);
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "vendor/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Shop shop = getVendorShop(userDetails);
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại id: " + id));

        if (!product.getShop().getId().equals(shop.getId())) {
            throw new SecurityException("Bạn không có quyền sửa sản phẩm của Shop khác!");
        }

        model.addAttribute("shop", shop);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "vendor/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam Long categoryId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        Shop shop = getVendorShop(userDetails);

        if (shop.getStatus() != ShopStatus.APPROVED) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cửa hàng của bạn chưa được ADMIN duyệt! Không thể đăng sản phẩm.");
            return "redirect:/vendor/products";
        }

        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));

        if (product.getId() != null) {
            Product existing = productService.findById(product.getId()).orElseThrow();
            if (!existing.getShop().getId().equals(shop.getId())) {
                throw new SecurityException("Không có quyền chỉnh sửa!");
            }
        }

        product.setShop(shop);
        product.setCategory(category);
        productService.saveProduct(product);

        redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công!");
        return "redirect:/vendor/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Shop shop = getVendorShop(userDetails);
        try {
            productService.deleteProduct(id, shop.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vendor/products";
    }

    @GetMapping("/orders")
    public String vendorOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Shop shop = getVendorShop(userDetails);
        List<OrderItem> orderItems = orderService.findVendorOrderItems(shop.getId());

        model.addAttribute("shop", shop);
        model.addAttribute("orderItems", orderItems);
        return "vendor/orders";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Long orderItemId,
                                    @RequestParam OrderStatus status,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        Shop shop = getVendorShop(userDetails);
        try {
            orderService.updateOrderItemStatus(orderItemId, shop.getId(), status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vendor/orders";
    }

    @GetMapping("/shop")
    public String shopProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Shop shop = getVendorShop(userDetails);
        model.addAttribute("shop", shop);
        return "vendor/shop-profile";
    }

    @PostMapping("/shop/update")
    public String updateShopProfile(@RequestParam String name,
                                    @RequestParam String description,
                                    @RequestParam String address,
                                    @RequestParam String contactPhone,
                                    @RequestParam(required = false) String logoUrl,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        Shop shop = getVendorShop(userDetails);
        shopService.updateShop(shop.getId(), name, description, address, contactPhone, logoUrl);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cửa hàng thành công!");
        return "redirect:/vendor/shop";
    }
}
