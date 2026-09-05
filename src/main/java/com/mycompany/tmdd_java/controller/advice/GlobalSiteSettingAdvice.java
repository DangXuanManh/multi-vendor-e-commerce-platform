package com.mycompany.tmdd_java.controller.advice;

import com.mycompany.tmdd_java.entity.SiteSetting;
import com.mycompany.tmdd_java.service.SiteSettingService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalSiteSettingAdvice {

    private final SiteSettingService siteSettingService;

    public GlobalSiteSettingAdvice(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @ModelAttribute("siteSetting")
    public SiteSetting getGlobalSiteSetting() {
        return siteSettingService.getSiteSetting();
    }
}
