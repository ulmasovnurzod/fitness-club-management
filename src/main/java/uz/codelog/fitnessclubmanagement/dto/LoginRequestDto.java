package uz.codelog.fitnessclubmanagement.dto;

public record LoginRequestDto(
        String email,
        String password,
        String deviceId
) {
}
