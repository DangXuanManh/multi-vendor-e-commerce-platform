package com.mycompany.tmdd_java.config;

import com.mycompany.tmdd_java.entity.*;
import com.mycompany.tmdd_java.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ShopRepository shopRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           OrderRepository orderRepository,
                           ReviewRepository reviewRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            updateExistingProductImages();
            return; // Data already initialized
        }

        // 1. Create Categories with Realistic Images
        Category cat1 = categoryRepository.save(new Category("Điện thoại - Máy tính", "Thiết bị điện tử, smartphone, laptop, phụ kiện công nghệ chính hãng", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600"));
        Category cat2 = categoryRepository.save(new Category("Thời trang", "Quần áo, giày dép, phụ kiện thời trang nam nữ xu hướng mới", "https://images.unsplash.com/photo-1445205170230-053b83016050?w=600"));
        Category cat3 = categoryRepository.save(new Category("Nhà cửa - Đời sống", "Đồ gia dụng thông minh, thiết bị nhà bếp, nội thất tiện ích", "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600"));
        Category cat4 = categoryRepository.save(new Category("Sách & Văn phòng phẩm", "Sách kinh doanh, kỹ năng sống, văn phòng phẩm cao cấp", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=600"));

        // 2. Create Users
        User admin = userRepository.save(new User(
                "admin",
                passwordEncoder.encode("admin123"),
                "Quản Trị Viên Sàn",
                "admin@jvtech.com",
                "0901234567",
                Role.ROLE_ADMIN
        ));

        User vendor1User = userRepository.save(new User(
                "vendor1",
                passwordEncoder.encode("vendor123"),
                "Nguyễn Văn Shop 1",
                "vendor1@jvtech.com",
                "0912345678",
                Role.ROLE_VENDOR
        ));

        User vendor2User = userRepository.save(new User(
                "vendor2",
                passwordEncoder.encode("vendor123"),
                "Trần Thị Shop 2",
                "vendor2@jvtech.com",
                "0923456789",
                Role.ROLE_VENDOR
        ));

        User vendor3User = userRepository.save(new User(
                "vendor3",
                passwordEncoder.encode("vendor123"),
                "Lê Văn Shop 3",
                "vendor3@jvtech.com",
                "0934567890",
                Role.ROLE_VENDOR
        ));

        User customer1 = userRepository.save(new User(
                "customer1",
                passwordEncoder.encode("customer123"),
                "Phạm Văn Mua Hàng",
                "customer1@gmail.com",
                "0987654321",
                Role.ROLE_CUSTOMER
        ));

        // 3. Create Shops
        Shop shop1 = new Shop(
                "Công Nghệ 247",
                "Chuyên mua bán smartphone, laptop, tai nghe và phụ kiện công nghệ chính hãng bảo hành 12-24 tháng.",
                "123 Nguyễn Trãi, Phường 2, Quận 5, TP.HCM",
                "contact@congnghe247.com",
                "0912345678",
                vendor1User,
                ShopStatus.APPROVED
        );
        shop1.setLogoUrl("https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=200");
        shop1 = shopRepository.save(shop1);

        Shop shop2 = new Shop(
                "Thời Trang Chic Style",
                "Cửa hàng thời trang nam nữ phong cách hiện đại, trẻ trung, dẫn đầu xu hướng mới nhất.",
                "45 Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM",
                "contact@chicstyle.com",
                "0923456789",
                vendor2User,
                ShopStatus.APPROVED
        );
        shop2.setLogoUrl("https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=200");
        shop2 = shopRepository.save(shop2);

        Shop shop3 = new Shop(
                "Gia Dụng SmartHome",
                "Cung cấp thiết bị gia dụng thông minh, nồi chiên, máy lọc không khí cho ngôi nhà Việt.",
                "88 Cầu Giấy, Phường Quan Hoa, Quận Cầu Giấy, Hà Nội",
                "contact@smarthome.vn",
                "0934567890",
                vendor3User,
                ShopStatus.APPROVED
        );
        shop3.setLogoUrl("https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=200");
        shop3 = shopRepository.save(shop3);

        // 4. Generate Realistic Products (>100 per shop) with Precise Matching Images
        List<Product> productsToSave = new ArrayList<>();

        // Tech Catalog
        Object[][] techCatalog = {
            {"iPhone 15 Pro Max 256GB Titan Tự Nhiên", "34990000", "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600", "Điện thoại Apple iPhone 15 Pro Max chip A17 Pro, vỏ Titan cao cấp, camera 48MP zoom 5x."},
            {"MacBook Pro 14 inch M3 Max 36GB 1TB", "52990000", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600", "Apple MacBook Pro M3 Max màn hình Liquid Retina XDR 120Hz, hiệu năng đồ họa cực khủng."},
            {"Laptop Dell XPS 15 9530 i7 16GB 512GB", "34990000", "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=600", "Laptop Dell XPS 15 Intel Core i7 13700H, RAM 16GB, SSD 512GB, RTX 4050, màn hình OLED 3.5K Touch."},
            {"Samsung Galaxy S24 Ultra 5G 512GB", "31490000", "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600", "Smartphone AI Samsung Galaxy S24 Ultra khung Titanium, bút S-Pen, camera 200MP zoom 100x."},
            {"Tai nghe Bluetooth Chống Ồn Sony WH-1000XM5", "7490000", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600", "Tai nghe chụp tai Sony WH-1000XM5 chống ồn chủ động ANC hàng đầu thế giới, thời lượng pin 30 giờ."},
            {"iPad Pro 12.9 inch M2 Wi-Fi 128GB", "26990000", "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600", "Máy tính bảng Apple iPad Pro M2 màn hình Mini-LED XDR 120Hz, hỗ trợ Apple Pencil 2."},
            {"Bàn phím cơ không dây Logitech MX Keys Mini", "2390000", "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600", "Bàn phím cơ không dây Logitech MX Keys Mini phím gõ êm ái, kết nối 3 thiết bị cùng lúc."},
            {"Chuột không dây Apple Magic Mouse 2 Black", "1890000", "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600", "Chuột cảm ứng đa điểm Apple Magic Mouse 2 thiết kế siêu mỏng nhẹ sang trọng."},
            {"Màn hình Gaming LG UltraGear 27 inch 4K 144Hz", "11990000", "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600", "Màn hình máy tính LG UltraGear IPS 4K UHD 144Hz 1ms Nano IPS hỗ trợ G-Sync và FreeSync."},
            {"Loa Bluetooth Chống Nước JBL Charge 5 40W", "3690000", "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600", "Loa di động JBL Charge 5 âm thanh JBL Original Pro Sound, chống nước IP67, pin 20 giờ."},
            {"Ổ cứng di động SSD Samsung T7 Shield 1TB", "2890000", "https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=600", "Ổ cứng di động SSD Samsung T7 Shield tốc độ đọc 1050MB/s, chống nước chống va đập chuẩn quân đội."},
            {"Đồng hồ Apple Watch Series 9 GPS 45mm", "9990000", "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=600", "Smartwatch Apple Watch Series 9 chip S9 SIP, tính năng chạm hai lần Double Tap thông minh."},
            {"Laptop Gaming Asus ROG Strix G16 i9 16GB", "42990000", "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600", "Laptop chơi game Asus ROG Strix G16 chip Intel Core i9-13980HX, VGA RTX 4070, tản nhiệt ROG Intelligent."},
            {"Smartphone Google Pixel 8 Pro 128GB Obsidian", "21990000", "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600", "Điện thoại Google Pixel 8 Pro chip Google Tensor G3, camera AI chụp đêm xuất sắc, màn hình 120Hz Super Actua."},
            {"Tai nghe Apple AirPods Pro Gen 2 USB-C", "5890000", "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600", "Tai nghe không dây Apple AirPods Pro 2 trang bị chip H2, chống ồn chủ động gấp 2 lần, cổng sạc USB-C."}
        };

        for (int i = 1; i <= 105; i++) {
            Object[] item = techCatalog[(i - 1) % techCatalog.length];
            String name = (String) item[0] + (i > 15 ? " (Mã Vạch #" + i + ")" : "");
            BigDecimal price = new BigDecimal((String) item[1]);
            String img = (String) item[2];
            String desc = (String) item[3];
            productsToSave.add(new Product(name, desc, price, 15 + (i % 25), img, cat1, shop1));
        }

        // Fashion Catalog
        Object[][] fashionCatalog = {
            {"Áo Sơ Mi Nam Cotton Oxford Tay Dài Slimfit", "450000", "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600", "Áo sơ mi nam chất liệu Cotton Oxford 100% thấm hút mồ hôi, chống nhăn nhẹ, phom dáng lịch lãm."},
            {"Đầm Xòe Nữ Lụa Hàn Thiết Kế Vintage", "680000", "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=600", "Đầm nữ dáng xòe lụa Hàn sang trọng, tôn dáng nhẹ nhàng, phù hợp đi làm, đi tiệc và dạo phố."},
            {"Giày Sneaker Nam Thể Thao Nike Air Force 1", "2890000", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600", "Giày thể thao Nike Air Force 1 '07 chất liệu da cao cấp, đệm khí Air êm ái phong cách street style."},
            {"Áo Khoác Denim Nam Nữ Form Wide Unisex", "550000", "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600", "Áo khoác bò Jean Unisex phong cách Hàn Quốc dầy dặn, bền màu, dễ phối đồ cá tính."},
            {"Quần Jean Nam Co Giãn Slimfit Xanh Đen", "390000", "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=600", "Quần jean nam chất liệu denim co giãn thoải mái, phom ôm nhẹ chân đứng dáng trẻ trung."},
            {"Túi Xách Nữ Da Thật Cao Cấp Công Sở", "1250000", "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=600", "Túi xách nữ chất liệu da bò nguyên tấm đường may tỉ mỉ, ngăn chứa rộng rãi đựng vừa ipad."},
            {"Kính Mát Chống Tia UV400 Ray-Ban Aviator", "3450000", "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600", "Kính phi công Ray-Ban gọng mạ vàng tròng kính chống chói UV400 bảo vệ mắt tối đa."},
            {"Mũ Lưỡi Trai Nón Sơn Cao Cấp Unisex", "220000", "https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=600", "Nón lưỡi trai chất liệu kaki cotton thoáng khí, chốt khóa kim loại sang trọng."},
            {"Áo Thun Unisex Cotton 100% In Hình Art", "250000", "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600", "Áo phông nam nữ cotton 250gsm thoáng mát, công nghệ in lụa sắc nét không bong tróc."},
            {"Ví Da Nam Bò Thật Khóa Thuận Tiện", "380000", "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600", "Ví nam dáng đứng da bò thật nhiều ngăn để thẻ và tiền mặt tiện dụng."},
            {"Giày Cao Gót Nữ Mũi Nhọn 7cm Da Mờ", "590000", "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=600", "Giày cao gót công sở nữ mũi nhọn gót nhọn 7cm êm chân, tôn dáng thanh lịch."},
            {"Áo Vest Nam Blazer Phong Cách Hàn Quốc", "1150000", "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=600", "Áo blazer nam 2 lớp chất tuýt si giữ phom chuẩn, may thủ công tinh tế."},
            {"Giày Thể Thao Adidas Ultraboost Light Nam", "3200000", "https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?w=600", "Giày chạy bộ Adidas Ultraboost đệm Boost năng lượng đàn hồi siêu nhẹ, ôm sát bàn chân."},
            {"Balo Du Lịch Chống Nước Herschel Supply", "1650000", "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600", "Balo cao cấp chất liệu Polyester chống thấm nước, tích hợp ngăn đựng laptop 15.6 inch xịn xò."}
        };

        for (int i = 1; i <= 105; i++) {
            Object[] item = fashionCatalog[(i - 1) % fashionCatalog.length];
            String name = (String) item[0] + (i > 14 ? " (BST #" + i + ")" : "");
            BigDecimal price = new BigDecimal((String) item[1]);
            String img = (String) item[2];
            String desc = (String) item[3];
            productsToSave.add(new Product(name, desc, price, 20 + (i % 30), img, cat2, shop2));
        }

        // Home Catalog
        Object[][] homeCatalog = {
            {"Nồi Chiên Không Dầu Philips XXL 6.2L 2000W", "3290000", "https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=600", "Nồi chiên không dầu Philips công nghệ Rapid Air giảm 90% mỡ thừa, dung tích lớn chiên gà nguyên con."},
            {"Robot Hút Bụi Lau Nhà Xiaomi Vacuum S10", "6490000", "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600", "Robot hút bụi Xiaomi lực hút 4000Pa, định vị Laser LDS vẽ bản đồ thông minh tự động tránh vật cản."},
            {"Máy Lọc Không Khí Levoit Core 300S Smart", "3890000", "https://images.unsplash.com/photo-1616627547584-bf28cee262db?w=600", "Máy lọc không khí Levoit màng lọc HEPA H13 loại bỏ 99.97% bụi mịn PM2.5 và vi khuẩn trong phòng 41m2."},
            {"Bình Đun Siêu Tốc Thủy Tinh Lock&Lock 1.8L", "650000", "https://images.unsplash.com/photo-1594212699903-ec8a3eca50f6?w=600", "Ấm siêu tốc thủy tinh chịu nhiệt Lock&Lock công suất 1850W đun sôi nhanh, tự ngắt an toàn."},
            {"Máy Ép Trái Cây Chậm Hurom H300 Hàn Quốc", "9890000", "https://images.unsplash.com/photo-1622484210800-885107928926?w=600", "Máy ép chậm Hurom công nghệ ép tự động nguyên quả giữ trọn dưỡng chất vitamin và hương vị."},
            {"Lò Vi Sóng Có Nướng Sharp 20L 800W", "1890000", "https://images.unsplash.com/photo-1585659722983-3a675dabf23d?w=600", "Lò vi sóng nướng Sharp dung tích 20L rã đông nhanh, 5 mức công suất điều khiển nút vặn bền bỉ."},
            {"Máy Xay Sinh Tố Đa Năng Panasonic 450W", "1290000", "https://images.unsplash.com/photo-1553530666-ba11a7da3888?w=600", "Máy xay sinh tố cối thủy tinh Panasonic lưỡi dao inox 4 cánh chống gỉ xay đá cực nhuyễn."},
            {"Nồi Cơm Điện Tử Cuckoo 1.8L Hàn Quốc", "2490000", "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600", "Nồi cơm điện tử cao cấp Cuckoo lòng nồi phủ chống dính Xwall Marble chín đều hạt cơm mềm dẻo."},
            {"Quạt Điều Hòa Hơi Nước Midea 50L Cool", "2990000", "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=600", "Quạt làm mát không khí Midea dung tích bình chứa 50L làm mát diện tích 30m2 tiết kiệm điện."},
            {"Bàn Ủi Hơi Nước Đứng Tefal Pro Style 1800W", "1750000", "https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?w=600", "Bàn là hơi nước đứng Tefal bình nước 1.5L phun hơi liên tục 30g/phút phẳng quần áo tức thì."},
            {"Bộ Nồi Inox 3 Đáy Sunhouse 5 Món Cao Cấp", "990000", "https://images.unsplash.com/photo-1584992236310-6edddc08acff?w=600", "Bộ nồi chảo inox 304 Sunhouse đáy từ dùng cho mọi loại bếp ga, bếp từ, bếp hồng ngoại."},
            {"Đèn Học Chống Cận Xiaomi Mi Smart LED Desk Lamp", "790000", "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600", "Đèn bàn chống cận Xiaomi điều chỉnh độ sáng qua app điện thoại, bảo vệ thị lực tuyệt đối."},
            {"Máy Pha Cà Phê Espresso Delonghi Dedica 15 Bar", "5490000", "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600", "Máy pha cafe bán tự động Delonghi áp suất 15 bar chuẩn Ý, tích hợp vòi đánh sữa tạo bọt Cappuccino thơm ngon."}
        };

        for (int i = 1; i <= 105; i++) {
            Object[] item = homeCatalog[(i - 1) % homeCatalog.length];
            String name = (String) item[0] + (i > 13 ? " (Phiên bản #" + i + ")" : "");
            BigDecimal price = new BigDecimal((String) item[1]);
            String img = (String) item[2];
            String desc = (String) item[3];
            productsToSave.add(new Product(name, desc, price, 15 + (i % 20), img, cat3, shop3));
        }

        List<Product> savedProducts = productRepository.saveAll(productsToSave);

        // 5. Create Sample Order
        Order sampleOrder = new Order(
                customer1,
                "Phạm Văn Mua Hàng",
                "0987654321",
                "789 Điện Biên Phủ, Phường 22, Quận Bình Thạnh, TP.HCM",
                "Giao giờ hành chính giúp em",
                new BigDecimal("35440000")
        );

        Product p1 = savedProducts.get(0); // iPhone 15 Pro Max
        Product p2 = savedProducts.get(105); // Áo Sơ Mi Oxford

        OrderItem item1 = new OrderItem(p1, shop1, 1, p1.getPrice());
        OrderItem item2 = new OrderItem(p2, shop2, 1, p2.getPrice());

        sampleOrder.addItem(item1);
        sampleOrder.addItem(item2);
        orderRepository.save(sampleOrder);

        // 6. Create Sample Reviews
        if (reviewRepository != null && savedProducts.size() > 0) {
            reviewRepository.save(new Review(5, "Sản phẩm dùng rất tốt, đóng gói cẩn thận, giao hàng nhanh chóng!", customer1, savedProducts.get(0)));
            reviewRepository.save(new Review(4, "Chất lượng chính hãng, nhân viên tư vấn nhiệt tình.", customer1, savedProducts.get(1)));
            reviewRepository.save(new Review(5, "Rất hài lòng với gian hàng JVTech, sẽ tiếp tục ủng hộ shop!", customer1, savedProducts.get(105)));
        }

        System.out.println(">>> Data Initialization Finished Successfully! Total Realistic Products Seeded: " + savedProducts.size());
    }

    private void updateExistingProductImages() {
        List<Product> products = productRepository.findAll();
        boolean updated = false;
        for (Product p : products) {
            String name = p.getName();
            if (name == null) continue;
            
            String newImg = null;
            if (name.contains("Quạt Điều Hòa")) {
                newImg = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=600";
            } else if (name.contains("Nồi Cơm Điện")) {
                newImg = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600";
            } else if (name.contains("Máy Xay Sinh Tố")) {
                newImg = "https://images.unsplash.com/photo-1553530666-ba11a7da3888?w=600";
            } else if (name.contains("Lò Vi Sóng")) {
                newImg = "https://images.unsplash.com/photo-1585659722983-3a675dabf23d?w=600";
            } else if (name.contains("Máy Ép Trái Cây")) {
                newImg = "https://images.unsplash.com/photo-1622484210800-885107928926?w=600";
            } else if (name.contains("Bình Đun Siêu Tốc")) {
                newImg = "https://images.unsplash.com/photo-1594212699903-ec8a3eca50f6?w=600";
            } else if (name.contains("Máy Lọc Không Khí")) {
                newImg = "https://images.unsplash.com/photo-1616627547584-bf28cee262db?w=600";
            } else if (name.contains("Robot Hút Bụi")) {
                newImg = "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600";
            } else if (name.contains("Nồi Chiên Không Dầu")) {
                newImg = "https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=600";
            } else if (name.contains("Bàn Ủi Hơi Nước")) {
                newImg = "https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?w=600";
            } else if (name.contains("Bộ Nồi Inox")) {
                newImg = "https://images.unsplash.com/photo-1584992236310-6edddc08acff?w=600";
            } else if (name.contains("Đèn Học Chống Cận")) {
                newImg = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600";
            } else if (name.contains("Máy Pha Cà Phê")) {
                newImg = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600";
            }

            if (newImg != null && !newImg.equals(p.getImageUrl())) {
                p.setImageUrl(newImg);
                updated = true;
            }
        }
        if (updated) {
            productRepository.saveAll(products);
            System.out.println(">>> Updated legacy product images in MySQL database successfully!");
        }
    }
}
