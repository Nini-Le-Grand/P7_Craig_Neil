package com.nnk.springboot.integrationTests;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidListControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BidListRepository bidListRepository;

    @BeforeEach
    void setUp() {
        BidList bidList = new BidList();
        bidList.setAccount("Account");
        bidList.setType("Type");
        bidList.setBidQuantity(10d);

        bidListRepository.save(bidList);
    }

    @AfterEach
    void cleanUp() {
        bidListRepository.deleteAll();
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/bidList/list").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/list"));
    }

    @Test
    void testGetAddBidList() throws Exception {
        mockMvc.perform(get("/bidList/add").with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/add"));
    }

    @Test
    void testGetUpdateBidList() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getId();

        mockMvc.perform(get("/bidList/update/" + id).with(user("testuser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/update"))
               .andExpect(model().attribute("bidList", Matchers.hasProperty("bidQuantity", Matchers.equalTo(10d))));
    }

    @Test
    void testPostAddBidList_Success() throws Exception {
        mockMvc.perform(post("/bidList/validate").with(csrf())
                                                 .with(user("testuser").roles("USER"))
                                                 .param("account", "Account2")
                                                 .param("type", "Type2")
                                                 .param("bidQuantity", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/bidList/list"));

        List<BidList> bidLists = bidListRepository.findAll();
        assertThat(bidLists).hasSize(2);
    }

    @Test
    void testPostAddBidList_Failure() throws Exception {
        mockMvc.perform(post("/bidList/validate").with(csrf())
                                                 .with(user("testuser").roles("USER"))
                                                 .param("account", "Account2")
                                                 .param("type", "Type2")
                                                 .param("bidQuantity", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasFieldErrors("bidList", "bidQuantity"));

        List<BidList> bidLists = bidListRepository.findAll();
        assertThat(bidLists).hasSize(1);
    }

    @Test
    void testPostUpdateBidList_Success() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getId();

        mockMvc.perform(post("/bidList/update/" + id).with(csrf())
                                                 .with(user("testuser").roles("USER"))
                                                 .param("account", "Account2")
                                                 .param("type", "Type2")
                                                 .param("bidQuantity", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/bidList/list"));

        List<BidList> bidLists = bidListRepository.findAll();
        assertThat(bidLists.get(0).getBidQuantity()).isEqualTo(20d);
    }

    @Test
    void testPostUpdateBidList_Failure() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getId();

        mockMvc.perform(post("/bidList/update/" + id).with(csrf())
                                                     .with(user("testuser").roles("USER"))
                                                     .param("account", "Account2")
                                                     .param("type", "Type2")
                                                     .param("bidQuantity", "0"))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/update"))
               .andExpect(model().attributeHasFieldErrors("bidList", "bidQuantity"));


        List<BidList> bidLists = bidListRepository.findAll();
        assertThat(bidLists.get(0).getBidQuantity()).isEqualTo(10d);
    }

    @Test
    void testGetDeleteBidList() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getId();

        mockMvc.perform(get("/bidList/delete/" + id)
                                                 .with(user("testuser").roles("USER")))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/bidList/list"));

        List<BidList> bidLists = bidListRepository.findAll();
        assertThat(bidLists).hasSize(0);
    }
}
