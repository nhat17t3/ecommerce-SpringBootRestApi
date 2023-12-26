package com.nhat.demoSpringbooRestApi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, name = "key-setting")
    private String key;

    @Column(name = "value-setting")
    private String value;

    @Column(name = "group-setting")
    private String groupSetting;

    @Column(name = "default-value")
    private String defaultValue;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Setting(String key, String value) {
        this.key = key;
        this.value=value;
    }
}

