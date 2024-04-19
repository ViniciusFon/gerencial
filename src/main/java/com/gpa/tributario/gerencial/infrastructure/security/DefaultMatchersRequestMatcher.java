package com.gpa.tributario.gerencial.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

@Getter
@Setter
@AllArgsConstructor
public class DefaultMatchersRequestMatcher implements RequestMatcher {

    private String[] defaultMatchers;

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        for (String pattern : defaultMatchers) {
            if (new AntPathMatcher().match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
