package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CategoryDTO> findAll(){
        List<Category>list= categoryRepository.findAll(); //preciso converter a List<Category pra list<CategoryDto> :

      return list.stream()
                .map(x-> new CategoryDTO(x)).collect(Collectors.toList()); //transformando stream em list
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
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e){
                throw new ResourceNotFoundException("Id not found" +id);
        }

    }
}
