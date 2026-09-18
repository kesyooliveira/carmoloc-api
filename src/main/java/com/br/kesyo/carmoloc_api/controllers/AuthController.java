package com.br.kesyo.carmoloc_api.controllers;

import com.br.kesyo.carmoloc_api.dtos.auth.LoginRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.auth.LoginResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.auth.RefreshTokenRequestDTO;
import com.br.kesyo.carmoloc_api.entities.RefreshTokenEntity;
import com.br.kesyo.carmoloc_api.entities.UserEntity;
import com.br.kesyo.carmoloc_api.repositories.UserRepository;
import com.br.kesyo.carmoloc_api.security.JwtService;
import com.br.kesyo.carmoloc_api.services.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = this.userRepository.findByUsernameAndActiveTrue(request.getUsername())
            .orElseThrow();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = this.jwtService.generateToken(userDetails);
        RefreshTokenEntity refreshToken = this.refreshTokenService.createForUser(user);

        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        return ResponseEntity.ok(LoginResponseDTO.builder()
            .token(accessToken)
            .refreshToken(refreshToken.getToken())
            .username(userDetails.getUsername())
            .role(role)
            .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        RefreshTokenEntity storedToken = this.refreshTokenService.validateAndGet(request.getRefreshToken());
        UserEntity user = storedToken.getUser();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
        String newAccessToken = this.jwtService.generateToken(userDetails);
        RefreshTokenEntity newRefreshToken = this.refreshTokenService.createForUser(user);

        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        return ResponseEntity.ok(LoginResponseDTO.builder()
            .token(newAccessToken)
            .refreshToken(newRefreshToken.getToken())
            .username(userDetails.getUsername())
            .role(role)
            .build());
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.userRepository.findByUsernameAndActiveTrue(username).orElseThrow();
        this.refreshTokenService.revokeAllForUser(user.getId());
        return ResponseEntity.noContent().build();
    }
}
