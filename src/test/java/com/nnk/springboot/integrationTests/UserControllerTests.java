package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setFullname("Fullname1");
        user.setUsername("Username1");
        user.setPassword("HashedPassword1");
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/admin/user/list").with(user("testuser").roles("ADMIN")))
               .andExpect(status().isOk())
               .andExpect(view().name("user/list"));
    }

    @Test
    void testGetAddUser() throws Exception {
        mockMvc.perform(get("/admin/user/add").with(user("testuser").roles("ADMIN")))
               .andExpect(status().isOk())
               .andExpect(view().name("user/add"));
    }

    @Test
    void testGetUpdateUser() throws Exception {
        Integer id = userRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(get("/admin/user/update/" + id).with(user("testuser").roles("ADMIN")))
               .andExpect(status().isOk())
               .andExpect(view().name("user/update"))
               .andExpect(model().attribute("user", Matchers.hasProperty("username", Matchers.equalTo("Username1"))));
    }

    @Test
    void testPostAddUser_Success() throws Exception {
        mockMvc.perform(post("/admin/user/validate").with(csrf())
                                                .with(user("testuser").roles("ADMIN"))
                                                .param("fullname", "Fullname2")
                                                .param("username", "Username2")
                                                .param("password", "Password2*")
                                                .param("role", "ROLE_USER"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/admin/user/list"));

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void testPostAddUser_Failure() throws Exception {
        mockMvc.perform(post("/admin/user/validate").with(csrf())
                                                .with(user("testuser").roles("ADMIN"))
                                              .param("fullname", "Fullname2")
                                              .param("username", "")
                                              .param("password", "Password2*")
                                              .param("role", "ROLE_USER"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/add"))
               .andExpect(model().attributeHasFieldErrors("user", "username"));

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    void testPostUpdateUser_Success() throws Exception {
        Integer id = userRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(post("/admin/user/update/" + id).with(csrf())
                                                    .with(user("testuser").roles("ADMIN"))
                                                  .param("fullname", "Fullname2")
                                                  .param("username", "Username2")
                                                  .param("password", "Password2*")
                                                  .param("role", "ROLE_USER"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/admin/user/list"));

        List<User> users = userRepository.findAll();
        assertThat(users.get(0)
                          .getUsername()).isEqualTo("Username2");
    }

    @Test
    void testPostUpdateUser_Failure() throws Exception {
        Integer id = userRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(post("/admin/user/update/" + id).with(csrf())
                                                    .with(user("testuser").roles("ADMIN"))
                                                  .param("fullname", "Fullname2")
                                                  .param("username", "")
                                                  .param("password", "Password2*")
                                                  .param("role", "ROLE_USER"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/update"))
               .andExpect(model().attributeHasFieldErrors("user", "username"));

        List<User> users = userRepository.findAll();
        assertThat(users.get(0)
                          .getUsername()).isEqualTo("Username1");
    }

    @Test
    void testGetDeleteUser() throws Exception {
        Integer id = userRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(get("/admin/user/delete/" + id).with(user("testuser").roles("ADMIN")))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/admin/user/list"));

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(0);
    }
}
