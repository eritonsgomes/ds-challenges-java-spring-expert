package com.devsuperior.bds04.repositories;


import com.devsuperior.bds04.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query(value = "SELECT c FROM City c ORDER BY c.name")
    List<City> findAllByNameAsc();

}
