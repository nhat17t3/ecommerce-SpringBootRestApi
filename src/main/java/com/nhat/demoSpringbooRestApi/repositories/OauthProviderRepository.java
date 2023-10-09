package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.OauthProvider;
import com.nhat.demoSpringbooRestApi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthProviderRepository extends JpaRepository<OauthProvider, Integer> {
    Optional<OauthProvider> findByProviderNameAndProviderId(String providerName, String providerId);
}
