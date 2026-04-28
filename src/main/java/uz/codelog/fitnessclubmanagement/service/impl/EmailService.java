package uz.codelog.fitnessclubmanagement.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.codelog.fitnessclubmanagement.enums.ErrorType;
import uz.codelog.fitnessclubmanagement.exception.RestException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Email Tasdiqlash Kodi");

            message.setText("""
                    Sizning tasdiqlash kodingiz: %s
                    """.formatted(code));

            mailSender.send(message);
        } catch (Exception e) {
            throw RestException.restThrow(ErrorType.EMAIL_SEND_FAILED);
        }
    }
}
