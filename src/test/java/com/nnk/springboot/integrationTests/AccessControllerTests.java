package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.RegisterDTO;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AccessControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testGetRegistrationPage() throws Exception {
        mockMvc.perform(get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("access/register"));
    }

    @Test
    void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("access/login"));
    }

    @Test
    void testRegister_Success() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFullname("Fullname");
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("Password12*");
        registerDTO.setConfirmPassword("Password12*");

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                         .with(csrf())
                                         .param("fullname", registerDTO.getFullname())
                                         .param("username", registerDTO.getUsername())
                                         .param("password", registerDTO.getPassword())
                                         .param("confirmPassword", registerDTO.getConfirmPassword()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"));

        Optional<User> user = userRepository.findByUsername("testuser");
        assert user.isPresent();
        assert user.get()
                   .getUsername()
                   .equals("testuser");
        assert passwordEncoder.matches("Password12*", user.get()
                                                          .getPassword());
    }

    @Test
    void testLogin_Success() throws Exception {
        User user = new User();
        user.setFullname("Fullname");
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("Password12*"));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        mockMvc.perform(post("/login").with(csrf())
                                      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                      .param("username", "testuser")
                                      .param("password", "Password12*"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void testRegister_Failure() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFullname("Fullanme");
        registerDTO.setUsername("");
        registerDTO.setPassword("pass");
        registerDTO.setConfirmPassword("different");

        mockMvc.perform(post("/register").with(csrf())
                                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                         .param("fullname", registerDTO.getFullname())
                                         .param("username", registerDTO.getUsername())
                                         .param("password", registerDTO.getPassword())
                                         .param("confirmPassword", registerDTO.getConfirmPassword()))
               .andExpect(status().isOk())
               .andExpect(view().name("/access/register"))
               .andExpect(model().attributeHasFieldErrors("registerDTO", "username", "password", "confirmPassword"));
    }

    @Test
    void testLogin_Failure() throws Exception {
        mockMvc.perform(post("/login").with(csrf())
                                      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                      .param("username", "wrongUsername")
                                      .param("password", "password"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    void testRedirection() throws Exception {
        mockMvc.perform(get("/home").with(csrf())
                                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void testNoUrlRedirection() throws Exception {
        mockMvc.perform(get("/").with(user("testuser").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
    }

    @Test
    void testLogout() throws Exception {
        User user = new User();
        user.setFullname("Fullname");
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("Password12*"));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        mockMvc.perform(post("/login").with(csrf())
                                      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                      .param("username", "testuser")
                                      .param("password", "Password12*"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bidList/list"));

        mockMvc.perform(post("/app-logout").with(csrf())
                                           .with(user("testuser").roles("USER"))
                                           .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void testAdminRoute_Success() throws Exception {
        mockMvc.perform(get("/admin/user/list").with(user("testuser").roles("ADMIN")))
               .andExpect(status().isOk())
               .andExpect(view().name("user/list"));
    }
}
