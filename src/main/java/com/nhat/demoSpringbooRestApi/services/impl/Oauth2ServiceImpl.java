package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.O2authRequestDTO;
import com.nhat.demoSpringbooRestApi.models.OauthProvider;
import com.nhat.demoSpringbooRestApi.models.RefreshToken;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.OauthProviderRepository;
import com.nhat.demoSpringbooRestApi.repositories.RefreshTokenRepository;
import com.nhat.demoSpringbooRestApi.repositories.RoleRepo;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.services.Oauth2Service;
import com.nhat.demoSpringbooRestApi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class Oauth2ServiceImpl implements Oauth2Service {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OauthProviderRepository oauthProviderRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public User processOAuth2User(O2authRequestDTO o2authRequestDTO) {
        // Get user info from provider
        String providerId = o2authRequestDTO.getProviderId();
        String name = o2authRequestDTO.getName();
        String email = o2authRequestDTO.getEmail();
        // Check if user already exists
        Optional<OauthProvider> oauthProviderOptional = oauthProviderRepository.findByProviderNameAndProviderId(o2authRequestDTO.getProviderName(), providerId);
        User newUser = null;
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
                newUser = userRepository.save(user);
            } else {newUser = user;}
        } else {
            // User doesn't exist, create new user
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            Set<Role> roles = new HashSet<>();
            for (String roleName: o2authRequestDTO.getRoles()) {
                Role role = roleService.findRoleByName(roleName);
                roles.add(role);
            }
            user.setRoles(roles);
            newUser = userRepository.save(user);
            OauthProvider oauthProvider = new OauthProvider();
            oauthProvider.setProviderName(o2authRequestDTO.getProviderName());
            oauthProvider.setProviderId(providerId);
            oauthProvider.setUser(newUser);
            oauthProviderRepository.save(oauthProvider);
        }

        return newUser;

    }

    @Override
    public RefreshToken processOAuth2Tokens(O2authRequestDTO o2authRequestDTO, User user) {
        // Get tokens from provider
        String accessToken = o2authRequestDTO.getAccessToken();
        String refreshToken = o2authRequestDTO.getRefreshToken();
        // Save refresh token in database
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
//        newRefreshToken.setToken(refreshToken);
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setExpiresAt(Instant.now().plus(Duration.ofDays(7)));  // assuming the refresh token is valid for 7 days
        return  refreshTokenRepository.save(newRefreshToken);

        // For this example, we will not store the access token in the database, as it's typically short-lived and used directly by the client.
        // However, you should send the access token back to the client application in your API response.
    }

}
