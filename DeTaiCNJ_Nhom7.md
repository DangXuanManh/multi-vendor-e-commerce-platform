# TRƯỜNG ĐẠI HỌC CÔNG NGHỆ ĐÔNG Á
## KHOA CÔNG NGHỆ THÔNG TIN

### BÀI TẬP LỚN
**HỌC PHẦN: CÔNG NGHỆ JAVA**  
**ĐỀ TÀI: XÂY DỰNG ỨNG DỤNG WEB QUẢN LÝ SÀN THƯƠNG MẠI ĐIỆN TỬ ĐA NHÀ BÁN (MULTI-VENDOR MARKETPLACE)**

**Giảng viên hướng dẫn:** Trần Xuân Thanh  
**Danh sách sinh viên thực hiện - Nhóm 7:**
1. Đặng Xuân Mạnh - 20232652 - DCCNTT14.C.3
2. Trần Ngọc Sơn - 20232472 - DCCNTT14.C.3
3. Ngô Hoàng Anh - 20232558 - DCCNTT14.C.3

*Bắc Ninh - 2026*

---

## MỤC LỤC
1. LỜI NÓI ĐẦU
2. DANH MỤC BẢNG BIỂU VÀ SƠ ĐỒ
3. DANH MỤC CÁC TỪ VIẾT TẮT
4. CHƯƠNG 1: TỔNG QUAN VỀ DỰ ÁN VÀ SÀN THƯƠNG MẠI ĐIỆN TỬ ĐA NHÀ BÁN
5. CHƯƠNG 2: CƠ SỞ LÝ THUYẾT VÀ CÔNG NGHỆ SỬ DỤNG
6. CHƯƠNG 3: PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG
7. CHƯƠNG 4: XÂY DỰNG VÀ CÀI ĐẶT HỆ THỐNG
8. CHƯƠNG 5: THỰC NGHIỆM VÀ ĐÁNH GIÁ KẾT QUẢ
9. KẾT LUẬN
10. TÀI LIỆU THAM KHẢO

---

## LỜI NÓI ĐẦU
Trong thời đại công nghệ thông tin phát triển mạnh mẽ như hiện nay, thương mại điện tử đa nhà bán (Multi-Vendor Marketplace) đã trở thành mô hình kinh doanh trực tuyến phát triển vượt bậc. Thay vì một cửa hàng đơn lẻ, sàn thương mại điện tử đa nhà bán cho phép nhiều nhà cung cấp (Vendors/Sellers) cùng đăng ký, mở gian hàng, đăng bán sản phẩm và quản lý đơn hàng độc lập trên cùng một nền tảng duy nhất.

Xuất phát từ nhu cầu thực tiễn đó, nhóm 7 chúng em đã lựa chọn đề tài: “Xây dựng ứng dụng Web Quản lý Sàn Thương mại Điện tử Đa nhà bán (Multi-Vendor Marketplace)” ứng dụng hệ sinh thái Java Spring Boot, Spring Security, Spring Data JPA, Thymeleaf và hệ quản trị cơ sở dữ liệu MySQL 8.

---

## CHƯƠNG 1: TỔNG QUAN VỀ DỰ ÁN VÀ SÀN THƯƠNG MẠI ĐIỆN TỬ ĐA NHÀ BÁN
### 1.1. Lý do chọn đề tài
Mô hình Multi-Vendor Marketplace (như Shopee, Lazada, Amazon) mang lại giá trị kinh tế lớn và khả năng mở rộng không giới hạn. Việc xây dựng một sàn thương mại điện tử đa nhà bán bằng Spring Boot giúp sinh viên làm chủ kiến trúc doanh nghiệp (Enterprise Java Framework), nắm vững cơ chế phân quyền RBAC và xử lý các bài toán nghiệp vụ phức tạp như tách đơn hàng theo nhà bán.

### 1.2. Mục tiêu của đề tài
- Xây dựng hoàn chỉnh hệ thống Web E-Commerce Đa nhà bán sử dụng Spring Boot 3.1.5 và MySQL 8.
- Thiết lập cơ chế bảo mật và phân quyền 3 cấp độ: Admin (Quản trị sàn), Vendor (Chủ gian hàng), Customer (Khách mua hàng).
- Hiện thực hóa nghiệp vụ Giỏ hàng, Đặt hàng và Tách đơn hàng tự động (Order Splitting per Vendor).
- Xây dựng giao diện responsive thân thiện với Thymeleaf và Bootstrap 5.

---

## CHƯƠNG 2: CƠ SỞ LÝ THUYẾT VÀ CÔNG NGHỆ SỬ DỤNG
- **Java 17 LTS**: Ngôn ngữ lập trình chính hiệu năng cao.
- **Spring Boot 3.1.5**: Framework khởi tạo ứng dụng enterprise nhanh chóng.
- **Spring Security 6**: Cấu hình phân quyền RBAC (Admin, Vendor, Customer).
- **Spring Data JPA & Hibernate 6**: Quản lý truy vấn ORM CSDL MySQL.
- **Thymeleaf 3**: Engine sinh giao diện HTML5 động phía Server.
- **MySQL 8**: Hệ quản trị CSDL quan hệ 8 bảng dữ liệu (`users`, `shops`, `categories`, `products`, `orders`, `order_items`, `reviews`, `site_settings`).

---

## CHƯƠNG 3: PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG
- **Admin**: Quản lý người dùng, duyệt/khóa Vendor, quản lý danh mục, cài đặt website.
- **Vendor**: Tạo/quản lý gian hàng, sản phẩm, quản lý đơn hàng theo shop.
- **Customer**: Tìm kiếm sản phẩm, xem gallery ảnh, chọn size/màu, thêm giỏ hàng, đặt hàng COD.

---

## CHƯƠNG 4: XÂY DỰNG VÀ CÀI ĐẶT HỆ THỐNG
Dự án được đóng gói theo chuẩn Maven bao gồm 53 tập tin nguồn Java trong package `com.mycompany.tmdd_java`. Hệ thống tự động tách đơn hàng của khách hàng theo từng gian hàng nhà bán (Vendor Order Splitting).

---

## CHƯƠNG 5: THỰC NGHIỆM VÀ ĐÁNH GIÁ KẾT QUẢ
Hệ thống thử nghiệm đạt kết quả biên dịch thành công (`BUILD SUCCESS`), vận hành mượt mà trên tất cả luồng chức năng.

---

## KẾT LUẬN & TÀI LIỆU THAM KHẢO
Dự án hoàn thành xuất sắc các mục tiêu nghiên cứu và phát triển ứng dụng Web thương mại điện tử đa nhà bán.
