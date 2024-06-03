package com.istt.staff_notification_v2.service;

import java.util.UUID;

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

import com.istt.staff_notification_v2.dto.RoleDTO;
import com.istt.staff_notification_v2.entity.Role;
import com.istt.staff_notification_v2.repository.RoleRepo;

public interface RoleService {
	RoleDTO create(RoleDTO roleDTO);

	RoleDTO delete(String id);
}

@Service
class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepo roleRepo;

	@Transactional
	@Override
	public RoleDTO create(RoleDTO roleDTO) {
		try {
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

}