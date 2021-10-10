package cl.com.nisum.user.api.resource;

import cl.com.nisum.user.api.model.dto.TokenResponse;
import cl.com.nisum.user.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping
    public TokenResponse getToken() {
        return new TokenResponse(tokenService.generateTokenBySecurityType("test@test.cl"));
    }
}
