package uz.codelog.fitnessclubmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codelog.fitnessclubmanagement.dto.*;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;
import uz.codelog.fitnessclubmanagement.entity.Device;
import uz.codelog.fitnessclubmanagement.entity.Token;
import uz.codelog.fitnessclubmanagement.entity.User;
import uz.codelog.fitnessclubmanagement.entity.VerificationCode;
import uz.codelog.fitnessclubmanagement.enums.*;
import uz.codelog.fitnessclubmanagement.exception.RestException;
import uz.codelog.fitnessclubmanagement.mapper.UserMapper;
import uz.codelog.fitnessclubmanagement.repository.DeviceRepository;
import uz.codelog.fitnessclubmanagement.repository.TokenRepository;
import uz.codelog.fitnessclubmanagement.repository.UserRepository;
import uz.codelog.fitnessclubmanagement.repository.VerificationCodeRepository;
import uz.codelog.fitnessclubmanagement.security.JwtUtils;
import uz.codelog.fitnessclubmanagement.service.AuthService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int VERIFICATION_CODE_TTL_MINUTES = 15;
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 30;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthenticationManager authenticationManager;
    private final DeviceRepository deviceRepository;
    private final JwtUtils jwtUtils;
    private final TokenRepository tokenRepository;


    @Override
    @Transactional
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

    @Override
    @Transactional
    public ApiResponse<?> verify(VerifyRequestDto request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> RestException.restThrow(ErrorType.USER_NOT_FOUND));

        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            throw RestException.restThrow(ErrorType.USER_ALREADY_VERIFIED);
        }

        VerificationCode code = getVerificationCode(user, request.code());

        validateVerificationCode(code);

        markCodeAsUsed(code);
        activateUser(user);

        return ApiResponse.success(SuccessType.USER_VERIFIED_SUCCESSFULLY);


    }

    @Override
    @Transactional
    public ApiResponse<?> login(LoginRequestDto request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails)authenticate.getPrincipal();

        User user = (userDetails instanceof User)
                ? (User) userDetails
                : userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> RestException.restThrow(ErrorType.USER_NOT_FOUND));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw RestException.restThrow(ErrorType.USER_NOT_VERIFIED);
        }

        String deviceId = request.deviceId();

        if (deviceId == null || deviceId.isBlank()) {
            throw RestException.restThrow(ErrorType.DEVICE_ID_REQUIRED);
        }

        String finalDeviceId = deviceId;
        Device device = deviceRepository.findByDeviceIdAndUser(deviceId, user)
                .orElseGet(() -> Device.builder()
                        .deviceId(finalDeviceId)
                        .user(user)
                        .build());

        device.setLastLogin(LocalDateTime.now());
        device.setActive(true);
        deviceRepository.save(device);

        tokenRepository.deleteByUserAndTokenTypeAndDevice(user, TokenType.REFRESH_TOKEN, device);

        String accessToken = jwtUtils.generateToken(userDetails);
        Token refreshToken = createAndSaveRefreshToken(user, device, userDetails);

        log.info("User logged in successfully. userId={}, email={}, deviceId={}",
                user.getId(), user.getEmail(), deviceId);

        return ApiResponse.success(new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                user.getId()
        ), SuccessType.USER_LOGIN_SUCCESSFULLY);
    }


    private VerificationCode getVerificationCode(User user, String code) {
        return verificationCodeRepository
                .findByUserAndCode(user, code)
                .orElseThrow(() -> RestException.restThrow(ErrorType.INVALID_VERIFICATION_CODE));
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

    private void validateVerificationCode(VerificationCode verificationCode) {
        if (verificationCode.isUsed()) {
            throw RestException.restThrow(ErrorType.VERIFICATION_CODE_ALREADY_USED);
        }
    }

    private void markCodeAsUsed(VerificationCode code) {
        code.setUsed(true);
        code.setVerifiedAt(LocalDateTime.now());
        verificationCodeRepository.save(code);
    }

    private void activateUser(User user) {
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    private Token createAndSaveRefreshToken(User user, Device device, UserDetails userDetails) {
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        Token token = Token.builder()
                .token(refreshToken)
                .tokenType(TokenType.REFRESH_TOKEN)
                .expired(false)
                .revoked(false)
                .expiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS))
                .user(user)
                .device(device)
                .build();

        return tokenRepository.save(token);
    }
}
