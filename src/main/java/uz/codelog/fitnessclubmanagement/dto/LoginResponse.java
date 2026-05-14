package uz.codelog.fitnessclubmanagement.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    Long userId){

}


