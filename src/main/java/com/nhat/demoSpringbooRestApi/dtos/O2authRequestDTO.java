package com.nhat.demoSpringbooRestApi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class O2authRequestDTO {
    private String providerName;
    private String providerId;
    private String email;
//    private boolean verified_email;
    private String name;
//    private String given_name;
//    private String family_name;
//    private String link;
//    private String picture;
    private String accessToken;
    private String refreshToken;
    private Set<String> roles;
}
