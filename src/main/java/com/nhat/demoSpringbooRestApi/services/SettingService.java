package com.nhat.demoSpringbooRestApi.services;
import com.nhat.demoSpringbooRestApi.dtos.SettingDTO;
import com.nhat.demoSpringbooRestApi.models.Setting;

import java.util.List;
import java.util.Optional;

public interface SettingService {

    public List<Setting> getAllSettings();
    public Optional<Setting> getSettingByKey(String key);
    public Setting saveSetting(Setting setting);
    public void deleteSetting(int id);
    public void refreshSettings();
    public void updateMultipleSettings(List<SettingDTO> settingDTOs);

    //    List<Setting> findSettingByGroup (String group);

}
