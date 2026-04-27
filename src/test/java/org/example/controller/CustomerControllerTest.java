package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.ApiKeyInterceptor;
import org.example.config.WebConfig;
import org.example.exception.GlobalExceptionHandler;
import org.example.model.Customer;
import org.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import({CustomerService.class, ApiKeyInterceptor.class, WebConfig.class, GlobalExceptionHandler.class})
class CustomerControllerTest {

    private static final String API_KEY = "123456";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService.getAllCustomers().forEach(customer -> customerService.deleteCustomer(customer.getEmail()));
    }

    @Test
    void shouldCreateCustomerWhenBodyIsValid() throws Exception {
        Customer customer = new Customer("Ana", "ana@example.com", 30, "Madrid");

        mockMvc.perform(post("/customers")
                        .header("API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@example.com"));
    }

    @Test
    void shouldReturnNotFoundForMissingCustomer() throws Exception {
        mockMvc.perform(get("/customers/noone@example.com")
                        .header("API-Key", API_KEY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente no encontrado: noone@example.com"));
    }

    @Test
    void shouldValidateCustomerPayload() throws Exception {
        Customer customer = new Customer("", "correo-invalido", 16, "");

        mockMvc.perform(post("/customers")
                        .header("API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.age").exists())
                .andExpect(jsonPath("$.errors.address").exists());
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        customerService.createCustomer(new Customer("Luis", "luis@example.com", 28, "Sevilla"));

        mockMvc.perform(delete("/customers/luis@example.com")
                        .header("API-Key", API_KEY))
                .andExpect(status().isNoContent());
    }
}
