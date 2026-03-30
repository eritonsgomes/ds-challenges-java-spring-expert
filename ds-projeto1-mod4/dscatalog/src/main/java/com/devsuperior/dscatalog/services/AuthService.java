package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.PasswordRecoveryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.PasswordResetRequestDTO;
import com.devsuperior.dscatalog.entities.PasswordRecoveryEntity;
import com.devsuperior.dscatalog.entities.RoleEntity;
import com.devsuperior.dscatalog.entities.UserEntity;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import com.devsuperior.dscatalog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordRecoveryService passwordRecoveryService;
    private final PasswordEncoder passwordEncoder;

    protected UserEntity getUserAuthenticated() throws UsernameNotFoundException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (Objects.nonNull(authentication)) {
                Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();

                var username = "";

                if (Objects.nonNull(jwtPrincipal)) {
                    var hasUsernameClaim = Objects.nonNull(jwtPrincipal.getClaim("username"));
                    username = hasUsernameClaim ? jwtPrincipal.getClaim("username") : "";
                }

                List<UserDetailsProjection> userProjections = userRepository.searchUserAndRolesByEmail(username);

                if (userProjections.isEmpty()) {
                    throw new UsernameNotFoundException("Email not found");
                }

                UserEntity user = new UserEntity();

                user.setId(userProjections.getFirst().getUserId());
                user.setFirstName(userProjections.getFirst().getFirstName());
                user.setLastName(userProjections.getLast().getLastName());
                user.setPhone(userProjections.getFirst().getPhone());
                user.setBirthDate(userProjections.getFirst().getBirthDate());
                user.setEmail(userProjections.getFirst().getUsername());
                user.setPassword(userProjections.getFirst().getPassword());

                for (UserDetailsProjection projection : userProjections) {
                    user.addRole(new RoleEntity(projection.getRoleId(), projection.getAuthority()));
                }

                return user;
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }

        return null;
    }

    @Transactional
    public void recoverPassword(PasswordRecoveryRequestDTO request) {
        userRepository.findByEmail(request.email()).orElseThrow(
            () -> new UsernameNotFoundException("E-mail não encontrado")
        );

        PasswordRecoveryEntity passwordRecovery = passwordRecoveryService.create(request.email());

        passwordRecoveryService.sendPasswordRecoveryEmail(passwordRecovery);
    }

    @Transactional
    public void resetPassword(PasswordResetRequestDTO request) {
        List<PasswordRecoveryEntity> tokens = passwordRecoveryService.searchValidTokens(request.token(), Instant.now());

        UserEntity user = userRepository
            .findByEmail(tokens.getFirst().getEmail())
            .orElseThrow(
                () -> new ResourceNotFoundException("Token inválido")
            );

        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

}
