package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.*;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.RefreshToken;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.security.JWTUtil;
import com.nhat.demoSpringbooRestApi.services.Oauth2Service;
import com.nhat.demoSpringbooRestApi.services.UserService;
import com.nhat.demoSpringbooRestApi.services.impl.RefreshTokenService;
import com.nhat.demoSpringbooRestApi.security.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public ResponseEntity<BaseResponse> registerHandler(@Valid @RequestBody UserRequestDTO userRequestDTO) throws Exception {
        String encodedPass = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encodedPass);
        User user = userService.registerUser(userRequestDTO);
        String token = jwtUtil.generateToken(user.getEmail());
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("register successful", token);
        return ResponseEntity.status(200).body(baseResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> loginHandler(@Valid @RequestBody LoginRequestDTO credentials) {

        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);

        // UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(credentials.getEmail());
        User user = userService.getUserByEmail(credentials.getEmail());

        // Create a AccessToken
        String token = jwtUtil.generateToken(credentials.getEmail());

        // Create a RefreshToken
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), token, refreshToken.getToken());
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("login successful", loginResponseDTO);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse> refreshAndGetAuthenticationToken( @RequestBody LoginRequestDTO loginRequestDTO) {
//        String accessToken = authService.refreshToken(request);
        return refreshTokenService.findByToken(loginRequestDTO.getRefreshToken())
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    // Generate a new access token
                    final String jwt = jwtUtil.generateToken(user.getEmail());
                    LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), jwt, refreshToken.getToken());
                    BaseResponse baseResponse = BaseResponse.createSuccessResponse("refresh-token successful", loginResponseDTO);
                    return ResponseEntity.status(200).body(baseResponse);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));
    }

    @PostMapping("/getMyUserInfor")
    public ResponseEntity<BaseResponse> getInformationUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        String email = jwtUtil.validateTokenAndRetrieveSubject(loginRequestDTO.getAccessToken());
        User user = userService.getUserByEmail(email);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("get information user successful", user);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestParam String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("Logged out successfully");
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping("/oauth2/login-success")
    public ResponseEntity<BaseResponse> handleOAuth2LoginSuccess(@RequestBody O2authRequestDTO o2authRequestDTO) {
            // Process user info from OAuth2 provider
            User user = oauth2Service.processOAuth2User(o2authRequestDTO);
            // Process access and refresh tokens
            RefreshToken refreshToken = oauth2Service.processOAuth2Tokens(o2authRequestDTO , user);
            // Generate JWT token for user
            UsernamePasswordAuthenticationToken userAuthentication =
                    new UsernamePasswordAuthenticationToken(user, null);
            String accessToken = jwtUtil.generateToken(user.getEmail());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), accessToken, refreshToken.getToken());
            BaseResponse baseResponse = BaseResponse.createSuccessResponse("login o2auth successful", loginResponseDTO);
            return ResponseEntity.ok(baseResponse);
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
