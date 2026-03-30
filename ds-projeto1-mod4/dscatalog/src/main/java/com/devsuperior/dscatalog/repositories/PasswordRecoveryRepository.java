package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.PasswordRecoveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecoveryEntity, Long> {

    @Query("SELECT pr FROM PasswordRecoveryEntity pr WHERE pr.token = :token AND pr.expiration > :now")
    List<PasswordRecoveryEntity> searchValidTokens(String token, Instant now);

}
