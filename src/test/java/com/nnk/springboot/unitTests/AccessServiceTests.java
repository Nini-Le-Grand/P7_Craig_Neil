package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.RegisterDTO;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.AccessService;
import com.nnk.springboot.services.Validators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private Validators validators;
    @Mock
    private BindingResult result;
    @InjectMocks
    private AccessService accessService;
    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setFullname("testFullname");
        registerDTO.setUsername("testUser");
        registerDTO.setPassword("Password1**");
        registerDTO.setConfirmPassword("Password1**");
    }

    @Test
    void testRegister_Success() {
        when(validators.usernameExists(registerDTO.getUsername())).thenReturn(false);
        when(validators.isPasswordInvalid(registerDTO.getPassword())).thenReturn(true);
        when(validators.passwordMatches(registerDTO.getPassword(), registerDTO.getConfirmPassword())).thenReturn(true);
        when(result.hasErrors()).thenReturn(false);

        accessService.register(registerDTO, result);

        verify(userRepository, times(1)).save(argThat(user -> "testUser".equals(user.getUsername())));
    }

    @Test
    void testRegister_Failure_UsernameAlreadyExists() {
        when(validators.usernameExists(registerDTO.getUsername())).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);

        accessService.register(registerDTO, result);

        verify(result, times(1)).rejectValue("username", "error.username", "This username is already used");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_Failure_IncorrectPasswordPattern() {
        registerDTO.setPassword("invalid");

        when(validators.usernameExists(anyString())).thenReturn(false);
        when(validators.isPasswordInvalid(anyString())).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);

        accessService.register(registerDTO, result);


        verify(result).rejectValue("password", "error.password",
                                   "MDP : 8 char minimum : 1 MAJ, 1 min, 1 chiffre, 1 symbole");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_Failure_PasswordsDoNotMatch() {
        registerDTO.setPassword("Password2**");

        when(validators.usernameExists(anyString())).thenReturn(false);
        when(validators.isPasswordInvalid(anyString())).thenReturn(false);
        when(validators.passwordMatches(anyString(), anyString())).thenReturn(false);
        when(result.hasErrors()).thenReturn(true);

        accessService.register(registerDTO, result);


        verify(result).rejectValue("password", "error.password", "Passwords do not match");
        verify(result).rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_Failure_FormHasErrors() {
        when(result.hasErrors()).thenReturn(true);

        accessService.register(registerDTO, result);

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testRegister_Failure_ExceptionDuringSave() {
        when(result.hasErrors()).thenReturn(false);
        when(validators.usernameExists(anyString())).thenReturn(false);
        when(validators.isPasswordInvalid(anyString())).thenReturn(false);
        when(validators.passwordMatches(anyString(), anyString())).thenReturn(true);

        doThrow(new RuntimeException("Database error")).when(userRepository).save(any(User.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accessService.register(registerDTO, result);
        });

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la création de l'utilisateur\"", exception.getMessage());
    }
}