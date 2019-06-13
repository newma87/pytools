package {{Package}}.util;

import {{Package}}.security.JWTUser;
import {{Package}}.security.SystemPrivelege;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                JWTUser springSecurityUser = (JWTUser) authentication.getPrincipal();
                return springSecurityUser.getAuthority() == 0;
            } else {
                return authentication.getAuthorities().stream()
                        .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(SystemPrivelege.ANONYMOUS));
            }
        }
        return false;
    }

    public static boolean isCurrentUserInRole(int authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                JWTUser springSecurityUser = (JWTUser) authentication.getPrincipal();
                return (springSecurityUser.getAuthority() & authority) > 0;
            } else {
                return authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority ->
                                grantedAuthority.getAuthority().equals(SystemPrivelege.getPrivilegeName(authority)));
            }
        }
        return false;
    }
}
