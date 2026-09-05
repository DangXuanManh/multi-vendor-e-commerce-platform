package com.mycompany.tmdd_java.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "site_settings")
public class SiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String siteName = "JVTech Marketplace";

    @Column(length = 500)
    private String logoUrl = "https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=100";

    @Column(length = 100)
    private String contactEmail = "support@jvtech.com";

    @Column(length = 20)
    private String contactPhone = "1900 1234";

    @Column(length = 255)
    private String address = "Tòa nhà Capital Place, 29 Liễu Giai, Ba Đình, Hà Nội";

    @Column(length = 500)
    private String heroBannerText = "Khám phá hàng trăm sản phẩm hấp dẫn từ nhiều gian hàng uy tín!";

    @Column(length = 255)
    private String footerText = "© 2026 JVTech Marketplace. Tất cả quyền được bảo lưu.";

    private boolean maintenanceMode = false;

    public SiteSetting() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeroBannerText() {
        return heroBannerText;
    }

    public void setHeroBannerText(String heroBannerText) {
        this.heroBannerText = heroBannerText;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }
}
