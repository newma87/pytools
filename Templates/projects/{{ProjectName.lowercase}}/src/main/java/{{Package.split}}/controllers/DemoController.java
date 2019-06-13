package {{Package}}.controllers;

import {{Package}}.common.RestRespDTO;
import {{Package}}.security.jwt.TokenProvider;
import {{Package}}.util.RestRespUtil;
import {{Package}}.util.SecurityUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 14:43:59
 * Description:
 */
@RestController
@Api(description = "DEMO API")
@RequestMapping("/apiv1")
public class DemoController {
    public static class LoginDTO implements Serializable {
        private static final Long serialVersionUID = 1L;

        private String user;
        private String password;
        private Boolean isRemember;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Boolean getRemember() {
            return isRemember;
        }

        public void setRemember(Boolean remember) {
            isRemember = remember;
        }

        @Override
        public String toString() {
            return "LoginDTO{" +
                    "user='" + user + '\'' +
                    ", password='" + password + '\'' +
                    ", isRemember=" + isRemember +
                    '}';
        }
    }

    private static Logger log = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private AuthenticationManager author;

    @Autowired
    private TokenProvider provider;

    @Autowired
    private PasswordEncoder encoder;


    @GetMapping("/hello")
    public RestRespDTO hello(String user) {
        String name = SecurityUtil.getCurrentUserLogin();
        return RestRespUtil.getSuccessRsp(String.format("hello, %s", name));
    }

    @PostMapping("/login")
    public RestRespDTO getToken(@RequestBody LoginDTO login) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUser(), login.getPassword());
        try {
            Authentication author = this.author.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(author);
            String token = this.provider.createToken(author, true);
            return RestRespUtil.getSuccessRsp(token);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RestRespUtil.getOtherErrorRsp("login failed");
    }
}
