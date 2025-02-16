package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Service class for managing user accounts.
 */
@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Validators validators;


    /**
     * Retrieves all user accounts.
     *
     * @return a list of {@link UserDTO} containing user account information.
     */
    public List<UserDTO> getUsers() {
        logger.info("Fetching all users.");

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

    /**
     * Retrieves a specific user account by its ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the {@link User} corresponding to the given ID.
     * @throws ResponseStatusException if the user is not found.
     */
    public User getUser(Integer id) {
        logger.info("Fetching user with ID: {}", id);

        return userRepository.findById(id)
                             .orElseThrow(() -> {
                                 logger.warn("User with ID {} not found.", id);
                                 return new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                         "Le user avec l'id %d n'existe pas", id));
                             });
    }

    /**
     * Gets the ID of the currently connected user.
     *
     * @return the ID of the connected user, or null if not authenticated.
     */
    public Integer getConnectedUserId() {
        logger.info("Fetching connected user ID");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof User) ? ((User) principal).getId() : null;
    }

    /**
     * Prepares a user account for updating.
     *
     * @param id the ID of the user to update.
     * @return the {@link UserDTO} containing user details for updating.
     * @throws ResponseStatusException if the user is not found.
     */
    public UserDTO findUserToUpdate(Integer id) {
        logger.info("Preparing to update user with ID: {}", id);

        User user = getUser(id);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullname(user.getFullname());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword("");
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    /**
     * Adds a new user account.
     *
     * @param userDTO the {@link UserDTO} containing details of the user to add.
     * @param result the binding result to capture validation errors.
     * @throws ResponseStatusException if an error occurs during the creation of the user.
     */
    public void addUser(UserDTO userDTO, BindingResult result) {
        logger.info("Adding new user: {}", userDTO.getUsername());

        if (validators.usernameExists(userDTO.getUsername())) {
            result.rejectValue("username", "error.username", "This username is already used");
        }

        if (userDTO.getPassword().isBlank()) {
            logger.info("Checking if password is blank");
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
            logger.info("New user '{}' created successfully.", user.getUsername());
        } catch (Exception e) {
            logger.error("Error creating user '{}': {}", user.getUsername(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation du user");
        }
    }

    /**
     * Updates an existing user account.
     *
     * @param id the ID of the user to update.
     * @param userDTO the {@link UserDTO} containing updated details of the user.
     * @param result the binding result to capture validation errors.
     * @throws ResponseStatusException if an error occurs during the update of the user.
     */
    public void updateUser(Integer id, UserDTO userDTO, BindingResult result) {
        logger.info("Updating user with ID: {}", id);

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
            logger.info("User '{}' updated successfully.", user.getUsername());
        } catch (Exception e) {
            logger.error("Error updating user '{}': {}", user.getUsername(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du user");
        }
    }

    /**
     * Deletes a user account.
     *
     * @param id the ID of the user to delete.
     * @throws ResponseStatusException if an error occurs during the deletion of the user.
     */
    public void deleteUser(Integer id) {
        logger.info("Deleting user with ID: {}", id);

        Integer currentUserId = getConnectedUserId();

        if (id.equals(currentUserId)) {
            logger.warn("User {} attempted to delete their own account.", currentUserId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez pas supprimer votre propre user.");
        }

        User user = getUser(id);

        try {
            userRepository.delete(user);
            logger.info("User deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting user with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du user");
        }
    }

    /**
     * Loads a user by username for authentication.
     *
     * @param username the username of the user to load.
     * @return the {@link UserDetails} object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Starting authentication process for user '{}'.", username);

        return userRepository.findByUsername(username)
                             .orElseThrow(() -> {
                                 logger.warn("Authentication failed: User '{}' not found.", username);
                                 return new UsernameNotFoundException("User not found");
                             });
    }
}
