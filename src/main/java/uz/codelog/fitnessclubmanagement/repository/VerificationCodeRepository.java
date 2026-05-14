package uz.codelog.fitnessclubmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.codelog.fitnessclubmanagement.entity.User;
import uz.codelog.fitnessclubmanagement.entity.VerificationCode;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findByUserAndCode(User user, String code);
}
