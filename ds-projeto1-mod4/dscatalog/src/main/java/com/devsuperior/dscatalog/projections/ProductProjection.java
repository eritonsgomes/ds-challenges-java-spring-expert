package com.devsuperior.dscatalog.projections;

import com.devsuperior.dscatalog.entities.ID;

public interface ProductProjection extends ID<Long> {

    Long getId();
    String getName();

}
