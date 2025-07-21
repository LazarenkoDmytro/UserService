package ua.org.blablacar.userservice.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RestExceptionHandlerTest {

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .standaloneSetup(new TestCtrl())
            .setControllerAdvice(new RestExceptionHandler())
            .build();
    }

    record Req(
        @NotBlank(message = "email is mandatory")
        @Email(message = "invalid email")
        String email) {
    }

    @EnableWebMvc
    @Controller
    @Validated
    static class TestCtrl {
        @PostMapping("/t")
        String t(@RequestBody @Valid Req r) {
            return "ok";
        }
    }

    @Test
    void validationErrorReturnsBadRequest() throws Exception {
        var body = new ObjectMapper().writeValueAsString(new Req(null));

        mvc.perform(post("/t")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }
}