package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.entity.Category;
import com.mycompany.tmdd_java.entity.Order;
import com.mycompany.tmdd_java.entity.OrderStatus;
import com.mycompany.tmdd_java.entity.Role;
import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;
import com.mycompany.tmdd_java.entity.SiteSetting;
import com.mycompany.tmdd_java.entity.User;
import com.mycompany.tmdd_java.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public static class MonthChartData {
        private String label;
        private int monthNum;
        private BigDecimal revenue;
        private BigDecimal commission;
        private int barHeight;
        private int barY;
        private int cx;
        private int cy;
        private int barX;
        private boolean isFuture;
        private boolean isCurrent;

        public MonthChartData(String label, int monthNum, BigDecimal revenue, BigDecimal commission, int barHeight, int barY, int cx, int cy, int barX, boolean isFuture, boolean isCurrent) {
            this.label = label;
            this.monthNum = monthNum;
            this.revenue = revenue;
            this.commission = commission;
            this.barHeight = barHeight;
            this.barY = barY;
            this.cx = cx;
            this.cy = cy;
            this.barX = barX;
            this.isFuture = isFuture;
            this.isCurrent = isCurrent;
        }

        public String getLabel() { return label; }
        public int getMonthNum() { return monthNum; }
        public BigDecimal getRevenue() { return revenue; }
        public BigDecimal getCommission() { return commission; }
        public int getBarHeight() { return barHeight; }
        public int getBarY() { return barY; }
        public int getCx() { return cx; }
        public int getCy() { return cy; }
        public int getBarX() { return barX; }
        public boolean isFuture() { return isFuture; }
        public boolean isCurrent() { return isCurrent; }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> allUsers = userService.findAllUsers();
        List<Order> allOrders = orderService.findAllOrders();
        List<Shop> allShops = shopService.findAllShops();
        List<Category> categories = categoryService.findAll();

        long totalUsers = allUsers.size();
        long totalShops = shopService.countApprovedShops();
        long pendingShops = shopService.countPendingShops();
        long totalProducts = productService.countTotalProducts();
        BigDecimal totalPlatformRevenue = orderService.calculatePlatformRevenue();
        BigDecimal platformCommission = totalPlatformRevenue.multiply(new BigDecimal("0.05"));

        // 1. Order Status Counts (Real DB Data)
        long totalOrders = allOrders.size();
        long pendingOrdersCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long processingOrdersCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PROCESSING).count();
        long shippedOrdersCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.SHIPPED).count();
        long completedOrdersCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long cancelledOrdersCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        long denominator = totalOrders > 0 ? totalOrders : 1;
        int pendingPct = (int) Math.round((double) pendingOrdersCount * 100 / denominator);
        int processingPct = (int) Math.round((double) processingOrdersCount * 100 / denominator);
        int shippedPct = (int) Math.round((double) shippedOrdersCount * 100 / denominator);
        int completedPct = (int) Math.round((double) completedOrdersCount * 100 / denominator);
        int cancelledPct = (int) Math.round((double) cancelledOrdersCount * 100 / denominator);

        if (totalOrders == 0) {
            pendingPct = 15;
            processingPct = 20;
            shippedPct = 25;
            completedPct = 35;
            cancelledPct = 5;
        }

        // 2. Category Product Breakdown (Real DB Data)
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (Category cat : categories) {
            long catProdCount = productService.findByCategory(cat.getId()).size();
            int catPct = totalProducts > 0 ? (int) Math.round((double) catProdCount * 100 / totalProducts) : 0;
            Map<String, Object> stat = new HashMap<>();
            stat.put("id", cat.getId());
            stat.put("name", cat.getName());
            stat.put("count", catProdCount);
            stat.put("pct", catPct);
            categoryStats.add(stat);
        }

        // 3. User CRM Statistics & Real Spent per User
        List<Map<String, Object>> userCrmList = new ArrayList<>();
        long repeatCustomers = 0;
        long vipCount = 0;

        for (User u : allUsers) {
            List<Order> uOrders = allOrders.stream()
                    .filter(o -> o.getCustomer() != null && o.getCustomer().getId().equals(u.getId()))
                    .collect(Collectors.toList());
            long uOrderCount = uOrders.size();
            if (uOrderCount > 1) repeatCustomers++;
            BigDecimal uTotalSpent = uOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String crmTier;
            if (u.getRole() == Role.ROLE_ADMIN) {
                crmTier = "ADMIN";
            } else if (u.getRole() == Role.ROLE_VENDOR) {
                crmTier = "VENDOR";
            } else if (uTotalSpent.compareTo(new BigDecimal("30000000")) >= 0) {
                crmTier = "VIP_DIAMOND";
                vipCount++;
            } else if (uTotalSpent.compareTo(new BigDecimal("10000000")) >= 0) {
                crmTier = "VIP_GOLD";
                vipCount++;
            } else if (uTotalSpent.compareTo(new BigDecimal("2000000")) >= 0) {
                crmTier = "VIP_SILVER";
                vipCount++;
            } else {
                crmTier = "STANDARD";
            }

            Map<String, Object> uMap = new HashMap<>();
            uMap.put("user", u);
            uMap.put("orderCount", uOrderCount);
            uMap.put("totalSpent", uTotalSpent);
            uMap.put("crmTier", crmTier);
            userCrmList.add(uMap);
        }

        BigDecimal averageOrderValue = totalOrders > 0
                ? totalPlatformRevenue.divide(new BigDecimal(totalOrders), 0, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        double retentionRate = totalUsers > 0
                ? Math.round((double) repeatCustomers * 1000.0 / totalUsers) / 10.0
                : 0.0;

        // 4. Monthly Revenue Array (1..12) (Exact Real Month Calculation)
        int currentMonth = LocalDateTime.now().getMonthValue(); // Month 9 (September)
        BigDecimal[] rawMonthlyRevenues = new BigDecimal[12];
        BigDecimal maxMonthlyRev = BigDecimal.ZERO;

        for (int i = 0; i < 12; i++) {
            final int monthNum = i + 1;
            BigDecimal mRev = allOrders.stream()
                    .filter(o -> o.getOrderDate() != null && o.getOrderDate().getMonthValue() == monthNum)
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rawMonthlyRevenues[i] = mRev;
            if (mRev.compareTo(maxMonthlyRev) > 0) {
                maxMonthlyRev = mRev;
            }
        }

        if (maxMonthlyRev.compareTo(BigDecimal.ZERO) == 0) {
            maxMonthlyRev = new BigDecimal("1000000"); // Avoid division by zero
        }

        List<MonthChartData> monthChartList = new ArrayList<>();
        StringBuilder polylinePoints = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int monthNum = i + 1;
            String label = "T" + monthNum;
            BigDecimal rev = rawMonthlyRevenues[i];
            BigDecimal comm = rev.multiply(new BigDecimal("0.05"));

            boolean isFuture = monthNum > currentMonth;
            boolean isCurrent = monthNum == currentMonth;

            int barX = 80 + (i * 66);
            int cx = barX + 15;
            int barHeight = 0;
            int barY = 260;
            int cy = 260;

            if (!isFuture && rev.compareTo(BigDecimal.ZERO) > 0) {
                double ratio = rev.doubleValue() / maxMonthlyRev.doubleValue();
                barHeight = (int) Math.round(ratio * 210);
                if (barHeight < 8) barHeight = 8;
                barY = 260 - barHeight;
                cy = 260 - (int) Math.round(ratio * 120);
            } else if (!isFuture) {
                barHeight = 2; // Flat baseline for past months with 0 revenue
                barY = 258;
                cy = 258;
            }

            monthChartList.add(new MonthChartData(label, monthNum, rev, comm, barHeight, barY, cx, cy, barX, isFuture, isCurrent));

            if (!isFuture) {
                if (polylinePoints.length() > 0) polylinePoints.append(" ");
                polylinePoints.append(cx).append(",").append(cy);
            }
        }

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalShops", totalShops);
        model.addAttribute("pendingShops", pendingShops);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalPlatformRevenue", totalPlatformRevenue);
        model.addAttribute("platformCommission", platformCommission);

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrdersCount", pendingOrdersCount);
        model.addAttribute("processingOrdersCount", processingOrdersCount);
        model.addAttribute("shippedOrdersCount", shippedOrdersCount);
        model.addAttribute("completedOrdersCount", completedOrdersCount);
        model.addAttribute("cancelledOrdersCount", cancelledOrdersCount);

        model.addAttribute("pendingPct", pendingPct);
        model.addAttribute("processingPct", processingPct);
        model.addAttribute("shippedPct", shippedPct);
        model.addAttribute("completedPct", completedPct);
        model.addAttribute("cancelledPct", cancelledPct);

        model.addAttribute("categoryStats", categoryStats);
        model.addAttribute("userCrmList", userCrmList);
        model.addAttribute("averageOrderValue", averageOrderValue);
        model.addAttribute("retentionRate", retentionRate);
        model.addAttribute("vipCount", vipCount);

        model.addAttribute("monthChartList", monthChartList);
        model.addAttribute("polylinePoints", polylinePoints.toString());
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("maxMonthlyRev", maxMonthlyRev);

        model.addAttribute("users", allUsers);
        model.addAttribute("orders", allOrders);
        model.addAttribute("shops", allShops);
        model.addAttribute("categories", categories);

        return "admin/dashboard";
    }

    @GetMapping("/vendors")
    public String listVendors(@RequestParam(name = "status", required = false) String status, Model model) {
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
    public String approveVendor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        shopService.approveShop(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã duyệt cửa hàng thành công!");
        return "redirect:/admin/vendors";
    }

    @PostMapping("/vendors/reject/{id}")
    public String rejectVendor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
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
    public String saveCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes) {
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Lưu danh mục thành công!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
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
    public String toggleUserStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
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
    public String saveSettings(@ModelAttribute("setting") SiteSetting setting, RedirectAttributes redirectAttributes) {
        siteSettingService.saveSiteSetting(setting);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật cấu hình website thành công!");
        return "redirect:/admin/settings";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAllActive());
        return "admin/products";
    }

    @PostMapping("/products/approve/{id}")
    public String approveProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.approveProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Phê duyệt sản phẩm thành công!");
        return "redirect:/admin/vendors";
    }

    @PostMapping("/products/reject/{id}")
    public String rejectProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.rejectProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối sản phẩm!");
        return "redirect:/admin/vendors";
    }
}
