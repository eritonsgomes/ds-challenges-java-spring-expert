package com.devsuperior.bds04.repositories;

import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

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

}
