package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(
        value = """
            SELECT p FROM ProductEntity p JOIN FETCH p.categories
        """
    )
    List<ProductEntity> searchAll();

    @Query(
        value = """
            SELECT p FROM ProductEntity p JOIN FETCH p.categories
        """,
        countQuery = """
            SELECT COUNT(p) FROM ProductEntity p JOIN p.categories
        """
    )
    Page<ProductEntity> searchAllPages(Pageable pageable);

}
