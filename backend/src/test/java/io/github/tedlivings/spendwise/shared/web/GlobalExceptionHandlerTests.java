package io.github.tedlivings.spendwise.shared.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.tedlivings.spendwise.shared.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


class GlobalExceptionHandlerTests {
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new FakeController())
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}
	
	@Test
	void shouldReturnFieldErrorsWhenRequestBodyIsInvalid() throws Exception{
		mockMvc.perform(post("/fake/expenses")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"name":"", "amount":-5}
						"""))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
			.andExpect(jsonPath("$.type").value("urn:spendwise:problem:validation-error"))
			.andExpect(jsonPath("$.title").value("Validation failed"))
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.instance").value("/fake/expenses"))
            .andExpect(jsonPath("$.errors.length()").value(2))
            .andExpect(jsonPath("$.errors[?(@.field == 'name')]").exists())
            .andExpect(jsonPath("$.errors[?(@.field == 'amount')]").exists());
	}
	
	@Test
	void shouldReturnNotFoundWhenResourceDoesNotExist() throws Exception{
		mockMvc.perform(get("/fake/missing"))
		.andExpect(status().isNotFound())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
		.andExpect(jsonPath("$.type").value("urn:spendwise:problem:resource-not-found"))
        .andExpect(jsonPath("$.detail").value("Expense with id '42' was not found"));
	}
	
	@Test
    void shouldHideInternalDetailsWhenUnexpectedErrorOccurs() throws Exception {
        mockMvc.perform(get("/fake/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value("urn:spendwise:problem:internal-error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred"));
    }
	
	
	@RestController
	@RequestMapping("/fake")
	static class FakeController{
		
		public record CreateRequest(@NotBlank String name, @Positive BigDecimal amount) {}
		
		@PostMapping("/expenses")
		void create(@Valid @RequestBody CreateRequest request) {
			// intentionally empty: only validation matters here
		}
		
		@GetMapping("/missing")
		void missing() {
			throw new ResourceNotFoundException("Expense", 42);
		}
		
		@GetMapping("/boom")
		void boom() {
			throw new IllegalStateException("secret internal detail");
		}
		
	}
	
}





