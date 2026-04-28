package uz.codelog.fitnessclubmanagement.mapper;

import org.springframework.stereotype.Component;
import uz.codelog.fitnessclubmanagement.dto.RegisterRequestDto;
import uz.codelog.fitnessclubmanagement.dto.UserCreateDto;
import uz.codelog.fitnessclubmanagement.dto.UserResponseDto;
import uz.codelog.fitnessclubmanagement.entity.User;
import uz.codelog.fitnessclubmanagement.enums.Role;
import uz.codelog.fitnessclubmanagement.enums.UserStatus;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDto userCreateDto) {
        return  User.builder()
                .fullName(userCreateDto.fullName())
                .email(userCreateDto.email())
                .password(userCreateDto.password())
                .phoneNumber(userCreateDto.phoneNumber())
                .role(Role.MEMBER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
