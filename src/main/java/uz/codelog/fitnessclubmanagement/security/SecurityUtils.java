package uz.codelog.fitnessclubmanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import uz.codelog.fitnessclubmanagement.enums.ErrorType;
import uz.codelog.fitnessclubmanagement.exception.RestException;

public class SecurityUtils {

    public static String getCurrentUserEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
            throw RestException.restThrow(ErrorType.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        throw RestException.restThrow(ErrorType.UNAUTHORIZED);
    }
}
