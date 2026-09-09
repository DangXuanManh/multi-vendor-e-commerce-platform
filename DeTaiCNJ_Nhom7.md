# BÁO CÁO BÀI TẬP LỚN MÔN CÔNG NGHỆ JAVA

**Đề tài:** "Xây dựng ứng dụng web quản lý sàn thương mại điện tử đa nhà bán (Multi-vendor Marketplace) sử dụng Spring Boot, Spring Security, Spring Data JPA, Thymeleaf và MySQL"
**Tên dự án:** JVTech Marketplace  
**Nhóm thực hiện:** Nhóm 7  
**Năm thực hiện:** 2026  

---

## MỤC LỤC

1. [CHƯƠNG 1: GIỚI THIỆU TỔNG QUAN VỀ ĐỀ TÀI](#chuong-1-gioi-thieu-tong-quan-ve-de-tai)
2. [CHƯƠNG 2: CÔNG NGHỆ VÀ CÔNG CỤ SỬ DỤNG](#chuong-2-cong-nghe-va-cong-cu-su-dung)
3. [CHƯƠNG 3: PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG](#chuong-3-phan-tich-va-thiet-ke-he-thong)
4. [CHƯƠNG 4: CÀI ĐẶT VÀ TRIỂN KHAI ỨNG DỤNG](#chuong-4-cai-dat-va-trien-khai-ung-dung)
5. [CHƯƠNG 5: KẾT QUẢ VÀ ĐÁNH GIÁ](#chuong-5-ket-qua-va-danh-gia)
6. [CHƯƠNG 6: KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN](#chuong-6-ket-luan-va-huong-phat-trien)

---

## CHƯƠNG 1: GIỚI THIỆU TỔNG QUAN VỀ ĐỀ TÀI

### 1.1. Lý do chọn đề tài
Trong kỷ nguyên số hóa hiện nay, Thương mại điện tử (E-Commerce) đã trở thành một thành tố quan trọng bậc nhất của nền kinh tế toàn cầu. Mô hình Sàn thương mại điện tử Đa nhà bán (Multi-vendor Marketplace) như Shopee, Lazada, Amazon đã chứng minh tính hiệu quả vượt trội khi kết nối hàng ngàn nhà bán hàng (Vendor/Seller) với triệu người tiêu dùng (Customer) trên cùng một nền tảng chung.

Đối với sinh viên học tập chuyên ngành Công nghệ thông tin, việc nghiên cứu và làm chủ các công nghệ lập trình ứng dụng doanh nghiệp như Java Spring Boot, Spring Security, Spring Data JPA kết hợp cùng MySQL và Thymeleaf là yêu cầu vô cùng thiết thực.

### 1.2. Mục tiêu của đề tài
- Xây dựng thành công hệ thống Web Sàn thương mại điện tử Đa nhà bán hoạt động hoàn chỉnh.
- Phân quyền bảo mật đa vai trò (Multi-role Authorization) sử dụng Spring Security & BCrypt Password Encoder cho 3 Actor: `ROLE_ADMIN`, `ROLE_VENDOR`, `ROLE_CUSTOMER`.
- Quản lý danh mục sản phẩm, gian hàng, sản phẩm đa dạng với hình ảnh Unsplash chất lượng cao (100% chuẩn ảnh matching).
- Tính năng mua sắm nâng cao: Giỏ hàng Session/Database, Đặt hàng đa gian hàng (tự động phân tách chi tiết đơn hàng OrderItem theo Shop), Đánh giá review sản phẩm 5 sao, Tương tác chọn phân loại màu sắc/kích cỡ và gallery ảnh sản phẩm.
- Trang quản trị Admin và Vendor Dashboard trực quan với báo cáo doanh thu, duyệt gian hàng, quản lý đơn hàng và cấu hình hệ thống.

### 1.3. Phạm vi nghiên cứu và Đối tượng sử dụng
Hệ thống phục vụ 3 nhóm đối tượng người dùng chính:
- **Khách hàng (Customer):** Xem danh mục sản phẩm phân trang 50 mục/trang, tìm kiếm theo từ khóa/danh mục/khu vực/thương hiệu, chọn phân loại màu sắc/size, đổi ảnh xem trước, thêm giỏ hàng, đặt hàng, viết đánh giá sao.
- **Nhà bán hàng (Vendor):** Đăng ký mở gian hàng (chờ Admin duyệt), đăng tải và chỉnh sửa sản phẩm, quản lý tồn kho, theo dõi đơn hàng thuộc gian hàng của mình.
- **Quản trị viên (Admin):** Quản lý toàn bộ người dùng, duyệt/khóa gian hàng Vendor, quản lý danh mục, xem thống kê tổng quan doanh thu/đơn hàng, tùy chỉnh thông tin sàn (Site Settings).

---

## CHƯƠNG 2: CÔNG NGHỆ VÀ CÔNG CỤ SỬ DỤNG

### 2.1. Ngôn ngữ Java 17 & Spring Boot 3.1.5
Dự án sử dụng Java LTS phiên bản 17 với hiệu năng tối ưu, tính năng pattern matching, Record và Sealed classes. Spring Boot 3.1.5 đóng vai trò là khung ứng dụng chính, giúp đơn giản hóa việc khởi tạo với Embedded Tomcat Server, quản lý dependency qua Maven `pom.xml`.

### 2.2. Spring Security & Mã hóa BCrypt
Spring Security kiểm soát truy cập toàn bộ các Request URL:
- URL `/admin/**` -> Yêu cầu quyền `ROLE_ADMIN`.
- URL `/vendor/**` -> Yêu cầu quyền `ROLE_VENDOR`.
- URL `/customer/**`, `/checkout/**` -> Yêu cầu quyền `ROLE_CUSTOMER` hoặc đã đăng nhập.
- Các URL công khai `/`, `/products/**`, `/shops/**`, `/auth/**` -> Cho phép truy cập tự do (`permitAll`).

Mật khẩu người dùng được mã hóa bằng thuật toán BCrypt trong `DataInitializer.java` và `UserServiceImpl.java` đảm bảo an toàn tuyệt đối.

### 2.3. Spring Data JPA & Hibernate ORM
Tự động ánh xạ các Entity Java (`User`, `Shop`, `Category`, `Product`, `Order`, `OrderItem`, `Review`, `SiteSetting`) thành các bảng quan hệ trong CSDL MySQL. Cung cấp các hàm CRUD chuẩn và hỗ trợ Phân trang `Pageable`, `PageRequest` tối ưu truy vấn.

### 2.4. Template Engine Thymeleaf & Bootstrap 5
Giao diện được xây dựng hiện đại với Bootstrap 5, FontAwesome icons và Thymeleaf Template Layout (`layout/main.html`) giúp tái sử dụng Navbar, Footer, Alert messages nhất quán.

---

## CHƯƠNG 3: PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG

### 3.1. Sơ đồ Use Case Hệ Thống
Sơ đồ Use Case tổng quát và sơ đồ phân rã chi tiết được thiết kế native trong file `CN_Java.drawio` gồm các phân hệ:
- **Actor Customer:** Đăng ký, Đăng nhập, Tìm kiếm/Lọc sản phẩm, Xem chi tiết/Đổi ảnh/Chọn phân loại, Thêm giỏ hàng, Đặt hàng, Xem lịch sử đơn hàng, Viết đánh giá sản phẩm.
- **Actor Vendor:** Đăng ký gian hàng, Đăng sản phẩm mới, Cập nhật thông tin gian hàng, Quản lý đơn hàng của shop, Cập nhật trạng thái giao hàng.
- **Actor Admin:** Đăng nhập quản trị, Phê duyệt gian hàng Vendor (`PENDING` -> `APPROVED`/`REJECTED`), Quản lý người dùng/Khóa tài khoản, Quản lý danh mục sản phẩm, Xem thống kê báo cáo sàn, Tùy chỉnh thông tin website.

### 3.2. Sơ đồ Lớp (Class Diagram) & Kiến trúc Phân tầng (Layered Architecture)
Hệ thống tuân thủ nghiêm ngặt mô hình 4 tầng (4-Tier Architecture):
1. **Entity Layer:** Định nghĩa dữ liệu gốc trong package `com.mycompany.tmdd_java.entity`.
2. **Repository Layer:** Giao tiếp CSDL kế thừa `JpaRepository` trong package `com.mycompany.tmdd_java.repository`.
3. **Service Layer:** Xử lý logic nghiệp vụ trong package `com.mycompany.tmdd_java.service` và `service.impl`.
4. **Controller Layer:** Tiếp nhận HTTP Request và trả về View Thymeleaf trong package `com.mycompany.tmdd_java.controller`.

### 3.3. Cấu trúc Cơ sở dữ liệu (Database Schema)

| STT | Tên Bảng (Table) | Khóa Chính (PK) | Mô Tả Nhanh |
|---|---|---|---|
| 1 | `users` | `id` | Lưu thông tin tài khoản (Admin, Vendor, Customer), mật khẩu BCrypt, vai trò role. |
| 2 | `shops` | `id` | Lưu gian hàng của Vendor, logo, địa chỉ, trạng thái duyệt (`APPROVED`/`PENDING`). |
| 3 | `categories` | `id` | Lưu danh mục sản phẩm (Điện thoại, Thời trang, Gia dụng, Sách...). |
| 4 | `products` | `id` | Lưu thông tin sản phẩm, giá, số lượng kho, URL ảnh Unsplash, `category_id`, `shop_id`. |
| 5 | `orders` | `id` | Lưu thông tin đơn hàng chung, người nhận, địa chỉ, tổng tiền, `customer_id`. |
| 6 | `order_items` | `id` | Lưu chi tiết các sản phẩm trong đơn, phân chia theo `shop_id`, giá, số lượng. |
| 7 | `reviews` | `id` | Lưu đánh giá 1-5 sao và bình luận sản phẩm của người dùng. |
| 8 | `site_settings` | `id` | Lưu cấu hình tên sàn, hotline, email liên hệ toàn trang. |

---

## CHƯƠNG 4: CÀI ĐẶT VÀ TRIỂN KHAI ỨNG DỤNG

### 4.1. Cấu trúc thư mục mã nguồn
Cấu trúc dự án Java Maven:
- `src/main/java/com/mycompany/tmdd_java/config`: Cấu hình Security, Handler và DataInitializer.
- `src/main/java/com/mycompany/tmdd_java/controller`: Các Controller chính (`HomeController`, `AuthController`, `ProductController`, `CartController`, `CheckoutController`, `CustomerController`, `VendorController`, `AdminController`).
- `src/main/java/com/mycompany/tmdd_java/dto`: Data Transfer Objects (`CartDto`, `RegistrationDto`).
- `src/main/java/com/mycompany/tmdd_java/entity`: Các JPA Entities.
- `src/main/java/com/mycompany/tmdd_java/repository`: Các Spring Data JPA Repositories.
- `src/main/java/com/mycompany/tmdd_java/service`: Interfaces & Service Implementations.
- `src/main/resources/templates`: Các trang giao diện HTML Thymeleaf.

### 4.2. Các tính năng nổi bật đã hoàn thiện
1. **Tự động cập nhật hình ảnh chuẩn (Auto-Update Seed Data):** Khởi tạo 105+ sản phẩm thực tế cho mỗi gian hàng với 100% hình ảnh Unsplash khớp đúng tên sản phẩm.
2. **Phân trang sản phẩm linh hoạt:** Mặc định hiển thị 50 sản phẩm/trang, hỗ trợ lọc theo danh mục, từ khóa và sắp xếp.
3. **Đặt hàng và Phân tách Đơn hàng Đa nhà bán:** Khi khách hàng đặt mua nhiều sản phẩm từ các gian hàng khác nhau trong 1 đơn hàng, hệ thống tự động phân chia `order_items` theo từng `shop_id` để nhà bán hàng dễ dàng theo dõi và xử lý đơn.
4. **Đánh giá sản phẩm & Xếp hạng Sao:** Khách hàng đã mua hàng có thể gửi đánh giá 1-5 sao kèm bình luận, hệ thống tự động tính điểm đánh giá trung bình `averageRating` hiển thị lên trang chi tiết.
5. **Tương tác hình ảnh và phân loại:** Trang chi tiết sản phẩm hỗ trợ đổi ảnh chính khi click ảnh thu nhỏ (Gallery Strip) và chọn linh hoạt Màu sắc, Kích cỡ với phản hồi giao diện trực quan.

---

## CHƯƠNG 5: KẾT QUẢ VÀ ĐÁNH GIÁ

### 5.1. Kết quả đạt được
Hệ thống Sàn thương mại điện tử JVTech Marketplace đã được cài đặt và vận hành ổn định trên môi trường thử nghiệm với đầy đủ chức năng của 3 Actor. Mã nguồn sạch, phân chia tầng rõ ràng, biên dịch thành công 100% bằng Maven (`BUILD SUCCESS`), đồng bộ kho lưu trữ Git GitHub (`master` & `main`) và thư mục `E:\ProjectJava`.

### 5.2. Kịch bản kiểm thử (Test Cases)
- **Test Case 1:** Đăng ký / Đăng nhập phân quyền ADMIN, VENDOR, CUSTOMER -> Thành công.
- **Test Case 2:** Tìm kiếm & Lọc danh mục sản phẩm (Phân trang 50 sản phẩm) -> Đáp ứng nhanh, hiển thị chuẩn.
- **Test Case 3:** Thêm giỏ hàng, Chọn Màu sắc/Kích cỡ, Đổi ảnh preview -> Hoạt động mượt mà.
- **Test Case 4:** Đặt hàng đa shop & Phân tách đơn hàng theo Vendor -> Lưu vào CSDL chính xác.
- **Test Case 5:** Duyệt gian hàng Vendor trong Admin Dashboard -> Cập nhật trạng thái PENDING -> APPROVED lập tức.

---

## CHƯƠNG 6: KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN

### 6.1. Kết luận
Đề tài đã hoàn thành xuất sắc các mục tiêu đề ra cho bài tập lớn môn Công nghệ Java. Nhóm đã làm chủ được các công nghệ cốt lõi bao gồm Spring Boot 3, Spring Security, Spring Data JPA, Thymeleaf và MySQL. Hệ thống không chỉ đáp ứng về mặt lý thuyết mà còn đạt tiêu chuẩn cao về mặt giao diện người dùng và nghiệp vụ thực tế.

### 6.2. Hướng phát triển trong tương lai
- Tích hợp cổng thanh toán trực tuyến (VNPAY, MoMo, ZaloPay, PayPal).
- Tích hợp tính năng Chat Realtime giữa Khách hàng và Nhà bán hàng sử dụng WebSocket / STOMP.
- Áp dụng thuật toán gợi ý sản phẩm thông minh (AI Recommendation System) dựa trên lịch sử xem và mua hàng.
- Triển khai hệ thống lên đám mây (Cloud Deployment với Docker, Kubernetes, AWS/Heroku).
