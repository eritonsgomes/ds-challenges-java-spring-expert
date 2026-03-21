package com.devsuperior.bds04.services;


import com.devsuperior.bds04.dto.requests.RoleRequestDTO;
import com.devsuperior.bds04.dto.requests.UserRequestDTO;
import com.devsuperior.bds04.dto.responses.UserResponseDTO;
import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.exceptions.database.DatabaseException;
import com.devsuperior.bds04.exceptions.services.ResourceNotFoundException;
import com.devsuperior.bds04.projections.UserDetailsProjection;
import com.devsuperior.bds04.repositories.RoleRepository;
import com.devsuperior.bds04.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponseDTO create(UserRequestDTO request) {
		User entity = request.toEntity();

		entity.setPassword(passwordEncoder.encode(request.getPassword()));

		addRolesToUserEntity(request, entity);

		entity = userRepository.save(entity);

		return new UserResponseDTO(entity);
	}

	@Transactional(readOnly = true)
	public void addRolesToUserEntity(UserRequestDTO request, User entity) {
		Set<RoleRequestDTO> requestRoles = request.getRoles();
		Set<Role> roleEntities = new HashSet<>();

		for (RoleRequestDTO requestDTO : requestRoles) {
			if (!roleRepository.existsById(requestDTO.getId())) {
				throw new ResourceNotFoundException(
					MessageFormat.format("O Role {0} não foi encontrado", requestDTO.getId())
				);
			}

			Role role = roleRepository.findById(requestDTO.getId()).orElse(null);

			roleEntities.add(role);
		}

		for (Role role : roleEntities) {
			entity.getRoles().add(role);
		}
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return userRepository.findAll().stream().map(UserResponseDTO::new).toList();
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> findAllPages(Pageable pageable) {
		return userRepository.findAll(pageable).map(UserResponseDTO::new);
	}

	@Transactional(readOnly = true)
	public UserResponseDTO findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));

		return new UserResponseDTO(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);

		if (result.isEmpty()) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		User user = new User();

		user.setEmail(result.getFirst().getUsername());
		user.setPassword(result.getFirst().getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}

	@Transactional(readOnly = true)
	public long count() {
		return userRepository.count();
	}

	@Transactional
	public UserResponseDTO update(Long id, UserRequestDTO request) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}

		User entity = userRepository.getReferenceById(id);

		BeanUtils.copyProperties(request, entity, "id");

		entity.getRoles().clear();
		addRolesToUserEntity(request, entity);

		entity = userRepository.save(entity);

		return new UserResponseDTO(entity);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}

		try {
			userRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

}
