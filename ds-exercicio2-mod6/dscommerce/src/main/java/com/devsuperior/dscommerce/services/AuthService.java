package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUtil tokenUtil;

    public String getAuthenticatedUserName() {
        return tokenUtil.getUserNameFromToken();
    }

    public void validateSelfOrAdmin(Long userId) {
        String userName = tokenUtil.getUserNameFromToken();

        User me = userRepository.findByEmail(userName).orElseThrow(
            () -> new UsernameNotFoundException("Username not found")
        );

        if (me.hasRole("ROLE_ADMIN")) {
            return;
        }

        if(!me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied. Should be self or admin");
        }
    }

}
