package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
	
    @Autowired
   private CategoryRepository categoryRepository;
    
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category>list= categoryRepository.findAll(pageRequest);  //preciso converter a List<Category pra list<CategoryDto> :
      return list.map(x-> new CategoryDTO(x));
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
    
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity =obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
        
        return new CategoryDTO(entity);
    }
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) { //converter dto p category
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);      //converto entity novamente p retornar  CategoryDTO
    }
    @Transactional
    public CategoryDTO update(Long id,CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getReferenceById(id); //instancia
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e){
                throw new ResourceNotFoundException("Id not found" +id);
        }
    }
    public void delete(Long id) {
        try{
            categoryRepository.deleteById(id);
        }
        catch(EmptyResultDataAccessException e) { //caso tentar exluir id q nao existe
            throw new ResourceNotFoundException("id not found " + id);
        }
        catch (DataIntegrityViolationException e){
            throw  new DatabaseException("Integrity violation");
        }
    }
}
