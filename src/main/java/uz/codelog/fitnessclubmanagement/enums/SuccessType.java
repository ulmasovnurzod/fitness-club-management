package uz.codelog.fitnessclubmanagement.enums;

import lombok.Getter;

@Getter
public enum SuccessType {
    SUCCESS("success");
    private final String key;

    SuccessType(String s) {
        this.key = s;
    }
}
