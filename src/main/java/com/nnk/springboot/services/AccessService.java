package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.RegisterDTO;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccessService {
    @Autowired
    private Validators validators;
    @Autowired
    private UserRepository userRepository;

    public void register(RegisterDTO registerDTO, BindingResult result) {
        if (validators.usernameExists(registerDTO.getUsername())) {
            result.rejectValue("username", "error.username", "This username is already used");
        }

        if (validators.isPasswordInvalid(registerDTO.getPassword())) {
            result.rejectValue("password", "error.password",
                               "MDP : 8 char minimum : 1 MAJ, 1 min, 1 chiffre, 1 symbole");
        }

        if (!validators.passwordMatches(registerDTO.getPassword(), registerDTO.getConfirmPassword())) {
            result.rejectValue("password", "error.password", "Passwords do not match");
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        if (result.hasErrors()) {
            return;
        }

        BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
        User newUser = new User();
        newUser.setFullname(registerDTO.getFullname());
        newUser.setUsername(registerDTO.getUsername());
        newUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
        newUser.setRole("ROLE_USER");

        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la création de l'utilisateur");
        }
    }
}
