package {{Package}}.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-04 9:38:50
 * Description:
 */
@ConfigurationProperties(prefix = "default.security")
public class SecurityProperty {
    private JWT jwt;

    public JWT getJwt() {
        return jwt;
    }

    public void setJwt(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "SecurityConfig{" +
                "jwt=" + jwt +
                '}';
    }

    public static class JWT {
        private String secret;

        private Long tokenValidateInSecond;

        private Long tokenValidateInSecondForRememberMe;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Long getTokenValidateInSecond() {
            return tokenValidateInSecond;
        }

        public void setTokenValidateInSecond(Long tokenValidateInSecond) {
            this.tokenValidateInSecond = tokenValidateInSecond;
        }

        public Long getTokenValidateInSecondForRememberMe() {
            return tokenValidateInSecondForRememberMe;
        }

        public void setTokenValidateInSecondForRememberMe(Long tokenValidateInSecondForRememberMe) {
            this.tokenValidateInSecondForRememberMe = tokenValidateInSecondForRememberMe;
        }

        @Override
        public String toString() {
            return "JWT{" +
                    "secret='" + secret + '\'' +
                    ", tokenValidateInSecond=" + tokenValidateInSecond +
                    ", tokenValidateInSecondForWeek=" + tokenValidateInSecondForRememberMe +
                    '}';
        }
    }
}
