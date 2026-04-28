package uz.codelog.fitnessclubmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.RegisterResponseDto;
import uz.codelog.fitnessclubmanagement.dto.UserResponseDto;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;
import uz.codelog.fitnessclubmanagement.entity.User;
import uz.codelog.fitnessclubmanagement.entity.VerificationCode;
import uz.codelog.fitnessclubmanagement.enums.ErrorType;
import uz.codelog.fitnessclubmanagement.enums.Role;
import uz.codelog.fitnessclubmanagement.enums.SuccessType;
import uz.codelog.fitnessclubmanagement.enums.UserStatus;
import uz.codelog.fitnessclubmanagement.exception.RestException;
import uz.codelog.fitnessclubmanagement.mapper.UserMapper;
import uz.codelog.fitnessclubmanagement.repository.UserRepository;
import uz.codelog.fitnessclubmanagement.repository.VerificationCodeRepository;
import uz.codelog.fitnessclubmanagement.service.AuthService;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int VERIFICATION_CODE_TTL_MINUTES = 15;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public ApiResponse<?> register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.email())) {
            throw RestException.restThrow(ErrorType.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw RestException.restThrow(ErrorType.PHONE_ALREADY_EXISTS);
        }

        User entity = userMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRole(Role.MEMBER);
        entity.setStatus(UserStatus.PENDING);
        User newUser = userRepository.save(entity);

        String code = generateVerificationCode();
        saveVerificationCode(newUser, code);

        emailService.sendVerificationCode(newUser.getEmail(), code);

        return ApiResponse.success(new RegisterResponseDto(
                newUser.getId(),
                newUser.getEmail()
        ), SuccessType.VERIFICATION_CODE_SENT_TO_EMAIL);


    }

    private void saveVerificationCode(User user, String code) {
        VerificationCode verificationCode = VerificationCode.builder()
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(VERIFICATION_CODE_TTL_MINUTES))
                .user(user)
                .isUsed(false)
                .build();
        verificationCodeRepository.save(verificationCode);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(900000) + 100000;
        return String.format("%06d", num);
    }
}
