package uz.codelog.fitnessclubmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.UserCreateDto;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;
import uz.codelog.fitnessclubmanagement.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<?> create(RegisterRequestDto request) {
        return userService.create(request);
    }
}
