package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class categoryResource {

    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public ResponseEntity<List<CategoryDTO>>findAll(){
        List<CategoryDTO>list = categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
   public ResponseEntity<CategoryDTO>findById(@PathVariable Long id){
        CategoryDTO dto = categoryService.findById(id);
        return ResponseEntity.ok().body(dto);

    }
}