package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDTO;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.services.Validators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private Validators validators;
    @Mock
    private BindingResult result;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    private User user1;
    private User user2;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1);
        user1.setUsername("test1");
        user1.setPassword("HashedPassword");

        user2 = new User();
        user2.setId(2);
        user2.setUsername("test2");

        userDTO = new UserDTO();
        userDTO.setUsername("test3");
        userDTO.setPassword("Password12*");
        userDTO.setRole("ROLE_ADMIN");
    }

    @Test
    void testGetUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = userService.getUsers();

        assertEquals(2, result.size());
        assertEquals("test1", result.get(0)
                                    .getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUser_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        User result = userService.getUser(1);

        assertNotNull(result);
        assertEquals("test1", result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUser_Failure() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> userService.findUserToUpdate(1));

        assertEquals("404 NOT_FOUND \"Le user avec l'id 1 n'existe pas\"", exception.getMessage());
    }

    @Test
    void testGetConnectedUserId() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(user1);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Integer result = userService.getConnectedUserId();
        assertEquals(user1.getId(), result);
    }

    @Test
    void testFindUserToUpdate_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        UserDTO result = userService.findUserToUpdate(1);

        assertNotNull(result);
        assertEquals("test1", result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testAddUser_Success() {
        when(validators.usernameExists(userDTO.getUsername())).thenReturn(false);
        when(validators.isPasswordInvalid(userDTO.getPassword())).thenReturn(false);
        when(result.hasErrors()).thenReturn(false);

        userService.addUser(userDTO, result);

        verify(userRepository, times(1)).save(argThat(user -> "test3".equals(user.getUsername())));
    }

    @Test
    void testAddUser_Failure_UsernameExists() {
        when(validators.usernameExists(userDTO.getUsername())).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);

        userService.addUser(userDTO, result);

        verify(result).rejectValue("username", "error.username", "This username is already used");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAddUser_Failure_BlankPassword() {
        userDTO.setPassword("");
        when(validators.usernameExists(userDTO.getUsername())).thenReturn(false);
        when(result.hasErrors()).thenReturn(true);

        userService.addUser(userDTO, result);

        verify(result).rejectValue("password", "error.password", "Password is mandatory");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAddUser_Failure_InvalidPassword() {
        userDTO.setPassword("invalid");
        when(validators.usernameExists(userDTO.getUsername())).thenReturn(false);
        when(validators.isPasswordInvalid(userDTO.getPassword())).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);

        userService.addUser(userDTO, result);

        verify(result).rejectValue("password", "error.password",
                                   "MDP : 8 char minimum : 1 MAJ, 1 min, 1 chiffre, 1 symbole");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAddUser_Failure_ExceptionDuringSave() {
        when(result.hasErrors()).thenReturn(false);
        when(validators.usernameExists(anyString())).thenReturn(false);
        when(validators.isPasswordInvalid(anyString())).thenReturn(false);

        doThrow(new RuntimeException("Database error")).when(userRepository)
                                                       .save(any(User.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.addUser(userDTO, result);
        });

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la creation du user\"",
                     exception.getMessage());
    }

    @Test
    void testUpdateUser_Success_UpdateUsername() {
        userDTO.setPassword("");
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(validators.usernameExists(anyString())).thenReturn(false);
        when(result.hasErrors()).thenReturn(false);

        userService.updateUser(1, userDTO, result);

        verify(userRepository, times(1)).save(argThat(user -> "test3".equals(user.getUsername())));
    }

    @Test
    void testUpdateUser_Failure_UpdateUsername() {
        userDTO.setPassword("");
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(validators.usernameExists(anyString())).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);

        userService.updateUser(1, userDTO, result);

        verify(result).rejectValue("username", "error.username", "This username is already used");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_Success_UpdatePassword() {
        userDTO.setUsername("test1");
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(validators.isPasswordInvalid(anyString())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedNewPassword");
        when(result.hasErrors()).thenReturn(false);

        userService.updateUser(1, userDTO, result);

        verify(userRepository, times(1)).save(argThat(user -> "encodedNewPassword".equals(user.getPassword())));
    }

    @Test
    void testUpdateUser_Failure_UpdatePassword() {
        userDTO.setUsername("test1");
        userDTO.setPassword("invalid");
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(validators.isPasswordInvalid(anyString())).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);

        userService.updateUser(1, userDTO, result);

        verify(result).rejectValue("password", "error.password",
                                   "MDP : 8 char minimum : 1 MAJ, 1 min, 1 chiffre, 1 symbole");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_Failure_ExceptionDuringSave() {
        when(result.hasErrors()).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(validators.usernameExists(anyString())).thenReturn(false);
        when(validators.isPasswordInvalid(anyString())).thenReturn(false);

        doThrow(new RuntimeException("Database error")).when(userRepository)
                                                       .save(any(User.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.updateUser(1, userDTO, result);
        });

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la modification du user\"",
                     exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(
                SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext)
                                       .thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(user1);
            when(userService.getConnectedUserId()).thenReturn(user1.getId());

            when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

            userService.deleteUser(user2.getId());

            verify(userRepository, times(1)).delete(user2);
        }
    }

    @Test
    void testDeleteUser_Failure_SelfDeletion() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(user1);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.deleteUser(user1.getId());
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Vous ne pouvez pas supprimer votre propre user.", exception.getReason());

        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testDeleteUser_ExceptionDuringDelete() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(
                SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext)
                                       .thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(user1);
            when(userService.getConnectedUserId()).thenReturn(user1.getId());

            when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
            doThrow(new RuntimeException("DB error")).when(userRepository)
                                                     .delete(user2);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.deleteUser(user2.getId());
            });

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
            assertEquals("Une erreur est survenue lors de la suppression du user", exception.getReason());
        }
    }

    @Test
    void testLoadUserByUsername_Success() {
        String username = "john_doe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user1));

        UserDetails result = userService.loadUserByUsername(username);

        assertEquals(user1.getUsername(), result.getUsername());
        assertEquals(user1.getPassword(), result.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "non_existent_user";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });

        assertEquals("User not found", exception.getMessage());
    }
}

