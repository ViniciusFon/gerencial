package com.gpa.tributario.gerencial.infrastructure.security;

import com.gpa.tributario.gerencial.infrastructure.exception.UnauthorizedException;
import com.gpa.tributario.gerencial.repository.UsuarioRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService; // Service que definimos anteriormente

    @Autowired
    private UsuarioRepositorio userRepository; // Repository que definimos anteriormente

    @Value("${spring.security.default-matchers}")
    private String[] defaultMatchers;

    @Value("${spring.security.time-new-token}")
    private Integer TIME_NEW_TOKEN;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Verifica se o endpoint requer autenticação antes de processar a requisição
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
            if (token != null) {

                UserDetailsImpl userDetails = jwtTokenService.getDataFromToken(token);

                // Cria um objeto de autenticação do Spring Security
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Define o objeto de autenticação no contexto de segurança do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.setHeader("Authorization", "Bearer " + token);
                if(userDetails.getTimeToExpire() <= TIME_NEW_TOKEN){
                    response.setHeader("Authorization", "Bearer " + jwtTokenService.generateToken(userDetails));
                }

            } else {
                throw new UnauthorizedException("O token está ausente.");
            }
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        return !new DefaultMatchersRequestMatcher(defaultMatchers).matches(request);
    }

}

