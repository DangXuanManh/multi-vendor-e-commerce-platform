package com.mycompany.tmdd_java.service;

import com.mycompany.tmdd_java.entity.SiteSetting;

public interface SiteSettingService {
    SiteSetting getSiteSetting();
    SiteSetting saveSiteSetting(SiteSetting setting);
}
