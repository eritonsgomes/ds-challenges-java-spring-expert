package com.devsuperior.dscommerce.repositories;


import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.projections.CategoryMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(nativeQuery = true, value = "SELECT c.id, c.name FROM tb_category c WHERE c.id IN :ids")
    List<CategoryMinProjection> searchByIdIn(List<Long> ids);

    @Query(nativeQuery = true, value = "SELECT c.id, c.name FROM tb_category c WHERE c.id IN :ids")
    Page<CategoryMinProjection> searchByIdIn(List<Long> ids, Pageable pageable);

}
