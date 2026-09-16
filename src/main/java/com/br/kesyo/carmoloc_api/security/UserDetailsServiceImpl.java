package com.br.kesyo.carmoloc_api.security;

import com.br.kesyo.carmoloc_api.entities.UserEntity;
import com.br.kesyo.carmoloc_api.exceptions.UsernameNotFoundException;
import com.br.kesyo.carmoloc_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = this.userRepository.findByUsernameAndActiveTrue(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
