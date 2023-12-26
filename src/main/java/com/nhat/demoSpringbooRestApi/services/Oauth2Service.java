package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.O2authRequestDTO;
import com.nhat.demoSpringbooRestApi.models.RefreshToken;
import com.nhat.demoSpringbooRestApi.models.User;

import java.util.Map;

public interface Oauth2Service {
    User processOAuth2User(O2authRequestDTO o2authRequestDTO);

    RefreshToken processOAuth2Tokens(O2authRequestDTO o2authRequestDTO, User user);

//    public GoogleIdToken.Payload verify(String tokenId) throws Exception;
}