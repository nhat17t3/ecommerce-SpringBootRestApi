package com.nhat.demoSpringbooRestApi.security;

import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);
        System.out.println(user);
        return user.map(UserInfoConfig::new).orElseThrow(() -> new UsernameNotFoundException("Can not find with useName or email:" + username));
    }
}
