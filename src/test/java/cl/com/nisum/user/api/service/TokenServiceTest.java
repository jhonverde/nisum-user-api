package cl.com.nisum.user.api.service;

import cl.com.nisum.user.api.properties.TokenJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:application.properties"})
@Import(TokenService.class)
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenJwtProperties tokenJwtProperties;

    @Test
    public void givenSubject_whenGenerateTokenByUserType_thenReturnToken() {
        String subjectExpected = "jhon.verde@test.cl";
        String tokenResponse = tokenService.generateTokenByUserType("jhon.verde@test.cl");
        TokenJwtProperties.TokenProperties tokenProperties = tokenJwtProperties.getTypes().get(TokenJwtProperties.TokenType.USER);
        String subjectResponse = getTokenSubject(tokenResponse, tokenProperties);

        assertThat(subjectResponse).isEqualTo(subjectExpected);
    }

    @Test
    public void givenSubject_whenGenerateTokenBySecurityType_thenReturnToken() {
        String subjectExpected = "jhon.verde@test.cl";
        String tokenResponse = tokenService.generateTokenBySecurityType("jhon.verde@test.cl");
        TokenJwtProperties.TokenProperties tokenProperties = tokenJwtProperties.getTypes().get(TokenJwtProperties.TokenType.SECURITY);
        String subjectResponse = getTokenSubject(tokenResponse, tokenProperties);

        assertThat(subjectResponse).isEqualTo(subjectExpected);
    }

    @Test
    public void givenTokenValid_whenIsTokenValid_thenReturnTrue() {
        String tokenValid = tokenService.generateTokenBySecurityType("jhon.verde@test.cl");
        boolean isTokenValid = tokenService.isTokenValid(TokenJwtProperties.TokenType.SECURITY, tokenValid);

        assertThat(isTokenValid).isEqualTo(true);
    }

    @Test
    public void givenTokenInValid_whenIsTokenValid_thenReturnFalse() {
        String tokenValid = "eeee.jjj.ccc";
        boolean isTokenValid = tokenService.isTokenValid(TokenJwtProperties.TokenType.SECURITY, tokenValid);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void givenTokenValid_whenGetToken_thenReturnTokenBody() {
        String subjectExpected = "jhon.verde@test.cl";
        String tokenValid = tokenService.generateTokenBySecurityType(subjectExpected);
        Claims claims = tokenService.getTokenBody(TokenJwtProperties.TokenType.SECURITY, tokenValid);
        String subjectResponse = claims.getSubject();

        assertThat(subjectResponse).isEqualTo(subjectExpected);
    }

    private String getTokenSubject(String tokenResponse, TokenJwtProperties.TokenProperties tokenProperties) {
        return Jwts
                .parser()
                .setSigningKey(tokenProperties.getSecret())
                .parseClaimsJws(tokenResponse)
                .getBody().getSubject();
    }
}
