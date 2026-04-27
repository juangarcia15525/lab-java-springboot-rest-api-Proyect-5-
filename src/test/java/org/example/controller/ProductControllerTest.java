package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.ApiKeyInterceptor;
import org.example.config.WebConfig;
import org.example.exception.GlobalExceptionHandler;
import org.example.model.Product;
import org.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import({ProductService.class, ApiKeyInterceptor.class, WebConfig.class, GlobalExceptionHandler.class})
class ProductControllerTest {

    private static final String API_KEY = "123456";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService.getAllProducts().forEach(product -> productService.deleteProduct(product.getName()));
    }

    @Test
    void shouldCreateProductWhenApiKeyAndBodyAreValid() throws Exception {
        Product product = new Product("Laptop", 1200.0, "Tech", 5);

        mockMvc.perform(post("/products")
                        .header("API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.0))
                .andExpect(jsonPath("$.category").value("Tech"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldRejectRequestWithoutApiKey() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Falta el header API-Key"));
    }

    @Test
    void shouldRejectInvalidProductBody() throws Exception {
        Product product = new Product("", -10.0, "", 0);

        mockMvc.perform(post("/products")
                        .header("API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de validacion"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.price").exists())
                .andExpect(jsonPath("$.errors.category").exists())
                .andExpect(jsonPath("$.errors.quantity").exists());
    }

    @Test
    void shouldRejectInvalidPriceRange() throws Exception {
        mockMvc.perform(get("/products/price")
                        .header("API-Key", API_KEY)
                        .param("min", "100")
                        .param("max", "50"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El rango de precios es invalido"));
    }
}
