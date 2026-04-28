package uz.codelog.fitnessclubmanagement.dto;

import uz.codelog.fitnessclubmanagement.enums.Role;
import uz.codelog.fitnessclubmanagement.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long userId,
        String fullName,
        String phoneNumber,
        String email,
        Role role,
        UserStatus status,
        LocalDateTime createAt
) {
}
