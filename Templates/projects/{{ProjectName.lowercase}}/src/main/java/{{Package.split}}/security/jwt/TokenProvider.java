package {{Package}}.security.jwt;

import java.util.*;
import javax.annotation.PostConstruct;

import {{Package}}.config.property.SecurityProperty;
import {{Package}}.security.JWTUser;
import {{Package}}.security.SystemPrivelege;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final SecurityProperty securityProperty;

    @Autowired
    public TokenProvider(SecurityProperty securityProperty) {
        this.securityProperty = securityProperty;
    }

    @PostConstruct
    public void init() {
        this.secretKey =
                securityProperty.getJwt().getSecret();

        this.tokenValidityInMilliseconds =
            1000 * securityProperty.getJwt().getTokenValidateInSecond();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * securityProperty.getJwt().getTokenValidateInSecondForRememberMe();
    }

    public String createToken(String loginName, Integer authority, Boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(loginName)
            .claim(AUTHORITIES_KEY, authority.toString())
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        return createToken(authentication.getName(), SystemPrivelege.getRevertPrivileges(authentication.getAuthorities()), rememberMe);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        Integer authorities = Integer.valueOf(claims.get(AUTHORITIES_KEY).toString());
        JWTUser principal = new JWTUser(claims.getSubject(), "",  authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, SystemPrivelege.getPrivileges(authorities));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
