package com.devsuperior.dscatalog.service;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;     //objeto mockado

    @Mock
    private CategoryRepository categoryRepository;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product>page;
    private Product product;
    private Category category;
    ProductDTO productDTO ;


    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId =1000L;
        dependentId=4L;
        product= Factory.createProduct();
        category= Factory.createCategory();
        product=Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        // configurando a simulaçao do comportamento do repository:
        Mockito.doNothing().when(repository).deleteById(existingId);  //nao faca nada quando eu mandar deletar um id q existe

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);    //qualquer argumento

        when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }
    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);         //testando se o result NAO é nulo
        verify(repository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){
      ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);  //verficando se nao é nulo
        verify(repository).findById(existingId);
    }
    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
             service.findById(nonExistingId);

        });
        verify(repository).findById(nonExistingId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = service.update(existingId,productDTO);
        Assertions.assertNotNull(result);
    }
    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdExists(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.update(nonExistingId,productDTO);
        });
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

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
                service.delete(nonExistingId);
        });
        Mockito.verify(repository).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentIdExists(){
        Assertions.assertThrows(DatabaseException.class,()->{
            service.delete(dependentId);
        });
        Mockito.verify(repository).deleteById(dependentId);
    }

}
