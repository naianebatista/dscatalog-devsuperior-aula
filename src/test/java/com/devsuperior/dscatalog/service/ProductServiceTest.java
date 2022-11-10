package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Table;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;     //objeto mockado
    private long existingId;
    private long nonexistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonexistingId=1000L;

        // configurando a simulaçao do comportamento do deleteById : nao faca nada quando eu mandar deletar um id q existe
        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonexistingId);

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(()->{
            service.delete(existingId);
        });
        Mockito.verify(repository).deleteById(existingId);

          //verifica se alguma chamada foi feita, no caso deleteById
       // Mockito.verify(repository,Mockito.times(1)).deleteById(existingId);
    }



}
