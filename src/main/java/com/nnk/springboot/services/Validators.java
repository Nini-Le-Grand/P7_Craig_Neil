package com.nnk.springboot.services;

import com.nnk.springboot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for validating user input, specifically usernames and passwords.
 */
@Service
public class Validators {
    private static final Logger logger = LoggerFactory.getLogger(Validators.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Checks if a username already exists in the repository.
     *
     * @param username the username to check.
     * @return true if the username exists, false otherwise.
     */
    public boolean usernameExists(String username) {
        logger.info("Checking if username {} exists in database ", username);

        return userRepository.findByUsername(username)
                             .isPresent();
    }

    /**
     * Validates the complexity of a password based on specific criteria.
     *
     * <p>
     * The password must meet the following requirements:
     * <ul>
     *   <li>At least 8 characters long.</li>
     *   <li>At least one uppercase letter.</li>
     *   <li>At least one lowercase letter.</li>
     *   <li>At least one digit.</li>
     *   <li>At least one special character (e.g., !@#$%^&*).</li>
     * </ul>
     * </p>
     *
     * @param password the password to validate.
     * @return true if the password is invalid, false if it meets the criteria.
     */
    public boolean isPasswordInvalid(String password) {
        logger.info("Checking if password is valid");

        return password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(
                ".*[a-z].*") || !password.matches(".*\\d.*") || !password.matches(
                ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }

    /**
     * Compares a password with its confirmation to check if they match.
     *
     * @param password        the original password.
     * @param confirmPassword the confirmation password.
     * @return true if the passwords match, false otherwise.
     */
    public boolean passwordMatches(String password, String confirmPassword) {
        logger.info("Checking if passwords match");

        return password.equals(confirmPassword);
    }
}
