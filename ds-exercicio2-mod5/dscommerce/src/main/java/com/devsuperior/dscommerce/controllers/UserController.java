package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface UserController {

    @GetMapping(value = "/me")
    ResponseEntity<UserDTO> getLoggedUser();

}
