package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TradeRepository tradeRepository;

    @BeforeEach
    void setUp() {
        Trade trade = new Trade();
        trade.setType("Type1");
        trade.setAccount("Account1");
        trade.setBuyQuantity(10d);

        tradeRepository.save(trade);
    }

    @AfterEach
    void cleanUp() {
        tradeRepository.deleteAll();
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/trade/list").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/list"));
    }

    @Test
    void testGetAddTrade() throws Exception {
        mockMvc.perform(get("/trade/add").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/add"));
    }

    @Test
    void testGetUpdateTrade() throws Exception {
        Integer id = tradeRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(get("/trade/update/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/update"))
               .andExpect(model().attribute("trade", Matchers.hasProperty("buyQuantity", Matchers.equalTo(10d))));
    }

    @Test
    void testPostAddTrade_Success() throws Exception {
        mockMvc.perform(post("/trade/validate").with(csrf())
                                                  .with(user("testuser").roles("USER"))
                                                  .param("account", "Account2")
                                                  .param("type", "Type2")
                                                  .param("buyQuantity", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/trade/list"));

        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades).hasSize(2);
    }

    @Test
    void testPostAddTrade_Failure() throws Exception {
        mockMvc.perform(post("/trade/validate").with(csrf())
                                                  .with(user("testuser").roles("USER"))
                                               .param("account", "Account2")
                                               .param("type", "Type2")
                                               .param("buyQuantity", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/add"))
               .andExpect(model().attributeHasFieldErrors("trade", "buyQuantity"));

        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades).hasSize(1);
    }

    @Test
    void testPostUpdateTrade_Success() throws Exception {
        Integer id = tradeRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(post("/trade/update/" + id).with(csrf())
                                                      .with(user("testuser").roles("USER"))
                                                   .param("account", "Account2")
                                                   .param("type", "Type2")
                                                   .param("buyQuantity", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/trade/list"));

        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades.get(0)
                            .getBuyQuantity()).isEqualTo(20d);
    }

    @Test
    void testPostUpdateTrade_Failure() throws Exception {
        Integer id = tradeRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(post("/trade/update/" + id).with(csrf())
                                                      .with(user("testuser").roles("USER"))
                                                   .param("account", "Account2")
                                                   .param("type", "Type2")
                                                   .param("buyQuantity", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/update"))
               .andExpect(model().attributeHasFieldErrors("trade", "buyQuantity"));

        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades.get(0)
                            .getBuyQuantity()).isEqualTo(10d);
    }

    @Test
    void testGetDeleteTrade() throws Exception {
        Integer id = tradeRepository.findAll()
                                       .get(0)
                                       .getId();

        mockMvc.perform(get("/trade/delete/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/trade/list"));

        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades).hasSize(0);
    }
}
