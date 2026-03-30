package com.devsuperior.dscatalog.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;


@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    @Value("${email.password-recovery.token.minutes}")
    private Integer tokenMinutes;

    @Value("${email.password-recovery.uri}")
    private URI passwordRecoveryUri;

    private String getPasswordRecoveryTemplate() {
        String template = """
            DSCatalog\n
            Recuperação de Senha\n
            Acesse o link abaixo para redefinir a senha:\n
            %s&token=%s\n
            Essa solicitação de recuperação de senha expira em %s minutos.
        """;

        return template;
    }

    public String generatePasswordRecoveryTemplate(String token) {
        String template = getPasswordRecoveryTemplate();

        template = String.format(template, passwordRecoveryUri, token, tokenMinutes);

        return template;
    }

}
