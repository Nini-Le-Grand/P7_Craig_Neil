package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Validators validators;

    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(user -> {
                                 UserDTO userDTO = new UserDTO();
                                 userDTO.setId(user.getId());
                                 userDTO.setFullname(user.getFullname());
                                 userDTO.setUsername(user.getUsername());
                                 userDTO.setRole(user.getRole());
                                 return userDTO;
                             })
                             .toList();
    }

    public User getUser(Integer id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                     "Le user avec l'id %d n'existe pas", id)));
    }

    public Integer getConnectedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof User) ? ((User) principal).getId() : null;
    }

    public UserDTO findUserToUpdate(Integer id) {
        User user = getUser(id);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullname(user.getFullname());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword("");
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public void addUser(UserDTO userDTO, BindingResult result) {
        if (validators.usernameExists(userDTO.getUsername())) {
            result.rejectValue("username", "error.username", "This username is already used");
        }

        if (userDTO.getPassword().isBlank()) {
            result.rejectValue("password", "error.password", "Password is mandatory");
        }

        if (validators.isPasswordInvalid(userDTO.getPassword())) {
            result.rejectValue("password", "error.password", "MDP : 8 char minimum : 1 MAJ, 1 min, 1 chiffre, 1 symbole");
        }

        if (result.hasErrors()) {
            return;
        }

        User user = new User();
        user.setFullname(userDTO.getFullname());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation du user");
        }
    }

    public void updateUser(Integer id, UserDTO userDTO, BindingResult result) {
        User user = getUser(id);

        if (!user.getUsername().equals(userDTO.getUsername())) {
            if (validators.usernameExists(userDTO.getUsername())) {
                result.rejectValue("username", "error.username", "This username is already used");
            }
        }

        if (!userDTO.getPassword().isBlank()) {
            if (validators.isPasswordInvalid(userDTO.getPassword())) {
                result.rejectValue("password", "error.password", "MDP : 8 char minimum : 1 MAJ, 1 min, 1 chiffre, 1 symbole");
            }
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            user.setPassword(user.getPassword());
        }

        if (result.hasErrors()) {
            return;
        }

        user.setFullname(userDTO.getFullname());
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du user");
        }
    }

    public void deleteUser(Integer id) {
        Integer currentUserId = getConnectedUserId();

        if (id.equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez pas supprimer votre propre user.");
        }

        User user = getUser(id);

        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du user");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
