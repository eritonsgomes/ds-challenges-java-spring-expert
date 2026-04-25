package com.devsuperior.dscommerce.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class TokenUtil {

    public TokenUtil() {
    }

    public String getUserNameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.equals("anonymousUser", authentication.getPrincipal())) {
            throw new JwtException("Token not found");
        }

        Jwt token= (Jwt) authentication.getPrincipal();

        Optional<Object> user = Optional.ofNullable(token.getClaims().get("user"));

        try {
            return (String) user.orElseThrow();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username not found");
        }
    }

}
