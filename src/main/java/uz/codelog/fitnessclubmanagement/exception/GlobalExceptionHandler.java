package uz.codelog.fitnessclubmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import uz.codelog.fitnessclubmanagement.dto.base.ApiResponse;
import uz.codelog.fitnessclubmanagement.enums.ErrorType;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handlerErrorResponseException(ErrorResponseException ex) {
        HttpStatus status = ex.getStatus();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                status.value(),
                ex.getMessage(),
                java.time.LocalDateTime.now()
        );

        ApiResponse<ErrorResponseDto> response = ApiResponse.error(errorResponseDto);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleRestException(RestException ex, HttpServletRequest request) {
        ErrorType errorType = ex.getErrorType();
        ApiResponse<ErrorResponseDto> response = ApiResponse.error(errorType);
        return new ResponseEntity<>(response, errorType.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiResponse<ErrorResponseDto> response = ApiResponse.error(
                ErrorType.VALIDATION_ERROR.getMsg() + ": " + details,
                400
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleJsonError(HttpMessageNotReadableException ex) {
        log.error("JSON parse error: ", ex);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMostSpecificCause().getMessage(), 400));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error("Data integrity error: ", ex);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMostSpecificCause().getMessage(), 400));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleInvalidDataAccess(InvalidDataAccessApiUsageException ex) {
        log.error("Invalid data access error: ", ex);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMostSpecificCause().getMessage(), 400));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleBadCredentials(BadCredentialsException ex) {
        ApiResponse<ErrorResponseDto> response = ApiResponse.error("Email yoki parol noto'g'ri", 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleUsernameNotFound(UsernameNotFoundException ex) {
        ApiResponse<ErrorResponseDto> response = ApiResponse.error("Foydalanuvchi topilmadi", 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<ErrorResponseDto> response = ApiResponse.error("Autentifikatsiya muvaffaqiyatsiz", 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handle404(NoResourceFoundException ex, HttpServletRequest request) {
        ApiResponse<ErrorResponseDto> response = ApiResponse.error("Not Found", 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleGeneralException(Exception ex) {
        log.error("Internal Server Error: ", ex);
        ApiResponse<ErrorResponseDto> response = ApiResponse.error(ex.getMessage(), 500);
        return ResponseEntity.internalServerError().body(response);
    }
}