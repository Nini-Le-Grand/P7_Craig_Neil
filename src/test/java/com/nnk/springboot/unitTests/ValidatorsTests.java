package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.Validators;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidatorsTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private Validators validators;

    @Test
    void testUsernameExists_True() {
        String username = "existingUser";
        User user = new User();
        user.setUsername("existingUser");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = validators.usernameExists(username);

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testUsernameExists_WhenUserDoesNotExist_ShouldReturnFalse() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = validators.usernameExists(username);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testIsPasswordInvalid_WhenTooShort_ShouldReturnTrue() {
        String password = "Ab1!";
        assertTrue(validators.isPasswordInvalid(password));
    }

    @Test
    void testIsPasswordInvalid_WhenNoUpperCase_ShouldReturnTrue() {
        String password = "password1!";
        assertTrue(validators.isPasswordInvalid(password));
    }

    @Test
    void testIsPasswordInvalid_WhenNoLowerCase_ShouldReturnTrue() {
        String password = "PASSWORD1!";
        assertTrue(validators.isPasswordInvalid(password));
    }

    @Test
    void testIsPasswordInvalid_WhenNoDigit_ShouldReturnTrue() {
        String password = "Password!";
        assertTrue(validators.isPasswordInvalid(password));
    }

    @Test
    void testIsPasswordInvalid_WhenNoSpecialCharacter_ShouldReturnTrue() {
        String password = "Password1";
        assertTrue(validators.isPasswordInvalid(password));
    }

    @Test
    void testIsPasswordInvalid_WhenValidPassword_ShouldReturnFalse() {
        String password = "Valid08!";
        assertFalse(validators.isPasswordInvalid(password));
    }

    @Test
    void testPasswordMatches_WhenPasswordsAreEqual_ShouldReturnTrue() {
        assertTrue(validators.passwordMatches("SecurePass123!", "SecurePass123!"));
    }

    @Test
    void testPasswordMatches_WhenPasswordsAreDifferent_ShouldReturnFalse() {
        assertFalse(validators.passwordMatches("SecurePass123!", "WrongPass456!"));
    }
}
