# BÁO CÁO BÀI TẬP LỚN CÔNG NGHỆ JAVA (CNJ18)
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

---

## LỜI NÓI ĐẦU
Trong kỷ nguyên số hóa và sự phát triển bùng nổ của thương mại điện tử (E-Commerce) trên toàn cầu, phương thức mua sắm trực tuyến đã trở thành thói quen thiết yếu của hàng tỷ người tiêu dùng. Mô hình Sàn thương mại điện tử đa nhà bán (Multi-Vendor Marketplace) như Shopee, Lazada, Amazon, Tiki đóng vai trò là một hệ sinh thái thương mại trung gian quy mô lớn.

---

## CHƯƠNG 1. TỔNG QUAN ĐỀ TÀI
### 1.1. Lý do chọn đề tài
Mô hình Sàn thương mại điện tử đa nhà bán mang lại lợi thế vượt trội nhờ khả năng huy động nguồn lực sản phẩm từ hàng ngàn nhà bán hàng đối tác. Phát triển ứng dụng Web Multi-Vendor Marketplace đòi hỏi giải quyết nhiều bài toán kỹ thuật phức tạp: RBAC Security, Multi-vendor Shopping Cart, Vendor Order Splitting Algorithm.

### 1.2. Mục tiêu của đề tài
- Xây dựng hoàn chỉnh ứng dụng Web Sàn thương mại điện tử đa nhà bán JVTech Marketplace.
- Áp dụng chuẩn bảo mật Spring Security 6 phân quyền 3 vai trò: Admin, Vendor, Customer.
- Thiết kế CSDL 8 bảng chuẩn 3NF và thuật toán tách đơn hàng tự động.

### 1.3. Đối tượng và phạm vi nghiên cứu
- Đối tượng: Java 17 LTS, Spring Boot 3.1.5, Spring Security 6, Spring Data JPA, Thymeleaf 3, MySQL 8.
- Phạm vi: Phân hệ Admin, Vendor, Customer với đầy đủ các nghiệp vụ quản lý tài khoản, shop, sản phẩm, giỏ hàng, đặt hàng, tách đơn, đánh giá và thống kê.

### 1.4. Phương pháp thực hiện
1. Nghiên cứu tài liệu chính thức (Oracle Java SE 17, Spring Boot, Spring Security, MySQL).
2. Phân tích thiết kế hệ thống (UML Use Case, Activity, Sequence, Class Diagram, ERD).
3. Phát triển phần mềm Agile/Scrum theo các sprint tính năng.
4. Kiểm thử phần mềm (Ma trận Test Cases).

### 1.5. Ý nghĩa của đề tài
- Ý nghĩa học thuật: Nắm vững tư duy thiết kế phần mềm doanh nghiệp, làm chủ Spring IoC, DI, ORM Mapping, Spring Security FilterChain.
- Ý nghĩa thực tiễn: Cung cấp giải pháp mã nguồn Web Sàn thương mại điện tử đa nhà bán sẵn sàng triển khai cho các doanh nghiệp vừa và nhỏ.

### 1.6. Bố cục báo cáo
Báo cáo tập trung trình bày chi tiết Chương 1: Tổng quan đề tài.
