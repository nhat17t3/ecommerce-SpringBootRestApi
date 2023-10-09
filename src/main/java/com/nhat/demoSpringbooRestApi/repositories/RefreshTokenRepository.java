package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.RefreshToken;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

    void deleteByToken(String token);
}
