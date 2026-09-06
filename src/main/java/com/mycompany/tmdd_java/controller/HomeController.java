package com.mycompany.tmdd_java.controller;

import com.mycompany.tmdd_java.entity.Category;
import com.mycompany.tmdd_java.entity.Product;
import com.mycompany.tmdd_java.entity.Review;
import com.mycompany.tmdd_java.entity.Shop;
import com.mycompany.tmdd_java.entity.ShopStatus;
import com.mycompany.tmdd_java.service.CategoryService;
import com.mycompany.tmdd_java.service.ProductService;
import com.mycompany.tmdd_java.service.ReviewService;
import com.mycompany.tmdd_java.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ShopService shopService;
    private final ReviewService reviewService;

    public HomeController(ProductService productService, CategoryService categoryService, ShopService shopService, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.shopService = shopService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = categoryService.findAll();
        List<Shop> shops = shopService.findShopsByStatus(ShopStatus.APPROVED);

        // Group products by Category (Top 8 items per Category for clean Homepage layout)
        Map<Long, List<Product>> categoryProductsMap = new HashMap<>();
        for (Category cat : categories) {
            categoryProductsMap.put(cat.getId(), productService.findTop8ByCategory(cat.getId()));
        }

        // Top 8 latest active products
        Pageable top8Pageable = PageRequest.of(0, 8, Sort.by("id").descending());
        Page<Product> latestProductsPage = productService.findActiveProductsPaginated(top8Pageable);

        model.addAttribute("categories", categories);
        model.addAttribute("categoryProductsMap", categoryProductsMap);
        model.addAttribute("latestProducts", latestProductsPage.getContent());
        model.addAttribute("shops", shops);
        return "index";
    }

    @GetMapping("/products")
    public String productCatalog(@RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "12") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage;

        if (categoryId != null) {
            productPage = productService.findByCategoryPaginated(categoryId, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            productPage = productService.searchActiveProductsPaginated(keyword, pageable);
        } else {
            productPage = productService.findActiveProductsPaginated(pageable);
        }

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id: " + id));
        
        Pageable limit6 = PageRequest.of(0, 6, Sort.by("id").descending());
        List<Product> shopProducts = productService.findByShopPaginated(product.getShop().getId(), limit6).getContent();
        List<Product> categoryProducts = productService.findByCategoryPaginated(product.getCategory().getId(), limit6).getContent();
        List<Review> reviews = reviewService.findByProductId(id);
        Double averageRating = reviewService.getAverageRatingByProductId(id);

        model.addAttribute("product", product);
        model.addAttribute("shopProducts", shopProducts);
        model.addAttribute("categoryProducts", categoryProducts);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviews.size());
        return "products/detail";
    }

    @GetMapping("/shops/{id}")
    public String shopDetail(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "12") int size,
                             Model model) {
        Shop shop = shopService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cửa hàng id: " + id));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage = productService.findByShopPaginated(id, pageable);

        model.addAttribute("shop", shop);
        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        return "shops/detail";
    }
}
