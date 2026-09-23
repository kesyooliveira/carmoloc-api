package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.dtos.user.UserRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.user.UserResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.user.UserUpdateRequestDTO;
import com.br.kesyo.carmoloc_api.entities.UserEntity;
import com.br.kesyo.carmoloc_api.exceptions.ClientNotFoundException;
import com.br.kesyo.carmoloc_api.exceptions.DuplicateEmailException;
import com.br.kesyo.carmoloc_api.exceptions.DuplicateUsernameException;
import com.br.kesyo.carmoloc_api.exceptions.SelfDeleteNotAllowedException;
import com.br.kesyo.carmoloc_api.mapper.UserMapper;
import com.br.kesyo.carmoloc_api.repositories.UserRepository;
import com.br.kesyo.carmoloc_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {
        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException(request.getUsername());
        }

        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity entity = UserMapper.toEntity(request);

        UserEntity saved = this.userRepository.save(entity);

        return UserMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateRequestDTO request) {
        UserEntity entity = this.findEntityById(id);

        boolean emailChanged = !entity.getEmail().equals(request.getEmail());
        if (emailChanged && this.userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        entity.setFullName(request.getFullName());
        entity.setEmail(request.getEmail());
        entity.setRole(request.getRole());

        UserEntity saved = this.userRepository.save(entity);
        return UserMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        return UserMapper.toResponseDTO(this.findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllActive() {
        return this.userRepository.findByActiveTrue()
            .stream()
            .map(UserMapper::toResponseDTO)
            .toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        UserEntity entity = this.findEntityById(id);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (currentUsername.equals(entity.getUsername())) {
            throw new SelfDeleteNotAllowedException();
        }

        entity.setActive(false);
        this.userRepository.save(entity);
    }

    private UserEntity findEntityById(UUID id) {
        return this.userRepository.findById(id)
            .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
