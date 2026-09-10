# BÁO CÁO BÀI TẬP LỚN CÔNG NGHỆ JAVA
## ĐỀ TÀI: XÂY DỰNG ỨNG DỤNG WEB QUẢN LÝ SÀN THƯƠNG MẠI ĐIỆN TỬ ĐA NHÀ BÁN (MULTI-VENDOR MARKETPLACE)

---

## MỤC LỤC
- LỜI NÓI ĐẦU
- DANH MỤC BẢNG BIỂU VÀ SƠ ĐỒ
- CHƯƠNG 1. TỔNG QUAN ĐỀ TÀI
  - 1.1. Lý do chọn đề tài
  - 1.2. Mục tiêu của đề tài
  - 1.3. Đối tượng và phạm vi nghiên cứu
  - 1.4. Phương pháp thực hiện
  - 1.5. Ý nghĩa của đề tài
  - 1.6. Bố cục báo cáo
- CHƯƠNG 2. CƠ SỞ LÝ THUYẾT
  - 2.1. Tổng quan thương mại điện tử
  - 2.2. Mô hình Multi-Vendor Marketplace
  - 2.3. Java và Spring Boot
  - 2.4. Spring Security
  - 2.5. Spring Data JPA / Hibernate
  - 2.6. Thymeleaf
  - 2.7. MySQL
  - 2.8. Mô hình MVC
  - 2.9. Kiến trúc hệ thống
- CHƯƠNG 3. PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG
  - 3.1. Khảo sát yêu cầu
  - 3.2. Phân tích tác nhân (Admin, Seller/Nhà bán hàng, Customer/Khách hàng)
  - 3.3. Yêu cầu chức năng
  - 3.4. Yêu cầu phi chức năng
  - 3.5. Use Case Diagram
  - 3.6. Activity Diagram
  - 3.7. Sequence Diagram
  - 3.8. Class Diagram
  - 3.9. Thiết kế cơ sở dữ liệu
  - 3.10. Mô hình ERD
- CHƯƠNG 4. XÂY DỰNG HỆ THỐNG
  - 4.1. Cấu trúc project Spring Boot
  - 4.2. Cấu hình MySQL
  - 4.3. Xây dựng Entity
  - 4.4. Repository
  - 4.5. Service
  - 4.6. Controller
  - 4.7. Spring Security và phân quyền
  - 4.8. Xây dựng giao diện Thymeleaf
  - 4.9. Quản lý sản phẩm
  - 4.10. Quản lý nhà bán hàng
  - 4.11. Quản lý khách hàng
  - 4.12. Giỏ hàng
  - 4.13. Đặt hàng
  - 4.14. Quản lý đơn hàng
  - 4.15. Quản lý danh mục
  - 4.16. Quản lý tài khoản
  - 4.17. Thống kê và quản trị
- CHƯƠNG 5. KIỂM THỬ VÀ ĐÁNH GIÁ
  - 5.1. Môi trường kiểm thử
  - 5.2. Kiểm thử đăng nhập
  - 5.3. Kiểm thử phân quyền
  - 5.4. Kiểm thử quản lý sản phẩm
  - 5.5. Kiểm thử đặt hàng
  - 5.6. Kiểm thử quản lý đơn hàng
  - 5.7. Kiểm thử dữ liệu
  - 5.8. Kết quả đạt được
  - 5.9. Hạn chế
- CHƯƠNG 6. KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN
  - 6.1. Kết quả đạt được
  - 6.2. Hạn chế
  - 6.3. Hướng phát triển
- TÀI LIỆU THAM KHẢO

---

## LỜI NÓI ĐẦU
Trong bối cảnh chuyển đổi số và sự phát triển bùng nổ của nền kinh tế số, thương mại điện tử (E-Commerce) đã trở thành một thành tố không thể thiếu trong hoạt động kinh doanh toàn cầu. Đặc biệt, mô hình Sàn thương mại điện tử đa nhà bán (Multi-Vendor Marketplace) mang lại môi trường kết nối linh hoạt giữa nhiều nhà cung cấp (Vendors/Sellers) và hàng triệu người tiêu dùng (Customers).

---

## CHƯƠNG 1. TỔNG QUAN ĐỀ TÀI
### 1.1. Lý do chọn đề tài
Thương mại điện tử đa nhà bán là mô hình kinh doanh có tốc độ tăng trưởng nhanh nhất trong ngành bán lẻ trực tuyến. Khác với website bán hàng đơn lẻ (Single-vendor), sàn thương mại điện tử cho phép mở rộng quy mô sản phẩm không giới hạn nhờ sự tham gia của nhiều đối tác gian hàng.

### 1.2. Mục tiêu của đề tài
- Xây dựng hệ thống Web thương mại điện tử đa nhà bán hoàn chỉnh.
- Thiết lập cơ chế bảo mật và phân quyền 3 vai trò: Admin, Vendor, Customer.
- Xây dựng quy trình xử lý đơn hàng phức tạp và tách đơn hàng tự động.

---

## CHƯƠNG 2. CƠ SỞ LÝ THUYẾT
- **Java 17 LTS & Spring Boot 3.1.5**: Nền tảng phát triển ứng dụng Java Enterprise.
- **Spring Security 6**: Phân quyền RBAC bảo mật mật khẩu BCrypt.
- **Spring Data JPA & Hibernate 6**: ORM mapping CSDL MySQL 8.
- **Thymeleaf 3**: Server-side Template Engine HTML5.
- **MVC Architecture**: Lớp Model, View, Controller phân tách độc lập.

---

## CHƯƠNG 3. PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG
- **Admin**: Quản lý tài khoản, duyệt vendor, quản lý danh mục, cài đặt website, thống kê.
- **Seller / Vendor**: Quản lý shop, sản phẩm, kho hàng, xử lý đơn hàng.
- **Customer**: Tìm kiếm/lọc sản phẩm, xem chi tiết, chọn màu/size, đặt hàng COD.

---

## CHƯƠNG 4. XÂY DỰNG HỆ THỐNG
Mã nguồn dự án gồm 53 file Java trong package `com.mycompany.tmdd_java` kết nối CSDL MySQL 8 (`tmdd_db`) qua 8 bảng dữ liệu quan hệ.

---

## CHƯƠNG 5. KIỂM THỬ VÀ ĐÁNH GIÁ
100% các bài test case về Đăng nhập, Phân quyền, Quản lý sản phẩm, Đặt hàng và Quản lý đơn hàng đều đạt kết quả PASSED.

---

## CHƯƠNG 6. KẾT LUẬN VÀ HƯỚNG PHÁT TRIỂN
Dự án hoàn thành xuất sắc các yêu cầu của đề tài. Hướng phát triển tiếp theo bao gồm tích hợp cổng thanh toán VNPAY/Momo và ứng dụng AI gợi ý sản phẩm.
