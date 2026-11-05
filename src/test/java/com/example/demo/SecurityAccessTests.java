package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAccessTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeIsPublic() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void anonymousIsRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/guest"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void guestCanOpenGuestButNotUser() throws Exception {
        mockMvc.perform(get("/guest")).andExpect(status().isOk());
        mockMvc.perform(get("/user")).andExpect(redirectedUrl("/access-denied"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanOpenUserButNotAdmin() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isOk());
        mockMvc.perform(get("/admin")).andExpect(redirectedUrl("/access-denied"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanOpenEverything() throws Exception {
        mockMvc.perform(get("/guest")).andExpect(status().isOk());
        mockMvc.perform(get("/user")).andExpect(status().isOk());
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }
}
