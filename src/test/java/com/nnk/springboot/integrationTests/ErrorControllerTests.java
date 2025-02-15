package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ErrorControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BidListRepository bidListRepository;

    @Test
    void testUnknownRoute() throws Exception {
        mockMvc.perform(get("/unknown").with(user("testuser").roles("USER")))
               .andExpect(status().isNotFound());
    }

    @Test
    void testAdminRouteWithUserRole() throws Exception {
        mockMvc.perform(get("/admin/user/list").with(user("testuser").roles("USER")))
               .andExpect(status().isForbidden());
    }

    @Test
    void testInternalServerError() throws Exception {
        Mockito.when(bidListRepository.save(any(BidList.class)))
               .thenThrow(new RuntimeException("Simulated failure"));

        mockMvc.perform(post("/bidList/validate").with(csrf())
                                                 .with(user("testuser").roles("USER"))
                                                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                 .param("account", "Account")
                                                 .param("type", "Type")
                                                 .param("bidQuantity", "10.0"))
               .andExpect(status().isInternalServerError())
               .andExpect(view().name("access/error"))
               .andExpect(model().attribute("message", "Une erreur est survenue lors de la création de la bidList"));
    }

    @Test
    void testObjectNotFound() throws Exception {
        mockMvc.perform(get("/bidList/update/1").with(user("testuser").roles("USER")))
               .andExpect(status().isNotFound())
               .andExpect(view().name("access/error"))
               .andExpect(model().attribute("message", "La bidList avec l'id 1 n'existe pas"));
        ;
    }

    @Test
    void test403Error() throws Exception {
        mockMvc.perform(get("/error").with(user("testuser").roles("USER"))
                                     .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN.value()))
               .andExpect(status().isForbidden())
               .andExpect(view().name("access/error"))
               .andExpect(model().attribute("message", "Vous n'êtes pas autorisé à consulter cette page"));
    }

    @Test
    void test404Error() throws Exception {
        mockMvc.perform(get("/error").with(user("testuser").roles("USER"))
                                     .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value()))
               .andExpect(status().isNotFound())
               .andExpect(view().name("access/error"))
               .andExpect(model().attribute("message", "La page que vous cherchez n'existe pas"));
    }

    @Test
    void test500Error() throws Exception {
        mockMvc.perform(get("/error").with(user("testuser").roles("USER"))
                                     .requestAttr(RequestDispatcher.ERROR_STATUS_CODE,
                                                  HttpStatus.INTERNAL_SERVER_ERROR.value()))
               .andExpect(status().isInternalServerError())
               .andExpect(view().name("access/error"))
               .andExpect(model().attribute("message", "Une erreur interne est survenue"));
    }
}
