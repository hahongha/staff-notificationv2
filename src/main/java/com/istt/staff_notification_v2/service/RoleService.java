package com.istt.staff_notification_v2.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.dto.RoleDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.Role;
import com.istt.staff_notification_v2.entity.User;
import com.istt.staff_notification_v2.repository.RoleRepo;
import com.istt.staff_notification_v2.repository.UserRepo;

public interface RoleService {
	RoleDTO create(RoleDTO roleDTO);

	RoleDTO delete(String id);
	
	RoleDTO update(RoleDTO roleDTO);
	
	List<RoleDTO> getRole();
	
}

@Service
class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	private static final String ENTITY_NAME = "isttRole";

	@Transactional
	@Override
	public RoleDTO create(RoleDTO roleDTO) {
		try {
			if (roleRepo.findByRole(roleDTO.getRole()).isPresent()) {
				throw new BadRequestAlertException("Role name is exist", ENTITY_NAME, "exist");
			}
			
			ModelMapper mapper = new ModelMapper();
			Role role = mapper.map(roleDTO, Role.class);
			role.setRoleId(UUID.randomUUID().toString().replaceAll("-", ""));
			roleRepo.save(role);
			return roleDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Transactional
	@Override
	public RoleDTO delete(String id) {
		try {
			Role role = roleRepo.findById(id).orElseThrow(NoResultException::new);
			if (role != null) {
				//truy cap cac User theo role ID
				List<User> users = userRepo.findByRoles(role);
				for (User user : users) {
					user.getRoles().remove(role);
				}
				roleRepo.deleteById(id);
				return new ModelMapper().map(role, RoleDTO.class);
			}
			return null;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoleDTO update(RoleDTO roleDTO) {
		try {
			if (roleRepo.findByRole(roleDTO.getRole()).isPresent()
					&&
					roleRepo.findByRole(roleDTO.getRole()).get().getRoleId()!= roleDTO.getRoleId())
					{
				throw new BadRequestAlertException("Role name is exist", ENTITY_NAME, "exist");
			}
			Role role = roleRepo.findById(roleDTO.getRoleId()).get();
			if(role!=null) {
				role.setRole(roleDTO.getRole());
				role.setDescription(roleDTO.getDescription());
			}
			roleRepo.save(role);
			return roleDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public List<RoleDTO> getRole() {
		ModelMapper mapper = new ModelMapper();
		List<Role> roles = roleRepo.findAll();
		return roles
				  .stream()
				  .map(role -> mapper.map(role, RoleDTO.class))
				  .collect(Collectors.toList());
	}

}