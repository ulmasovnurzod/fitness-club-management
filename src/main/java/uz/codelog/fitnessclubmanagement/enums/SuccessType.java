package uz.codelog.fitnessclubmanagement.enums;

import lombok.Getter;

@Getter
public enum SuccessType {
    SUCCESS("success"),
    VERIFICATION_CODE_SENT_TO_EMAIL("verification.code.cent.to.email"),
    USER_VERIFIED_SUCCESSFULLY("user.verified.successfully"),
    USER_LOGIN_SUCCESSFULLY("user.login.successfull");
    private final String key;

    SuccessType(String s) {
        this.key = s;
    }
}
