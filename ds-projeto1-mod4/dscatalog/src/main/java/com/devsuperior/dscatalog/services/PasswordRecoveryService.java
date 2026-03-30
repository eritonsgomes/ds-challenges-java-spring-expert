package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.PasswordRecoveryEntity;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.repositories.PasswordRecoveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final PasswordRecoveryRepository passwordRecoveryRepository;

    private final AuthTokenService authTokenService;

    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    public void sendPasswordRecoveryEmail(PasswordRecoveryEntity passwordRecovery) {
        String body = emailTemplateService.generatePasswordRecoveryTemplate(passwordRecovery.getToken());
        emailService.sendEmail(passwordRecovery.getEmail(), "Recuperação de Senha", body).join();
    }

    @Transactional
    public PasswordRecoveryEntity create(String email) {
        PasswordRecoveryEntity passwordRecoveryEntity = new PasswordRecoveryEntity();

        passwordRecoveryEntity.setEmail(email);
        passwordRecoveryEntity.setToken(authTokenService.generateRecoveryToken());
        passwordRecoveryEntity.setExpiration(
            Instant.now().plusSeconds(authTokenService.getTokenMinutes() * 60L)
        );

        return passwordRecoveryRepository.save(passwordRecoveryEntity);
    }

    @Transactional(readOnly = true)
    public List<PasswordRecoveryEntity> searchValidTokens(String token, Instant from) {
        List<PasswordRecoveryEntity> tokens = passwordRecoveryRepository.searchValidTokens(token, from);

        if (tokens.isEmpty()) {
            throw new ResourceNotFoundException("Token not found");
        }

        return tokens;
    }

}
