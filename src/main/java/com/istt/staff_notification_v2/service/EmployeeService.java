package com.istt.staff_notification_v2.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.configuration.ApplicationProperties;
import com.istt.staff_notification_v2.configuration.ApplicationProperties.StatusEmployeeRef;
import com.istt.staff_notification_v2.dto.EmployeeDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.LeaveType;
import com.istt.staff_notification_v2.entity.Level;
import com.istt.staff_notification_v2.entity.User;
import com.istt.staff_notification_v2.repository.DepartmentRepo;
import com.istt.staff_notification_v2.repository.EmployeeRepo;
import com.istt.staff_notification_v2.repository.LevelRepo;
import com.istt.staff_notification_v2.repository.UserRepo;

public interface EmployeeService {
	EmployeeDTO create(EmployeeDTO employeeDTO);

	List<EmployeeDTO> filterEmployeeDependence(Employee employeeCurrent);

	List<EmployeeDTO> getEmployeeDependence(String employeeId);

	EmployeeDTO findByName(String name);

	EmployeeDTO findByEmail(String email);

	EmployeeDTO getEmployeeByEmployeename(String employeename);

	EmployeeDTO update(EmployeeDTO employeeDTO);

	Boolean delete(String id);

	Boolean deleteAll(List<String> ids);

	EmployeeDTO get(String id);
	
	List<EmployeeDTO> getAllEmployee();
	
}

@Service
class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private DepartmentRepo departmentRepo;
	
	@Autowired LevelRepo levelRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	ApplicationProperties props;

	private static final String ENTITY_NAME = "isttEmployee";
	
	//find max code
	private static Long getMaxLevelCode(Employee e) {
		Long max = Long.MIN_VALUE;
		for (Level level : e.getLevels()) {
			if (level.getLevelCode() > max) {
				max = Long.valueOf(level.getLevelCode());
			}
		}
		return max;
	}

	@Transactional
	@Override
	public EmployeeDTO create(EmployeeDTO employeeDTO) {
		try {
			if (employeeRepo.findByPhone(employeeDTO.getPhone()).isPresent()) {
				throw new BadRequestAlertException("Phone already exists", ENTITY_NAME, "exist");
			}
			if (employeeRepo.findByEmail(employeeDTO.getPhone()).isPresent()) {
				throw new BadRequestAlertException("Email already exists", ENTITY_NAME, "exist");
			}
			ModelMapper mapper = new ModelMapper();
			Employee employee = mapper.map(employeeDTO, Employee.class);
			employee.setEmployeeId(UUID.randomUUID().toString().replaceAll("-", ""));

			//setlevels
			Set<Level> levels = new HashSet<Level>();
			for (Level level : employeeDTO.getLevels()) {
				levels.add(levelRepo.findByLevelId(level.getLevelId()).orElseThrow(NoResultException::new));
			}
			employee.setLevels(levels);

			//set default status
			if (!props.getSTATUS_EMPLOYEE().contains(employee.getStatus())) {
				employee.setStatus(props.getSTATUS_EMPLOYEE().get(StatusEmployeeRef.ACTIVE.ordinal()));
			}
//			//set Approver
//			filterEmployeeDependence(employee);
//			employee.setEmployeeDependence(filterEmployeeDependence(employee));
//			employeeRepo.save(employee);

//			create new user 
			if (userRepo.findByUsername(employee.getEmail()).isPresent()) {
				throw new BadRequestAlertException("Bad request: Employee already exists", ENTITY_NAME,
						"Employee exists");
			}
			User user = new User();
			user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
			user.setPassword(new BCryptPasswordEncoder().encode("abcd456789"));
			user.setUsername(employee.getEmail());

			user.setEmployee(employee);

			// commit save
			userRepo.save(user);
			
			
			return employeeDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public EmployeeDTO findByEmail(String email) {
		Employee employeePage = employeeRepo.findByEmail(email).get();
		EmployeeDTO responseDTO = new ModelMapper().map(employeePage, EmployeeDTO.class);
		return responseDTO;
	}

	@Transactional
	@Override
	public EmployeeDTO findByName(String name) {
		Employee employee = employeeRepo.findByFullname(name).orElseThrow(NoResultException::new);
		ModelMapper mapper = new ModelMapper();
		return mapper.map(employee, EmployeeDTO.class);
	}

	@Transactional
	@Override
	public EmployeeDTO getEmployeeByEmployeename(String employeename) {
		Employee employee = employeeRepo.findByFullname(employeename).orElseThrow(NoResultException::new);
		ModelMapper mapper = new ModelMapper();
		return mapper.map(employee, EmployeeDTO.class);
	}

	@Transactional
	@Override
	public EmployeeDTO update(EmployeeDTO employeeDTO) {
		try {
			Employee employee = employeeRepo.findById(employeeDTO.getEmployeeId()).get();
			Employee employee2 = employeeRepo.findByPhone(employeeDTO.getPhone()).get();
			Employee employee3 = employeeRepo.findByEmail(employeeDTO.getEmail()).get();
			if(employee2!= null && employee2.getEmployeeId() != employeeDTO.getEmployeeId())
				throw new BadRequestAlertException("Email already exists", ENTITY_NAME, "exist");
			if(employee3!= null && employee3.getEmployeeId() != employeeDTO.getEmployeeId())
				throw new BadRequestAlertException("Phone already exists", ENTITY_NAME, "exist");
			if(employee!=null) {
				employee.setFullname(employeeDTO.getFullname());
				employee.setAddress(employeeDTO.getAddress());
				employee.setAvatar(employee.getAvatar());
				employee.setDateofbirth(employeeDTO.getDateofbirth());
			
				Department department = departmentRepo.findById(employeeDTO.getDepartment().getDepartmentId()).get();
				
				employee.setDepartment(department);
				
				employee.setEmail(employeeDTO.getEmail());
				employee.setPhone(employeeDTO.getPhone());
				employee.setHiredate(employeeDTO.getHiredate());
				employee.setOffdate(employeeDTO.getOffdate());
			}
			
			System.err.print(employee.toString());
			employeeRepo.save(employee);
			return employeeDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Transactional
	@Override
	public Boolean delete(String id) {
		Employee employee = employeeRepo.findById(id).orElseThrow(NoResultException::new);
		if (employee != null) {
			employee.setStatus(ApplicationProperties.StatusEmployeeRef.SUSPEND.toString());
//			User user = userRepo.findByEmployee(employee).get();
//			if(user!= null) {
//				System.err.println(user.getUserId());
//				userRepo.delete(user);
//			}
			employeeRepo.save(employee);
			return true;
		}else System.err.println("not found");
		return false;
	}

	@Override
	public Boolean deleteAll(List<String> ids) {
		List<Employee> employees = employeeRepo.findAllById(ids);
		if(employees.size()>0) {
			for (Employee employee : employees) {
				employee.setStatus(ApplicationProperties.StatusEmployeeRef.SUSPEND.toString());
//				User user = userRepo.findByEmployee(employee).get();
//				userRepo.delete(user);
				employeeRepo.save(employee);
			}
			return true;
		}
		return false;
	}

	@Override
	public EmployeeDTO get(String id) {
		try {
			Employee employee = employeeRepo.findById(id).orElseThrow(NoResultException::new);
			EmployeeDTO employeeDTO = new ModelMapper().map(employee, EmployeeDTO.class);
			return employeeDTO;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public List<EmployeeDTO> getAllEmployee() {
		ModelMapper mapper = new ModelMapper();
		List<Employee> employees = employeeRepo.findAll();
		return employees
				  .stream()
				  .map(employee -> mapper.map(employee, EmployeeDTO.class))
				  .collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDTO> getEmployeeDependence(String employeeId) {
//		try {
//
//			Employee employee = employeeRepo.findById(employeeId).orElseThrow(NoResultException::new);
//			List<String> employeeDependenceIds = employee.getEmployeeDependence();
//			if (employeeDependenceIds.size() == 0)
//				return new ArrayList<>();
//
//			List<Employee> employeeDependences = employeeRepo.findAllById(employeeDependenceIds);
////					.orElseThrow(NoResultException::new);
//			if(employeeDependences.size()<1) throw new NoResultException();
//			List<EmployeeDTO> employeeDependencesDTO = new ArrayList<>();
//			for (Employee e : employeeDependences) {
//				EmployeeDTO employeeDTO = new ModelMapper().map(e, EmployeeDTO.class);
//				employeeDependencesDTO.add(employeeDTO);
//			}
//			return employeeDependencesDTO;
//
//		} catch (ResourceAccessException e) {
//			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
//		} catch (HttpServerErrorException | HttpClientErrorException e) {
//			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
//		}
		return null;
	}
	
	@Override
	public List<EmployeeDTO> filterEmployeeDependence(Employee employeeCurrent) { // return list dependence employeeId
		ModelMapper mapper = new ModelMapper();
		List<Employee> employeesRaw = employeeRepo.findByDepartment(employeeCurrent.getDepartment().getDepartmentId());
		if (employeesRaw.isEmpty())
			throw new BadRequestAlertException("Bad request: Not found employee in employee` department", ENTITY_NAME,
					"Not found");

		Long maxLevelCurrentEmployee = getMaxLevelCode(employeeCurrent);
		List<EmployeeDTO> employeeIdDependences = new ArrayList<>();

		for (Employee e : employeesRaw) {
			if (getMaxLevelCode(e) > maxLevelCurrentEmployee) {
				EmployeeDTO approver = mapper.map(e, EmployeeDTO.class);
				employeeIdDependences.add(approver);
		}
		}
//		System.out.println("maxLevelCurrentEmployee: " + maxLevelCurrentEmployee);
		return employeeIdDependences;
	}
}