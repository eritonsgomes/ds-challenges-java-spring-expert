package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dtos.requests.PasswordRecoveryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.PasswordResetRequestDTO;
import com.devsuperior.dscatalog.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/password/recovery")
    public ResponseEntity<Void> recoverToken(@Valid @RequestBody PasswordRecoveryRequestDTO request) {
        authService.recoverPassword(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/password/reset")
    public ResponseEntity<Void> recoverToken(@Valid @RequestBody PasswordResetRequestDTO request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

}
