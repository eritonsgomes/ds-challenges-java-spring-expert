package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.projections.ProductProjection;
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

    @Query(
        nativeQuery = true,
        value = """
            SELECT * FROM (
                SELECT DISTINCT tb_product.id, tb_product.name
                FROM tb_product
                INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
                WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN (:categoryIds))
                AND (LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%')))
            ) AS tb_result
        """,
        countQuery = """
            SELECT COUNT(*) FROM (
                SELECT DISTINCT tb_product.id, tb_product.name
                FROM tb_product
                INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
                WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN (:categoryIds))
                AND (LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%')))
            ) AS tb_result
        """
    )
    Page<ProductProjection> searchAllByNameAndCategoryIds(List<Long> categoryIds, String name, Pageable pageable);

    @Query(
        value = """
            SELECT obj FROM ProductEntity obj JOIN FETCH obj.categories
            WHERE obj.id IN :productIds
        """
    )
    List<ProductEntity> searchProductsWithCategories(List<Long> productIds);

    @Query(
        value = """
            SELECT obj
            FROM ProductEntity obj
            JOIN FETCH obj.categories c
            WHERE obj.name LIKE CONCAT('%', LOWER(:name), '%')
            AND c.id IN :categoryIds
        """,
        countQuery = """
            SELECT COUNT(*) FROM (
                SELECT obj FROM ProductEntity obj JOIN obj.categories c
                WHERE obj.name LIKE CONCAT('%', LOWER(:name), '%')
                AND c.id IN :categoryIds
            )
        """
    )
    List<ProductEntity> searchProductByNameWithCategories(String name, List<Long> categoryIds, Pageable pageable);

}
