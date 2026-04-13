package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.projections.UserDetailsProjectionImpl;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactory {
	
	public static List<UserDetailsProjection> createCustomClientUser(String username) {
		
		List<UserDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsProjectionImpl(username, "123", 1L, "ROLE_CLIENT"));
		return list;
	}
	
	public static List<UserDetailsProjection> createCustomAdminUser(String username) {
		
		List<UserDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsProjectionImpl(username, "123", 2L, "ROLE_ADMIN"));
		return list;
	}
	
	public static List<UserDetailsProjection> createCustomAdminClientUser(String username) {
		
		List<UserDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsProjectionImpl(username, "123", 1L, "ROLE_CLIENT"));
		list.add(new UserDetailsProjectionImpl(username, "123", 2L, "ROLE_ADMIN"));
		return list;
	}

}
