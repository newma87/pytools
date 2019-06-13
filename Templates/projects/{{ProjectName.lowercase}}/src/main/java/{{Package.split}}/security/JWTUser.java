package {{Package}}.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 12:07:16
 * Description:
 */
public class JWTUser implements UserDetails {

    private String loginName;
    private String password;
    private Integer authority;      // 用户的系统权限值

    public JWTUser(String loginName, String password, Integer authority) {
        this.loginName = loginName;
        this.password = password;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return SystemPrivelege.getPrivileges(this.authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginName;
    }

    public Integer getAuthority() {
        return this.getAuthority();
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
