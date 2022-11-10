package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

   private long existingId ;
   private long nonExistingId;
   private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        existingId =1L;
        nonExistingId=1000L;
        countTotalProducts= 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product=repository.save(product);

        Assertions.assertNotNull(product.getId()); //verficar se id nao é nulo
        Assertions.assertEquals(countTotalProducts +1 ,product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);

       Optional<Product> result = repository.findById(existingId); // optional vai ter q retornar vazio, se excluiu mesmo
        Assertions.assertFalse(result.isPresent()); //isPresent testa se NAO ta  presente o objeto dentro do optional
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnOptionalNoEmptyIdWhenIdExists(){
       Optional<Product> result= repository.findById(existingId);

       Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalEmptyIdWhenIdNoExists(){
        Optional<Product> result=  repository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty());
       // Assertions.assertFalse(result.isPresent());
    }


}
