package uz.codelog.fitnessclubmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codelog.fitnessclubmanagement.entity.Device;
import uz.codelog.fitnessclubmanagement.entity.Token;
import uz.codelog.fitnessclubmanagement.entity.User;
import uz.codelog.fitnessclubmanagement.enums.TokenType;

public interface TokenRepository extends JpaRepository<Token, Long> {

    void deleteByUserAndTokenTypeAndDevice(User user, TokenType tokenType, Device device);
}