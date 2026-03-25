package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dtos.requests.RoleRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.UserRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.UserResponseDTO;
import com.devsuperior.dscatalog.entities.RoleEntity;
import com.devsuperior.dscatalog.entities.UserEntity;
import com.devsuperior.dscatalog.exceptions.database.DatabaseException;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.mappers.UserRequestDTOMapper;
import com.devsuperior.dscatalog.mappers.UserResponseDTOMapper;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final UserResponseDTOMapper userResponseMapper;
	private final UserRequestDTOMapper userRequestMapper;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponseDTO create(UserRequestDTO request) {
		UserEntity entity = userRequestMapper.toEntity(request);

		entity.setPassword(passwordEncoder.encode(request.password()));

		addRolesToUserEntity(request, entity);

		entity = userRepository.save(entity);

		return userResponseMapper.toDTO(entity);
	}

	@Transactional(readOnly = true)
	public void addRolesToUserEntity(UserRequestDTO request, UserEntity entity) {
		Set<RoleRequestDTO> requestRoles = request.roles();
		Set<RoleEntity> roleEntities = new HashSet<>();

		for (RoleRequestDTO requestDTO : requestRoles) {
			if (!roleRepository.existsById(requestDTO.id())) {
				throw new ResourceNotFoundException(
					MessageFormat.format("O Role {0} não foi encontrado", requestDTO.id())
				);
			}

			RoleEntity role = roleRepository.findById(requestDTO.id()).orElse(null);

			roleEntities.add(role);
		}

		for (RoleEntity role : roleEntities) {
			entity.getRoles().add(role);
		}
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return userRepository.searchAll().stream().map(userResponseMapper::toDTO).toList();
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> findAllPages(Pageable pageable) {
		return userRepository.searchAllPages(pageable).map(userResponseMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public UserResponseDTO findById(Long id) {
		UserEntity product = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));

		return userResponseMapper.toDTO(product);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);

		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		UserEntity user = new UserEntity();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new RoleEntity(projection.getRoleId(), projection.getAuthority()));
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

		UserEntity entity = userRepository.getReferenceById(id);

		BeanUtils.copyProperties(request, entity, "id");

		entity.getRoles().clear();
		addRolesToUserEntity(request, entity);

		entity = userRepository.save(entity);

		return userResponseMapper.toDTO(entity);
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
