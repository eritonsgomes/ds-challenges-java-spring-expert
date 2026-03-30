package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByAuthority(String roleOperator);

}
