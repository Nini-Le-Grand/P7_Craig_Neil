package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurvePointRepository curvePointRepository;

    @BeforeEach
    void setUp() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(10d);
        curvePoint.setValue(10d);

        curvePointRepository.save(curvePoint);
    }

    @AfterEach
    void cleanUp() {
        curvePointRepository.deleteAll();
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/curvePoint/list").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/list"));
    }

    @Test
    void testGetAddCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/add").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void testGetUpdateCurvePoint() throws Exception {
        Integer id = curvePointRepository.findAll().get(0).getId();

        mockMvc.perform(get("/curvePoint/update/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/update"))
               .andExpect(model().attribute("curvePoint", Matchers.hasProperty("value", Matchers.equalTo(10d))));
    }

    @Test
    void testPostAddCurvePoint_Success() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                                                 .with(user("testuser").roles("USER"))
                                                 .param("term", "20")
                                                 .param("value", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/curvePoint/list"));

        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        assertThat(curvePoints).hasSize(2);
    }

    @Test
    void testPostAddCurvePoint_Failure() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                                                 .with(user("testuser").roles("USER"))
                                                 .param("term", "20")
                                                 .param("value", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/add"))
               .andExpect(model().attributeHasFieldErrors("curvePoint", "value"));

        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        assertThat(curvePoints).hasSize(1);
    }

    @Test
    void testPostUpdateCurvePoint_Success() throws Exception {
        Integer id = curvePointRepository.findAll().get(0).getId();

        mockMvc.perform(post("/curvePoint/update/" + id).with(csrf())
                                                     .with(user("testuser").roles("USER"))
                                                     .param("term", "20")
                                                     .param("value", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/curvePoint/list"));

        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        assertThat(curvePoints.get(0).getValue()).isEqualTo(20d);
    }

    @Test
    void testPostUpdateCurvePoint_Failure() throws Exception {
        Integer id = curvePointRepository.findAll().get(0).getId();

        mockMvc.perform(post("/curvePoint/update/" + id).with(csrf())
                                                     .with(user("testuser").roles("USER"))
                                                     .param("term", "20")
                                                     .param("value", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/update"))
               .andExpect(model().attributeHasFieldErrors("curvePoint", "value"));

        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        assertThat(curvePoints.get(0).getValue()).isEqualTo(10d);
    }

    @Test
    void testGetDeleteCurvePoint() throws Exception {
        Integer id = curvePointRepository.findAll().get(0).getId();

        mockMvc.perform(get("/curvePoint/delete/" + id)
                                .with(user("testuser").roles("USER")))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/curvePoint/list"));

        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        assertThat(curvePoints).hasSize(0);
    }
}
