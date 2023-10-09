package com.nhat.demoSpringbooRestApi.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nhat.demoSpringbooRestApi.models.User;

import java.util.Map;

public interface Oauth2Service {
    User processOAuth2User(String provider, Map<String, Object> attributes);

    void processOAuth2Tokens(String provider, Map<String, Object> attributes, User user);

//    public GoogleIdToken.Payload verify(String tokenId) throws Exception;
}