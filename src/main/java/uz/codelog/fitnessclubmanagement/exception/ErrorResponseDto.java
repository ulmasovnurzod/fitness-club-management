package uz.codelog.fitnessclubmanagement.exception;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int statusCode,
        String message,
        LocalDateTime localDateTime
) {
}
