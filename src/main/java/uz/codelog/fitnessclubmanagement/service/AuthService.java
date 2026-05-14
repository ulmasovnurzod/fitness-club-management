package uz.codelog.fitnessclubmanagement.service;

import org.springframework.stereotype.Component;
import uz.codelog.fitnessclubmanagement.dto.LoginRequestDto;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.VerifyRequestDto;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;

@Component
public interface AuthService {
    ApiResponse<?> register(RegisterRequestDto request);

    ApiResponse<?> verify(VerifyRequestDto request);

    ApiResponse<?> login(LoginRequestDto request);
}
