package com.br.kesyo.carmoloc_api.controllers;

import com.br.kesyo.carmoloc_api.dtos.auth.LoginRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.auth.LoginResponseDTO;
import com.br.kesyo.carmoloc_api.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        return ResponseEntity.ok(LoginResponseDTO.builder()
            .token(token)
            .username(userDetails.getUsername())
            .role(role)
            .build());

    }
}
