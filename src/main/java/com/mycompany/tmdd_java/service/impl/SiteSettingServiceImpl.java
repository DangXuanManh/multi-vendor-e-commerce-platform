package com.mycompany.tmdd_java.service.impl;

import com.mycompany.tmdd_java.entity.SiteSetting;
import com.mycompany.tmdd_java.repository.SiteSettingRepository;
import com.mycompany.tmdd_java.service.SiteSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SiteSettingServiceImpl implements SiteSettingService {

    private final SiteSettingRepository siteSettingRepository;

    public SiteSettingServiceImpl(SiteSettingRepository siteSettingRepository) {
        this.siteSettingRepository = siteSettingRepository;
    }

    @Override
    public SiteSetting getSiteSetting() {
        SiteSetting setting = siteSettingRepository.findAll().stream().findFirst().orElseGet(() -> {
            SiteSetting defaultSetting = new SiteSetting();
            return siteSettingRepository.save(defaultSetting);
        });
        if (setting.getSiteName() == null || setting.getSiteName().equals("Sàn Thương Mại Điện Tử") || setting.getSiteName().equals("Sàn TMĐT") || setting.getSiteName().equals("Sàn Thương Mại Điện Tử Đa Nhà Bán")) {
            setting.setSiteName("JVTech Marketplace");
            setting.setContactEmail("support@jvtech.com");
            setting.setFooterText("© 2026 JVTech Marketplace. Tất cả quyền được bảo lưu.");
            return siteSettingRepository.save(setting);
        }
        return setting;
    }

    @Override
    @Transactional
    public SiteSetting saveSiteSetting(SiteSetting setting) {
        SiteSetting current = getSiteSetting();
        current.setSiteName(setting.getSiteName());
        current.setLogoUrl(setting.getLogoUrl());
        current.setContactEmail(setting.getContactEmail());
        current.setContactPhone(setting.getContactPhone());
        current.setAddress(setting.getAddress());
        current.setHeroBannerText(setting.getHeroBannerText());
        current.setFooterText(setting.getFooterText());
        current.setMaintenanceMode(setting.isMaintenanceMode());
        return siteSettingRepository.save(current);
    }
}
