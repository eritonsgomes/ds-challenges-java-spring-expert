package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.entities.UserEntity;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
        nativeQuery = true,
        value = """
            SELECT tbu.id AS userId, tbu.first_name as firstName, tbu.last_name as lastName, tbu.phone,
                tbu.birth_date as birthDate, tbu.email AS username, tbu.password, tbr.id AS roleId, tbr.authority
            FROM tb_user tbu
            INNER JOIN tb_user_role tbur ON tbu.id = tbur.user_id
            INNER JOIN tb_role tbr ON tbr.id = tbur.role_id
            WHERE tbu.email = :email
        """
    )
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

    @Query(
        value = """
            SELECT u FROM UserEntity u JOIN FETCH u.roles
        """
    )
    List<UserEntity> searchAll();

    @Query(
        value = """
            SELECT u FROM UserEntity u JOIN FETCH u.roles
        """,
        countQuery = """
            SELECT COUNT(u) FROM UserEntity u JOIN u.roles
        """
    )
    Page<ProductEntity> searchAllPages(Pageable pageable);

}
