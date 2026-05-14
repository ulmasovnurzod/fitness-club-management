package uz.codelog.fitnessclubmanagement.service;

import org.springframework.stereotype.Component;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.UserCreateDto;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;

@Component
public interface UserService {
    ApiResponse<?> create(RegisterRequestDto request);
}
