package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {
    @Autowired
     private ProductService productService;
    @GetMapping
    public ResponseEntity<Page<ProductDTO>>findAll(Pageable pageable){
        Page<ProductDTO>list = productService.findAllPaged(pageable);//PARAMETROS: Page, size, sort

        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO>findById(@PathVariable Long id){
        ProductDTO dto = productService.findById(id);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
        dto= productService.insert(dto);
        URI uri = ServletUriComponentsBuilder       // traz status 201 de criado
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,@RequestBody ProductDTO dto){
        dto= productService.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();      //vai nos dar resposta 204
    }



}
