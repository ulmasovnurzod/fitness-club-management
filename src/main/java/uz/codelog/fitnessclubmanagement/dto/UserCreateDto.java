package uz.codelog.fitnessclubmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateDto(

        @NotBlank(message = "fullName bo'sh bo'lmasin")
        String fullName,

        @NotNull(message = "Email noto'g'ri")
        String email,

        @NotBlank(message = "telefon nomer xato ")
        String phoneNumber,

        @NotBlank(message = "Password bo'sh bo'lmasin")
        String password

) {
}
