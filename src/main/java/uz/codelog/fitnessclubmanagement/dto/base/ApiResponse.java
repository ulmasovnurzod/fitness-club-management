package uz.codelog.fitnessclubmanagement.dto.base;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.codelog.fitnessclubmanagement.enums.ErrorType;
import uz.codelog.fitnessclubmanagement.enums.SuccessType;
import uz.codelog.fitnessclubmanagement.exception.ErrorResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final Boolean success;
    private String message;
    private String msgKey;
    private T data;

    private ApiResponse(Boolean success) {
        this.success = success;
    }

    private ApiResponse(T data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    private ApiResponse(T data, Boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    private ApiResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public static <E> ApiResponse<E> success(E data) {
        return new ApiResponse<>(data, Boolean.TRUE);
    }

    public static <E> ApiResponse<E> success(E data, String message) {
        return new ApiResponse<>(data, Boolean.TRUE, message);
    }

    public static <E> ApiResponse<E> success(E data, SuccessType successType) {
        ApiResponse<E> response = new ApiResponse<>(data, Boolean.TRUE);
        response.setMsgKey(successType.getKey());
        return response;
    }

    public static <E> ApiResponse<E> success(SuccessType successType) {
        ApiResponse<E> response = new ApiResponse<>(Boolean.TRUE);
        response.setMsgKey(successType.getKey());
        return response;
    }

    public static <E> ApiResponse<E> success() {
        return new ApiResponse<>(Boolean.TRUE);
    }

    public static ApiResponse<ErrorResponseDto> error(String errorMsg, int statusCode) {
        ErrorResponseDto errorDto = new ErrorResponseDto(statusCode, errorMsg, LocalDateTime.now());
        return new ApiResponse<>(errorDto, Boolean.FALSE);
    }

    public static ApiResponse<ErrorResponseDto> error(ErrorResponseDto data) {
        return new ApiResponse<>(data, Boolean.FALSE);
    }

    public static ApiResponse<ErrorResponseDto> error(ErrorType errorType) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                errorType.getStatus().value(),
                errorType.getMsg(),
                LocalDateTime.now()
        );
        return new ApiResponse<>(errorDto, Boolean.FALSE);
    }

}
