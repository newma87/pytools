package {{Package}}.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 11:51:22
 * Description:
 */
public class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}

