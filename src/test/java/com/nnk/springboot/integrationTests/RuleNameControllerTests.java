package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
public class RuleNameControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @BeforeEach
    void setUp() {
        RuleName ruleName = new RuleName();
        ruleName.setName("Name1");
        ruleName.setDescription("Description1");
        ruleName.setTemplate("Template1");
        ruleName.setJson("Json1");
        ruleName.setSqlStr("SQL1");
        ruleName.setSqlPart("Part1");

        ruleNameRepository.save(ruleName);
    }

    @AfterEach
    void cleanUp() {
        ruleNameRepository.deleteAll();
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/ruleName/list").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/list"));
    }

    @Test
    void testGetAddRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/add").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/add"));
    }

    @Test
    void testGetUpdateRuleName() throws Exception {
        Integer id = ruleNameRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(get("/ruleName/update/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/update"))
               .andExpect(model().attribute("ruleName", Matchers.hasProperty("name", Matchers.equalTo("Name1"))));
    }

    @Test
    void testPostAddRuleName_Success() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with(csrf())
                                                  .with(user("testuser").roles("USER"))
                                                  .param("name", "Name2")
                                                  .param("description", "Description2")
                                                  .param("template", "Template2")
                                                  .param("json", "Json2")
                                                  .param("sql", "SQL2")
                                                  .param("sqlPart", "Part2"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/ruleName/list"));

        List<RuleName> ruleNames = ruleNameRepository.findAll();
        assertThat(ruleNames).hasSize(2);
    }

    @Test
    void testPostAddRuleName_Failure() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with(csrf())
                                                  .with(user("testuser").roles("USER"))
                                                  .param("name", "")
                                                  .param("description", "Description2")
                                                  .param("template", "Template2")
                                                  .param("json", "Json2")
                                                  .param("sql", "SQL2")
                                                  .param("sqlPart", "Part2"))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/add"))
               .andExpect(model().attributeHasFieldErrors("ruleName", "name"));

        List<RuleName> ruleNames = ruleNameRepository.findAll();
        assertThat(ruleNames).hasSize(1);
    }

    @Test
    void testPostUpdateRuleName_Success() throws Exception {
        Integer id = ruleNameRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(post("/ruleName/update/" + id).with(csrf())
                                                      .with(user("testuser").roles("USER"))
                                                      .param("name", "Name2")
                                                      .param("description", "Description2")
                                                      .param("template", "Template2")
                                                      .param("json", "Json2")
                                                      .param("sql", "SQL2")
                                                      .param("sqlPart", "Part2"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/ruleName/list"));

        List<RuleName> ruleNames = ruleNameRepository.findAll();
        assertThat(ruleNames.get(0)
                            .getName()).isEqualTo("Name2");
    }

    @Test
    void testPostUpdateRuleName_Failure() throws Exception {
        Integer id = ruleNameRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(post("/ruleName/update/" + id).with(csrf())
                                                      .with(user("testuser").roles("USER"))
                                                      .param("name", "")
                                                      .param("description", "Description2")
                                                      .param("template", "Template2")
                                                      .param("json", "Json2")
                                                      .param("sql", "SQL2")
                                                      .param("sqlPart", "Part2"))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/update"))
               .andExpect(model().attributeHasFieldErrors("ruleName", "name"));

        List<RuleName> ruleNames = ruleNameRepository.findAll();
        assertThat(ruleNames.get(0)
                            .getName()).isEqualTo("Name1");
    }

    @Test
    void testGetDeleteRuleName() throws Exception {
        Integer id = ruleNameRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(get("/ruleName/delete/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/ruleName/list"));

        List<RuleName> ruleNames = ruleNameRepository.findAll();
        assertThat(ruleNames).hasSize(0);
    }
}
