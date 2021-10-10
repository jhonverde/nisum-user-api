package cl.com.nisum.user.api.service;

import cl.com.nisum.user.api.properties.TokenJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableConfigurationProperties(TokenJwtProperties.class)
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenJwtProperties tokenJwtProperties;

    public boolean isTokenValid(TokenJwtProperties.TokenType tokenType, String token) {
        try {
            TokenJwtProperties.TokenProperties tokenProperties = tokenJwtProperties.getTypes().get(tokenType);
            Jwts.parser()
                    .setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            return true;
        } catch (Exception e) {
            log.warn("Error parse token. Token: {}, Excepcion: {}", token, e.getMessage(), e);
            return false;
        }
    }

    public Claims getTokenBody(TokenJwtProperties.TokenType tokenType, String token) {
        TokenJwtProperties.TokenProperties tokenProperties = tokenJwtProperties.getTypes().get(tokenType);
        return Jwts.parser()
                .setSigningKey(tokenProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateTokenByUserType(String subject) {
        return generateTokenByType(TokenJwtProperties.TokenType.USER, subject);
    }

    public String generateTokenBySecurityType(String subject) {
        return generateTokenByType(TokenJwtProperties.TokenType.SECURITY, subject);
    }

    private String generateTokenByType(TokenJwtProperties.TokenType tokenType, String subject) {
        TokenJwtProperties.TokenProperties tokenProperties = tokenJwtProperties.getTypes().get(tokenType);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(tokenJwtProperties.getIssuer())
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getExpiredTime()))
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getSecret())
                .compact();
    }
}
