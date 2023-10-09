package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.LoginRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.LoginResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.RefreshToken;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.security.JWTUtil;
import com.nhat.demoSpringbooRestApi.services.Oauth2Service;
import com.nhat.demoSpringbooRestApi.services.UserService;
import com.nhat.demoSpringbooRestApi.services.impl.RefreshTokenService;
import com.nhat.demoSpringbooRestApi.services.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "Demo Rest Api Application")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private Oauth2Service oauth2Service;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody UserRequestDTO user) throws ResourceNotFoundException {
        String encodedPass = passwordEncoder.encode(user.getPassword());

        System.out.println(encodedPass);
        user.setPassword(encodedPass);

        User userDTO = userService.registerUser(user);

        String token = jwtUtil.generateToken(userDTO.getEmail());

        return new ResponseEntity<Map<String, Object>>(Collections.singletonMap("jwt-token", token),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginHandler(@Valid @RequestBody LoginRequestDTO credentials) {

        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);

        // UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(credentials.getEmail());
        User user = userRepo.findByEmail(credentials.getEmail());

        // Create a AccessToken
        String token = jwtUtil.generateToken(credentials.getEmail());

        // Create a RefreshToken
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), token, refreshToken.getToken());
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAndGetAuthenticationToken( @RequestBody LoginRequestDTO loginRequestDTO) {
//        String accessToken = authService.refreshToken(request);
        return refreshTokenService.findByToken(loginRequestDTO.getRefreshToken())
                .map(refreshToken -> {
                    User user = refreshToken.getUser();

                    // Issue a new access token
                    final String jwt = jwtUtil.generateToken(user.getEmail());
                    LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), jwt, refreshToken.getToken());
                    return ResponseEntity.ok(loginResponseDTO);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));
    }

    @PostMapping("/getMyUserInfor")
    public ResponseEntity<?> getInformationUser(@RequestBody LoginRequestDTO loginRequestDTO) {

        String email = jwtUtil.validateTokenAndRetrieveSubject(loginRequestDTO.getAccessToken());

        User user = userRepo.findByEmail(email);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
        return ResponseEntity.ok().body("Logged out successfully");
    }

    @GetMapping("/oauth2/login-success")
    public ResponseEntity<?> handleOAuth2LoginSuccess(String providerName, Map<String, Object> attributes) {

            // Process user info from OAuth2 provider
            User user = oauth2Service.processOAuth2User(providerName, attributes);

            // Process access and refresh tokens
            oauth2Service.processOAuth2Tokens(providerName, attributes, user);

            // Generate JWT token for user
            UsernamePasswordAuthenticationToken userAuthentication =
                    new UsernamePasswordAuthenticationToken(user, null);
            String jwt = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(jwt);
    }

//    @GetMapping("/oauth2/login-success")
//    public ResponseEntity<?> handleOAuth2LoginSuccess(Authentication authentication) {
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//
//            // Process user info from OAuth2 provider
//            String provider = oauthToken.getAuthorizedClientRegistrationId();
//            OAuth2User oauth2User = oauthToken.getPrincipal();
//            Map<String, Object> attributes = oauth2User.getAttributes();
//            User user = oauth2Service.processOAuth2User(provider, attributes);
//
//            // Process access and refresh tokens
//            oauth2Service.processOAuth2Tokens(provider, attributes, user);
//
//            // Generate JWT token for user
//            UsernamePasswordAuthenticationToken userAuthentication =
//                    new UsernamePasswordAuthenticationToken(user, null);
//            String jwt = jwtUtil.generateToken(user.getEmail());
//
//
//
//            return ResponseEntity.ok(jwt);
//
//        }else {
//            // Handle unexpected authentication type
//            throw new IllegalArgumentException("Expected OAuth2AuthenticationToken, but got " + authentication);
//        }
//    }
}
