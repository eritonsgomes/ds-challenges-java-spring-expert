package com.devsuperior.dsmovie.factories;

import com.devsuperior.dsmovie.entities.RoleEntity;
import com.devsuperior.dsmovie.entities.UserEntity;

public class UserFactory {
	
	public static UserEntity createUserEntity() {
        return new UserEntity(
			2L,
			"Maria",
			"maria@gmail.com",
			"$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG"
		);
	}

	public static UserEntity createCustomAdminUser(Long id, String username) {
		UserEntity user = new UserEntity(
			id,
			"Admin",
			username,
			"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"
		);

		user.addRole(new RoleEntity(2L, "ROLE_ADMIN"));

		return user;
	}

}
