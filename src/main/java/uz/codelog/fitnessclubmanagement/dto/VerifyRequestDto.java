package uz.codelog.fitnessclubmanagement.dto;

public record VerifyRequestDto(
        String email,
        String code
) {
}
