package cl.com.nisum.user.api.security;

import cl.com.nisum.user.api.properties.TokenJwtProperties;
import cl.com.nisum.user.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    private final Environment environment;

    private final TokenService tokenService;

    private final List<String> profilesDev = Arrays.asList("local", "dev");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        String requestURI = request.getRequestURI();
        String token;
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        boolean isProfileDev = activeProfiles.stream().anyMatch(profile -> profilesDev.contains(profile));

        if(isProfileDev){
            addUserToContextSecurity(true, "TOKEN");
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(header) || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = header.replace(BEARER_PREFIX, "");

        if(!tokenService.isTokenValid(TokenJwtProperties.TokenType.SECURITY, token)){
            filterChain.doFilter(request, response);
            return;
        }

        addUserToContextSecurity(false, token);
        filterChain.doFilter(request, response);
    }

    private void addUserToContextSecurity(boolean isProfileDev, String token) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        String subject = isProfileDev ? "USER_DEV" : tokenService.getTokenBody(TokenJwtProperties.TokenType.SECURITY, token).getSubject();
        User defaultUserDetails = new User(subject, "default", grantedAuthorityList);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(defaultUserDetails, null,
                defaultUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
