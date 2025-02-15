package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
public class RatingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        Rating rating = new Rating();
        rating.setFitchRating("1");
        rating.setSandPRating("1");
        rating.setMoodysRating("1");
        rating.setOrderNumber(1);

        ratingRepository.save(rating);
    }

    @AfterEach
    void cleanUp() {
        ratingRepository.deleteAll();
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/rating/list").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/list"));
    }

    @Test
    void testGetAddRating() throws Exception {
        mockMvc.perform(get("/rating/add").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/add"));
    }

    @Test
    void testGetUpdateRating() throws Exception {
        Integer id = ratingRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(get("/rating/update/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/update"))
               .andExpect(model().attribute("rating", Matchers.hasProperty("order", Matchers.equalTo(1))));
    }

    @Test
    void testPostAddRating_Success() throws Exception {
        mockMvc.perform(post("/rating/validate").with(csrf())
                                                .with(user("testuser").roles("USER"))
                                                .param("fitchRating", "2")
                                                .param("sandPRating", "2")
                                                .param("moodysRating", "2")
                                                .param("order", "2"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/rating/list"));

        List<Rating> ratings = ratingRepository.findAll();
        assertThat(ratings).hasSize(2);
    }

    @Test
    void testPostAddRating_Failure() throws Exception {
        mockMvc.perform(post("/rating/validate").with(csrf())
                                                .with(user("testuser").roles("USER"))
                                                .param("fitchRating", "2")
                                                .param("sandPRating", "2")
                                                .param("moodysRating", "2")
                                                .param("order", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/add"))
               .andExpect(model().attributeHasFieldErrors("rating", "order"));

        List<Rating> ratings = ratingRepository.findAll();
        assertThat(ratings).hasSize(1);
    }

    @Test
    void testPostUpdateRating_Success() throws Exception {
        Integer id = ratingRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(post("/rating/update/" + id).with(csrf())
                                                    .with(user("testuser").roles("USER"))
                                                    .param("fitchRating", "2")
                                                    .param("sandPRating", "2")
                                                    .param("moodysRating", "2")
                                                    .param("order", "2"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/rating/list"));

        List<Rating> ratings = ratingRepository.findAll();
        assertThat(ratings.get(0)
                          .getOrderNumber()).isEqualTo(2);
    }

    @Test
    void testPostUpdateRating_Failure() throws Exception {
        Integer id = ratingRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(post("/rating/update/" + id).with(csrf())
                                                    .with(user("testuser").roles("USER"))
                                                    .param("fitchRating", "2")
                                                    .param("sandPRating", "2")
                                                    .param("moodysRating", "2")
                                                    .param("order", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/update"))
               .andExpect(model().attributeHasFieldErrors("rating", "order"));

        List<Rating> ratings = ratingRepository.findAll();
        assertThat(ratings.get(0)
                          .getOrderNumber()).isEqualTo(1);
    }

    @Test
    void testGetDeleteRating() throws Exception {
        Integer id = ratingRepository.findAll()
                                     .get(0)
                                     .getId();

        mockMvc.perform(get("/rating/delete/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/rating/list"));

        List<Rating> ratings = ratingRepository.findAll();
        assertThat(ratings).hasSize(0);
    }
}
