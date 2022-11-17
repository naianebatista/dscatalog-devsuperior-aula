package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long  nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception{

        existingId= 1L;
        nonExistingId=1000L;
        dependentId= 3L;

       productDTO=Factory.createProductDto();
       page = new PageImpl<>(List.of(productDTO));

       when(productService.findAllPaged(any())).thenReturn(page);

       when(productService.findById(existingId)).thenReturn(productDTO);
       when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

       when(productService.update(eq(existingId),any())).thenReturn(productDTO);
       when(productService.update(eq(nonExistingId),any())).thenThrow(ResourceNotFoundException.class);

       doNothing().when(productService).delete(existingId);
       doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
       doThrow(DatabaseException.class).when(productService).delete(dependentId);

       when(productService.insert(any())).thenReturn(productDTO);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
       // mockMvc.perform(get("/products")).andExpect(status().isOk());

        ResultActions result =
                mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());   //assertions
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception{
        ResultActions result =
                mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").doesNotExist());       //jsonPath analisa  o corpo da resposta, $ acessa objeto da resposta
        result.andExpect(jsonPath("$.name").doesNotExist());
        result.andExpect(jsonPath("$.description").doesNotExist());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/{id}",nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
     public void updatedShouldReturnProductWhenIdExists() throws Exception {
        String jsonBody= objectMapper.writeValueAsString(productDTO);   //converti productDTO em uma string formato json

        ResultActions result =
                mockMvc.perform(put("/products/{id}",existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON) //o tippo de corpo da requisicao:json
                        .accept(MediaType.APPLICATION_JSON));  //tipo resposta resposta:json
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").doesNotExist());
        result.andExpect(jsonPath("$.name").doesNotExist());
        result.andExpect(jsonPath("$.description").doesNotExist());
     }

     @Test
     public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
            mockMvc.perform(put("/products/{id}",nonExistingId)
                    .content(jsonBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDtoStatusCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").doesNotExist());
        result.andExpect(jsonPath("$.name").doesNotExist());
        result.andExpect(jsonPath("$.description").doesNotExist());

    }
    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/products/{id}",existingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        ResultActions result =
                mockMvc.perform(delete("/products/{id}",nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }


}
