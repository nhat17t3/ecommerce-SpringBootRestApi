package com.nhat.demoSpringbooRestApi.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.nhat.demoSpringbooRestApi.models.OauthProvider;
import com.nhat.demoSpringbooRestApi.models.RefreshToken;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.OauthProviderRepository;
import com.nhat.demoSpringbooRestApi.repositories.RefreshTokenRepository;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.services.Oauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class Oauth2ServiceImpl implements Oauth2Service {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private OauthProviderRepository oauthProviderRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public User processOAuth2User(String providerName, Map<String, Object> attributes) {
        // Get user info from provider
        String providerId = (String) attributes.get("id");
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");

        // Check if user already exists
        Optional<OauthProvider> oauthProviderOptional = oauthProviderRepository.findByProviderNameAndProviderId(providerName, providerId);
        if (oauthProviderOptional.isPresent()) {
            // User already exists, update info if needed
            User user = oauthProviderOptional.get().getUser();
            boolean needUpdate = false;

            if (!user.getName().equals(name)) {
                user.setName(name);
                needUpdate = true;
            }

            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
                needUpdate = true;
            }

            if (needUpdate) {
                userRepository.save(user);
            }

            return user;
        } else {
            // User doesn't exist, create new user
            User user = new User();
            user.setName(name);
            user.setEmail(email);

            userRepository.save(user);

            OauthProvider oauthProvider = new OauthProvider();
            oauthProvider.setProviderName(providerName);
            oauthProvider.setProviderId(providerId);
            oauthProvider.setUser(user);

            oauthProviderRepository.save(oauthProvider);

            return user;
        }
    }

    @Override
    public void processOAuth2Tokens(String provider, Map<String, Object> attributes, User user) {
        // Get tokens from provider
        String accessToken = (String) attributes.get("accessToken");
        String refreshToken = (String) attributes.get("refreshToken");

        // Save refresh token in database
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
        newRefreshToken.setToken(refreshToken);
        newRefreshToken.setExpiresAt(Instant.now().plus(Duration.ofDays(7)));  // assuming the refresh token is valid for 7 days

        refreshTokenRepository.save(newRefreshToken);

        // For this example, we will not store the access token in the database, as it's typically short-lived and used directly by the client.
        // However, you should send the access token back to the client application in your API response.
    }

}
