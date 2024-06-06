package com.istt.staff_notification_v2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import com.istt.staff_notification_v2.configuration.ApplicationProperties;
import com.istt.staff_notification_v2.dto.RoleDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.dto.UserDTO;
import com.istt.staff_notification_v2.dto.UserResponse;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.Role;
import com.istt.staff_notification_v2.entity.User;
import com.istt.staff_notification_v2.repository.EmployeeRepo;
import com.istt.staff_notification_v2.repository.RoleRepo;
import com.istt.staff_notification_v2.repository.UserRepo;

public interface UserService {
	UserDTO create(UserDTO userDTO);

	Boolean deletebyEmployee(String id);

	UserDTO findByName(String name);

	UserDTO findByEmail(String email);

	UserDTO getEmployeeByEmployeename(String employeename);


	Boolean delete(String id);

	Boolean deleteAll(List<String> ids);
	
	List<UserResponse> getAll();

	UserResponse get(String id);

	UserResponse updatePassword(UserDTO userDTO);

	List<UserResponse> search(SearchDTO searchDTO);
	
	UserDTO update(UserDTO userDTO);
	
}

@Service
class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired 
	
	private RoleRepo roleRepo;

	@Autowired
	ApplicationProperties props;
	
	private void removeRole(User user) {
		List<Role> roles = roleRepo.findByUsers(user);
		if(roles.size()>0)
			for (Role role : roles) {
				role.getUsers().remove(user);
			}
	}

	@Override
	@Transactional
	public UserDTO create(UserDTO userDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			// creatte user
			User user = mapper.map(userDTO, User.class);
			String user_id = UUID.randomUUID().toString().replaceAll("-", "");
			user.setUserId(user_id);
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

			// map employee
			Employee employee = new Employee();
			if (user.getEmployee() != null) {
				employee = user.getEmployee();
				if (!props.getSTATUS_EMPLOYEE().contains(user.getEmployee().getStatus())) { // Validate if not contain
																							// set default suspend
					employee.setStatus(props.getSTATUS_EMPLOYEE().get(0));
				}
			} else {
				employee.setEmail(user.getUsername());
				employee.setStatus(props.getSTATUS_EMPLOYEE().get(0));
			}
			employee.setEmployeeId(UUID.randomUUID().toString().replaceAll("-", ""));

			user.setEmployee(employee);

			// commit save
			userRepo.save(user);
			return mapper.map(user, UserDTO.class);

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public UserDTO findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO findByEmail(String email) {
		Employee employee = employeeRepo.findByEmail(email).orElseThrow(NoResultException::new);
		
		User user = userRepo.findByEmployee(employee).orElseThrow(NoResultException::new);
		
		ModelMapper mapper = new ModelMapper();
		
		 return mapper.map(user, UserDTO.class);
	}

	@Override
	public UserDTO getEmployeeByEmployeename(String employeename) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean delete(String id) {
		User user = userRepo.findById(id).orElseThrow(NoResultException::new);
		if(user!= null) {
			removeRole(user);
			userRepo.delete(user);
		}
		return false;
	}
	
	@Override
	public Boolean deletebyEmployee(String id) {
		Employee employee = employeeRepo.findById(id).orElseThrow(NoResultException::new);
		User user = userRepo.findByEmployee(employee).orElseThrow(NoResultException::new);
		if(user!= null) {
			removeRole(user);
			userRepo.delete(user);
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public Boolean deleteAll(List<String> ids) {
		if(ids.isEmpty()) return false;
		userRepo.deleteAllById(ids);
		return true;
	}

	@Override
	@Transactional
	public UserResponse get(String id) {
		try {
			User user = userRepo.findByUserId(id).orElseThrow(NoResultException::new);
			UserResponse userResponse = new ModelMapper().map(user, UserResponse.class);
			return userResponse;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}

	}

	@Override
	@Transactional
	public UserResponse updatePassword(UserDTO userDTO) {
		try {
			User user = userRepo.findByUserId(userDTO.getId()).orElseThrow(NoResultException::new);
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			userRepo.save(user);
			UserResponse userResponse = new ModelMapper().map(user, UserResponse.class);
			return userResponse;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}

	}

	@Override
	public List<UserResponse> search(SearchDTO searchDTO) {
		try {
			List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList)
					.stream().map(order -> {
						if (order.getOrder().equals(SearchDTO.ASC))
							return Sort.Order.asc(order.getProperty());

						return Sort.Order.desc(order.getProperty());
					}).collect(Collectors.toList());

			Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

			Page<User> page = userRepo.search(searchDTO.getValue(), pageable);

			// Chuyển đổi từ Page<User> sang List<UserDTO>
//			List<UserDTO> userDTOList = page.getContent().stream().map(user -> convert(user))
//					.collect(Collectors.toList());
//			List<UserDTO> userDTOList = page.getContent().stream().map(user -> new UserDTO())
//					.collect(Collectors.toList());

			List<UserResponse> userDTOList = new ArrayList<>();

			for (User user : page.getContent()) {
				UserResponse userResponse = new ModelMapper().map(user, UserResponse.class);
				userDTOList.add(userResponse);
			}

			return userDTOList;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public List<UserResponse> getAll() {
		ModelMapper mapper = new ModelMapper();
		List<User> users  = userRepo.findAll();
		return users
				  .stream()
				  .map(user -> mapper.map(user, UserResponse.class))
				  .collect(Collectors.toList());
	}

	@Override
	public UserDTO update(UserDTO userDTO) {
		try {
			User user = userRepo.findByUserId(userDTO.getId()).orElseThrow(NoResultException::new);
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			ModelMapper model = new ModelMapper();
			Set<Role> roles = new HashSet<Role>();
			for (RoleDTO roleDTO : userDTO.getRoles()) {
				Role role = roleRepo.findById(roleDTO.getRoleId()).get();
				roles.add(role);
			}
			user.setRoles(roles);
			userRepo.save(user);
			return userDTO;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
	

}