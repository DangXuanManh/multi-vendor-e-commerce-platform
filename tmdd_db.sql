-- ============================================================
-- SÀN THƯƠNG MẠI ĐIỆN TỬ ĐA NHÀ BÁN (MULTI-VENDOR MARKETPLACE)
-- Database Script SQL chuẩn dữ liệu thực tế + Hình ảnh Unsplash
-- ============================================================

CREATE DATABASE IF NOT EXISTS `tmdd_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `tmdd_db`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `shops`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `users`;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Bảng NGUỜI DÙNG (users)
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `phone` VARCHAR(20) DEFAULT NULL,
  `role` VARCHAR(20) NOT NULL,
  `enabled` BIT(1) NOT NULL DEFAULT b'1',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Bảng CỬA HÀNG / GIAN HÀNG (shops)
CREATE TABLE `shops` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `contact_email` VARCHAR(100) DEFAULT NULL,
  `contact_phone` VARCHAR(20) DEFAULT NULL,
  `logo_url` VARCHAR(255) DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  `vendor_id` BIGINT NOT NULL UNIQUE,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_shops_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Bảng DANH MỤC SẢN PHẨM (categories)
CREATE TABLE `categories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Bảng SẢN PHẨM (products)
CREATE TABLE `products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `price` DECIMAL(12,2) NOT NULL,
  `stock_quantity` INT NOT NULL DEFAULT 0,
  `image_url` VARCHAR(500) DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  `category_id` BIGINT NOT NULL,
  `shop_id` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `fk_products_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Bảng ĐƠN HÀNG (orders)
CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NOT NULL,
  `recipient_name` VARCHAR(100) NOT NULL,
  `recipient_phone` VARCHAR(20) NOT NULL,
  `shipping_address` VARCHAR(255) NOT NULL,
  `note` TEXT DEFAULT NULL,
  `total_amount` DECIMAL(12,2) NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  `order_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Bảng CHI TIẾT ĐƠN HÀNG THEO SHOP (order_items)
CREATE TABLE `order_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `product_id` BIGINT NOT NULL,
  `shop_id` BIGINT NOT NULL,
  `quantity` INT NOT NULL,
  `price` DECIMAL(12,2) NOT NULL,
  `subtotal` DECIMAL(12,2) NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_items_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Users (Password admin123 / vendor123 / customer123)
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `enabled`, `created_at`) VALUES
(1, 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVym5p.D3xX9H29x66y24z0S', 'Quản Trị Viên Sàn', 'admin@tmdd.com', '0901234567', 'ROLE_ADMIN', b'1', NOW()),
(2, 'vendor1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVym5p.D3xX9H29x66y24z0S', 'Nguyễn Văn Shop 1', 'vendor1@tmdd.com', '0912345678', 'ROLE_VENDOR', b'1', NOW()),
(3, 'vendor2', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVym5p.D3xX9H29x66y24z0S', 'Trần Thị Shop 2', 'vendor2@tmdd.com', '0923456789', 'ROLE_VENDOR', b'1', NOW()),
(4, 'vendor3', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVym5p.D3xX9H29x66y24z0S', 'Lê Văn Shop 3', 'vendor3@tmdd.com', '0934567890', 'ROLE_VENDOR', b'1', NOW()),
(5, 'customer1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVym5p.D3xX9H29x66y24z0S', 'Phạm Văn Mua Hàng', 'customer1@gmail.com', '0987654321', 'ROLE_CUSTOMER', b'1', NOW());

-- Insert Shops
INSERT INTO `shops` (`id`, `name`, `description`, `address`, `contact_email`, `contact_phone`, `logo_url`, `status`, `vendor_id`, `created_at`) VALUES
(1, 'Công Nghệ 247', 'Chuyên mua bán smartphone, laptop, tai nghe và phụ kiện công nghệ chính hãng bảo hành 12-24 tháng.', '123 Nguyễn Trãi, Phường 2, Quận 5, TP.HCM', 'contact@congnghe247.com', '0912345678', 'https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=200', 'APPROVED', 2, NOW()),
(2, 'Thời Trang Chic Style', 'Cửa hàng thời trang nam nữ phong cách hiện đại, trẻ trung, dẫn đầu xu hướng mới nhất.', '45 Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM', 'contact@chicstyle.com', '0923456789', 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=200', 'APPROVED', 3, NOW()),
(3, 'Gia Dụng SmartHome', 'Cung cấp thiết bị gia dụng thông minh, nồi chiên, máy lọc không khí cho ngôi nhà Việt.', '88 Cầu Giấy, Phường Quan Hoa, Quận Cầu Giấy, Hà Nội', 'contact@smarthome.vn', '0934567890', 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=200', 'PENDING', 4, NOW());

-- Insert Categories
INSERT INTO `categories` (`id`, `name`, `description`, `image_url`) VALUES
(1, 'Điện thoại - Máy tính', 'Thiết bị điện tử, smartphone, laptop, phụ kiện công nghệ chính hãng', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600'),
(2, 'Thời trang', 'Quần áo, giày dép, phụ kiện thời trang nam nữ xu hướng mới', 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=600'),
(3, 'Nhà cửa - Đời sống', 'Đồ gia dụng thông minh, thiết bị nhà bếp, nội thất tiện ích', 'https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600'),
(4, 'Sách & Văn phòng phẩm', 'Sách kinh doanh, kỹ năng sống, văn phòng phẩm cao cấp', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=600');

-- Stored Procedure to populate 105 realistic products per shop
DROP PROCEDURE IF EXISTS `generate_products`;

DELIMITER //
CREATE PROCEDURE `generate_products`()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 105 DO
        -- Shop 1: Realistic Tech Products
        INSERT INTO `products` (`name`, `description`, `price`, `stock_quantity`, `image_url`, `status`, `category_id`, `shop_id`, `created_at`)
        VALUES (
            ELT(((i-1) MOD 12) + 1,
                'iPhone 15 Pro Max 256GB Titan Tự Nhiên',
                'MacBook Pro 14 inch M3 Max 36GB 1TB',
                'Laptop Dell XPS 15 9530 i7 16GB 512GB',
                'Samsung Galaxy S24 Ultra 5G 512GB',
                'Tai nghe Bluetooth Chống Ồn Sony WH-1000XM5',
                'iPad Pro 12.9 inch M2 Wi-Fi 128GB',
                'Bàn phím cơ không dây Logitech MX Keys Mini',
                'Chuột không dây Apple Magic Mouse 2 Black',
                'Màn hình Gaming LG UltraGear 27 inch 4K 144Hz',
                'Loa Bluetooth Chống Nước JBL Charge 5 40W',
                'Ổ cứng di động SSD Samsung T7 Shield 1TB',
                'Đồng hồ Apple Watch Series 9 GPS 45mm'
            ),
            'Sản phẩm công nghệ cao cấp chính hãng nhập khẩu bảo hành 12-24 tháng toàn quốc.',
            (1500000.00 + (i * 350000.00)),
            (15 + (i % 25)),
            ELT(((i-1) MOD 12) + 1,
                'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600',
                'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600',
                'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=600',
                'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600',
                'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600',
                'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600',
                'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600',
                'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600',
                'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600',
                'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600',
                'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=600',
                'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=600'
            ),
            'ACTIVE', 1, 1, NOW()
        );

        -- Shop 2: Realistic Fashion Products
        INSERT INTO `products` (`name`, `description`, `price`, `stock_quantity`, `image_url`, `status`, `category_id`, `shop_id`, `created_at`)
        VALUES (
            ELT(((i-1) MOD 12) + 1,
                'Áo Sơ Mi Nam Cotton Oxford Tay Dài Slimfit',
                'Đầm Xòe Nữ Lụa Hàn Thiết Kế Vintage',
                'Giày Sneaker Nam Thể Thao Nike Air Force 1',
                'Áo Khoác Denim Nam Nữ Form Wide Unisex',
                'Quần Jean Nam Co Giãn Slimfit Xanh Đen',
                'Túi Xách Nữ Da Thật Cao Cấp Công Sở',
                'Kính Mát Chống Tia UV400 Ray-Ban Aviator',
                'Mũ Lưỡi Trai Nón Sơn Cao Cấp Unisex',
                'Áo Thun Unisex Cotton 100% In Hình Art',
                'Ví Da Nam Bò Thật Khóa Thuận Tiện',
                'Giày Cao Gót Nữ Mũi Nhọn 7cm Da Mờ',
                'Áo Vest Nam Blazer Phong Cách Hàn Quốc'
            ),
            'Thời trang cao cấp chất liệu đắt giá phom dáng tôn vẻ sang trọng hiện đại.',
            (190000.00 + (i * 25000.00)),
            (20 + (i % 30)),
            ELT(((i-1) MOD 12) + 1,
                'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600',
                'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=600',
                'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600',
                'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600',
                'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=600',
                'https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=600',
                'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600',
                'https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=600',
                'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600',
                'https://images.unsplash.com/photo-1627123424574-724758594e93?w=600',
                'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=600',
                'https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=600'
            ),
            'ACTIVE', 2, 2, NOW()
        );

        -- Shop 3: Realistic Home Appliances Products
        INSERT INTO `products` (`name`, `description`, `price`, `stock_quantity`, `image_url`, `status`, `category_id`, `shop_id`, `created_at`)
        VALUES (
            ELT(((i-1) MOD 12) + 1,
                'Nồi Chiên Không Dầu Philips XXL 6.2L 2000W',
                'Robot Hút Bụi Lau Nhà Xiaomi Vacuum S10',
                'Máy Lọc Không Khí Levoit Core 300S Smart',
                'Bình Đun Siêu Tốc Thủy Tinh Lock&Lock 1.8L',
                'Máy Ép Trái Cây Chậm Hurom H300 Hàn Quốc',
                'Lò Vi Sóng Có Nướng Sharp 20L 800W',
                'Máy Xay Sinh Tố Đa Năng Panasonic 450W',
                'Nồi Cơm Điện Tử Cuckoo 1.8L Hàn Quốc',
                'Quạt Điều Hòa Hơi Nước Midea 50L Cool',
                'Bàn Ủi Hơi Nước Đứng Tefal Pro Style 1800W',
                'Bộ Nồi Inox 3 Đáy Sunhouse 5 Món Cao Cấp',
                'Đèn Học Chống Cận Xiaomi Mi Smart LED Desk Lamp'
            ),
            'Thiết bị gia dụng căn bếp tiện nghi giải pháp cho không gian sống hiện đại.',
            (350000.00 + (i * 45000.00)),
            (15 + (i % 20)),
            ELT(((i-1) MOD 12) + 1,
                'https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=600',
                'https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600',
                'https://images.unsplash.com/photo-1616627547584-bf28cee262db?w=600',
                'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f6?w=600',
                'https://images.unsplash.com/photo-1622484210800-885107928926?w=600',
                'https://images.unsplash.com/photo-1585659722983-3a675dabf23d?w=600',
                'https://images.unsplash.com/photo-1553530666-ba11a7da3888?w=600',
                'https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600',
                'https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=600',
                'https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?w=600',
                'https://images.unsplash.com/photo-1584992236310-6edddc08acff?w=600',
                'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600'
            ),
            'ACTIVE', 3, 3, NOW()
        );
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

CALL `generate_products`();
DROP PROCEDURE IF EXISTS `generate_products`;

-- Insert Sample Orders
INSERT INTO `orders` (`id`, `customer_id`, `recipient_name`, `recipient_phone`, `shipping_address`, `note`, `total_amount`, `status`, `order_date`) VALUES
(1, 5, 'Phạm Văn Mua Hàng', '0987654321', '789 Điện Biên Phủ, Phường 22, Quận Bình Thạnh, TP.HCM', 'Giao giờ hành chính giúp em', 35440000.00, 'PENDING', NOW());

-- Insert Order Items
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `shop_id`, `quantity`, `price`, `subtotal`, `status`) VALUES
(1, 1, 1, 1, 1, 34990000.00, 34990000.00, 'PENDING'),
(2, 1, 106, 2, 1, 450000.00, 450000.00, 'PENDING');
