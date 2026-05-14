package uz.codelog.fitnessclubmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.codelog.fitnessclubmanagement.dto.LoginRequestDto;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.VerifyRequestDto;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;
import uz.codelog.fitnessclubmanagement.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<?> register(RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/verify")
    public ApiResponse<?> verify(VerifyRequestDto request) {
        return authService.verify(request);
    }

    @PostMapping("/login")
    public ApiResponse<?> login(LoginRequestDto request) {
        return authService.login(request);
    }
}
