package com.devsuperior.dscatalog.projections;

import java.time.LocalDate;

public interface UserDetailsProjection {
    Long getUserId();
    String getFirstName();
    String getLastName();
    String getPhone();
    LocalDate getBirthDate();
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
