package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.CategoryMinDTO;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.projections.CategoryMinProjection;
import com.devsuperior.dscommerce.repositories.CategoryRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> result = productRepository.searchByName(name, pageable);
        return result.map(ProductMinDTO::new);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        List<CategoryMinDTO> categories = searchCategoryFrom(dto);

        Product entity = saveProductFrom(dto);

        dto = new ProductDTO(entity);
        dto.setCategories(categories);

        return dto;
    }

    private List<CategoryMinDTO> searchCategoryFrom(ProductDTO dto) {
        List<CategoryMinDTO> categories = new ArrayList<>();

        List<CategoryMinProjection> categoriesFound = categoryRepository.searchByIdIn(dto.getCategories().stream()
                .map(CategoryMinDTO::getId).toList());

        for (CategoryMinProjection projection: categoriesFound) {
            categories.add(new CategoryMinDTO(projection.getId(), projection.getName()));
        }

        return categories;
    }

    private Product saveProductFrom(ProductDTO dto) {
        Product entity = new Product();

        copyDtoToEntity(dto, entity);

        entity = productRepository.save(entity);

        return entity;
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);

            copyDtoToEntity(dto, entity);

            entity = productRepository.save(entity);

            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
    	if (!productRepository.existsById(id)) {
    		throw new ResourceNotFoundException("Recurso não encontrado");
    	}
    	try {
            productRepository.deleteById(id);
    	}
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.setCategories(new HashSet<>());

        dto.getCategories().forEach(cat -> {
            Category category = new Category();
            category.setId(cat.getId());
            entity.getCategories().add(category);
        });
    }
}
