package {{Package}}.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 12:07:16
 * Description:
 */
public class JWTUser implements UserDetails {

    private String loginName;
    private String password;
    private Optional<String[]> authorities;      // 用户的系统权限值

    public JWTUser(String loginName, String password, String[] authorities) {
        this.loginName = loginName;
        this.password = password;
        this.authorities = Optional.of(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> result = new ArrayList<>();
        if (authorities.isPresent()) {
            Arrays.asList(authorities.get()).stream()
                    .forEach((authority) -> result.add(new SimpleGrantedAuthority(authority)));
        }
        return result;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginName;
    }

    public String[] getRawAuthorities() {
        return this.authorities.get();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
