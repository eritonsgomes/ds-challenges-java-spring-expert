package com.devsuperior.dscatalog.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    @Getter
    @Value("${email.password-recovery.token.minutes}")
    private Integer tokenMinutes;

    public String generateRecoveryToken() {
        return UUID.randomUUID().toString();
    }

}
