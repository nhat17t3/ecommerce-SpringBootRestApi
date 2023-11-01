package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.SettingDTO;
import com.nhat.demoSpringbooRestApi.models.Setting;
import com.nhat.demoSpringbooRestApi.repositories.SettingRepository;
import com.nhat.demoSpringbooRestApi.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private Environment env;

    @Override
    public List<Setting> getAllSettings() {
        return settingRepository.findAll();
    }

    @Override
    public Optional<Setting> getSettingByKey(String key) {
        return settingRepository.findByKey(key);
    }

    @Override
    public Setting saveSetting(Setting setting) {
        return settingRepository.save(setting);
    }

    @Override
    public void deleteSetting(int id) {
        settingRepository.deleteById(id);
    }

    @Override
    public void refreshSettings() {
        saveSetting(new Setting("email", env.getProperty("app.email")));
        saveSetting(new Setting("clientId-paypal", env.getProperty("app.clientId-paypal")));
        saveSetting(new Setting("secretID-paypal", env.getProperty("app.secretID-paypal")));
    }

    @Override
    public void updateMultipleSettings(List<SettingDTO> settingDTOs) {
        for (SettingDTO dto : settingDTOs) {
            Optional<Setting> existingSetting = settingRepository.findByKey(dto.getKey());

            if (existingSetting.isPresent()) {
                Setting setting = existingSetting.get();
                setting.setValue(dto.getValue());
                settingRepository.save(setting);
            }
            // Bạn cũng có thể xử lý trường hợp cài đặt không tồn tại, tùy thuộc vào yêu cầu của bạn.
//            Dựa trên cài đặt của bạn, bạn có thể muốn thêm các kiểm tra và xác thực bổ sung, như xác định liệu
//            tất cả các cài đặt đều tồn tại trước khi cập nhật hoặc trả về một danh sách các cài đặt không thể
//            cập nhật.
        }
    }
}