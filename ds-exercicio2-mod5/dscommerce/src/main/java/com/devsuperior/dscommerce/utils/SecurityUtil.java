package com.devsuperior.dscommerce.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Objects;
import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String getUserName() {
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
