package com.example.aca.info;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.service-name=aca-cicd-demo",
        "app.environment=test",
        "app.message=test-message"
})
@AutoConfigureMockMvc
class AppInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsConfigurationProvidedByTheEnvironment() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("aca-cicd-demo"))
                .andExpect(jsonPath("$.environment").value("test"))
                .andExpect(jsonPath("$.message").value("test-message"));
    }
}
