package {{Package}}.util;

import {{Package}}.security.JWTUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 11:56:42
 * Description:
 */
public final class SecurityUtil {

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String loginName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                loginName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                loginName = (String) authentication.getPrincipal();
            }
        }
        return loginName;
    }

    public static String getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            return (String) authentication.getCredentials();
        }
        return null;
    }

    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                JWTUser springSecurityUser = (JWTUser) authentication.getPrincipal();
                String[] authorities = springSecurityUser.getRawAuthorities();
                if (authorities.length > 0) {
                    return Arrays.asList(authorities).stream()
                            .anyMatch((author) -> author.compareTo(authority) == 0);
                }
            } else {
                return authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority ->
                                grantedAuthority.getAuthority().equals(authority));
            }
        }
        return false;
    }
}
