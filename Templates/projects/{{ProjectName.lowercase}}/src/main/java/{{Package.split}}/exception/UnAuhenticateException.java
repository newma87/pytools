package {{Package}}.exception;

import org.springframework.security.core.AuthenticationException;

public class UnAuhenticateException extends AuthenticationException {
    public UnAuhenticateException(String message) {
        super(message);
    }
}
