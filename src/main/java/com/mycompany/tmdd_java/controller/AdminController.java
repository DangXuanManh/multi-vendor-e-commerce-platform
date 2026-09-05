package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.entity.Category;
import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;
import com.mycompany.tmdd_java.entity.SiteSetting;
import com.mycompany.tmdd_java.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ShopService shopService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final SiteSettingService siteSettingService;

    public AdminController(ShopService shopService,
                           UserService userService,
                           ProductService productService,
                           OrderService orderService,
                           CategoryService categoryService,
                           SiteSettingService siteSettingService) {
        this.shopService = shopService;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.siteSettingService = siteSettingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalUsers = userService.findAllUsers().size();
        long totalShops = shopService.countApprovedShops();
        long pendingShops = shopService.countPendingShops();
        long totalProducts = productService.countTotalProducts();
        BigDecimal totalPlatformRevenue = orderService.calculatePlatformRevenue();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalShops", totalShops);
        model.addAttribute("pendingShops", pendingShops);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalPlatformRevenue", totalPlatformRevenue);
        return "admin/dashboard";
    }

    @GetMapping("/vendors")
    public String listVendors(@RequestParam(required = false) String status, Model model) {
        List<Shop> shops;
        if (status != null && !status.isBlank()) {
            try {
                shops = shopService.findShopsByStatus(ShopStatus.valueOf(status.toUpperCase()));
            } catch (Exception e) {
                shops = shopService.findAllShops();
            }
        } else {
            shops = shopService.findAllShops();
        }

        model.addAttribute("shops", shops);
        model.addAttribute("currentFilter", status);
        return "admin/vendors";
    }

    @PostMapping("/vendors/approve/{id}")
    public String approveVendor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        shopService.approveShop(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã duyệt cửa hàng thành công!");
        return "redirect:/admin/vendors";
    }

    @PostMapping("/vendors/reject/{id}")
    public String rejectVendor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        shopService.rejectShop(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối đơn mở cửa hàng!");
        return "redirect:/admin/vendors";
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Lưu danh mục thành công!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa danh mục đang có sản phẩm!");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleUserStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String listAllOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "admin/orders";
    }

    @GetMapping("/settings")
    public String viewSettings(Model model) {
        model.addAttribute("setting", siteSettingService.getSiteSetting());
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@ModelAttribute SiteSetting setting, RedirectAttributes redirectAttributes) {
        siteSettingService.saveSiteSetting(setting);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật cấu hình website thành công!");
        return "redirect:/admin/settings";
    }
}
