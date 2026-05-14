package uz.codelog.fitnessclubmanagement.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    PHONE_ALREADY_EXISTS("Phone already exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.CONFLICT),
    UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED),

    MEMBERSHIP_PLAN_NOT_FOUND("Membership plan not found", HttpStatus.NOT_FOUND),
    ACTIVE_SUBSCRIPTION_ALREADY_EXISTS("Active subscription already exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR("validation.error", HttpStatus.BAD_REQUEST),


    PAYMENT_NOT_FOUND("Payment not found", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_SUCCESS("Payment is not successful", HttpStatus.BAD_REQUEST),

    ACCESS_DENIED("Access denied", HttpStatus.FORBIDDEN),
    INVALID_TOKEN("Invalid token", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("Token not found", HttpStatus.UNAUTHORIZED),

    BAD_REQUEST("Bad request", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_SEND_FAILED("Failed.to.send.email", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_VERIFIED("User.already.verified", HttpStatus.UNAUTHORIZED),
    INVALID_VERIFICATION_CODE("invalid.verification.code",HttpStatus.UNAUTHORIZED ),
    VERIFICATION_CODE_ALREADY_USED("verification.code.already.used",HttpStatus.CONFLICT ),
    USER_NOT_VERIFIED("User not verified", HttpStatus.UNAUTHORIZED ),
    DEVICE_ID_REQUIRED("Device ID is required", HttpStatus.BAD_REQUEST );

    private final String msg;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorType(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }

    ErrorType(String msg) {
        this.msg = msg;
    }
}
