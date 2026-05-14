package uz.codelog.fitnessclubmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.UserResponseDto;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;
import uz.codelog.fitnessclubmanagement.entity.User;
import uz.codelog.fitnessclubmanagement.enums.ErrorType;
import uz.codelog.fitnessclubmanagement.enums.Role;
import uz.codelog.fitnessclubmanagement.enums.UserStatus;
import uz.codelog.fitnessclubmanagement.exception.RestException;
import uz.codelog.fitnessclubmanagement.mapper.UserMapper;
import uz.codelog.fitnessclubmanagement.repository.UserRepository;
import uz.codelog.fitnessclubmanagement.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<?> create(RegisterRequestDto request) {

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw RestException.restThrow(ErrorType.PHONE_ALREADY_EXISTS);
        }

        if (request.email() != null && userRepository.existsByEmail(request.email())) {
            throw RestException.restThrow(ErrorType.EMAIL_ALREADY_EXISTS);
        }

        User entity = userMapper.toEntity(request);
        entity.setEmail(request.email());
        entity.setFullName(request.fullName());
        entity.setPhoneNumber(request.phoneNumber());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRole(Role.MEMBER);
        entity.setStatus(UserStatus.ACTIVE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        User save = userRepository.save(entity);

        return ApiResponse.success(new UserResponseDto(
                save.getId(),
                save.getFullName(),
                save.getPhoneNumber(),
                save.getEmail(),
                save.getRole(),
                save.getStatus(),
                save.getCreatedAt()
                ));


    }
}
