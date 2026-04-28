package uz.codelog.fitnessclubmanagement.enums;

import lombok.Getter;

@Getter
public enum SuccessType {
    SUCCESS("success"),
    VERIFICATION_CODE_SENT_TO_EMAIL("verification.code.cent.to.email"),;
    private final String key;

    SuccessType(String s) {
        this.key = s;
    }
}
