package com.istt.staff_notification_v2.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.LeaveType;
import com.istt.staff_notification_v2.repository.DepartmentRepo;
import com.istt.staff_notification_v2.repository.EmployeeRepo;

public interface DepartmentService {
	DepartmentDTO create(DepartmentDTO roleDTO);

	DepartmentDTO update(DepartmentDTO departmentDTO);
	
	DepartmentDTO findById(String id);
	
	DepartmentDTO findByName(String name);
	
	Boolean delete(String id);
	
	List<DepartmentDTO> getAllDepartment();
	
	Boolean deleteAllbyName(String name);
	
	Boolean deleteAllByIds(List<String> ids);

}

@Service
class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentRepo departmentRepo;
	private static final String ENTITY_NAME = "isttDepartment";
	
	@Autowired 
	private EmployeeRepo employeeRepo;

	@Transactional
	@Override
	public DepartmentDTO create(DepartmentDTO departmentDTO) {
		try {
			if (departmentRepo.findByDepartmentName(departmentDTO.getDepartmentName()).isPresent()) {
				throw new BadRequestAlertException("Department name already exists", ENTITY_NAME, "exist");
			}
			ModelMapper mapper = new ModelMapper();
			Department department = mapper.map(departmentDTO, Department.class);
			department.setDepartmentId(UUID.randomUUID().toString().replaceAll("-", ""));
			System.err.println(department.toString());
			departmentRepo.save(department);
			return departmentDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Transactional
	@Override
	public Boolean delete(String id) {
		//neu khong tim thay thi -> NoResultException
		Department department = departmentRepo.findById(id)
				.orElseThrow(NoResultException::new)
				;
		if (department != null) {
			departmentRepo.deleteById(id);
			return true;
		}else System.err.print(department.toString());
		return false;
	}

	@Override
	public DepartmentDTO update(DepartmentDTO departmentDTO) {
		try {
			if (departmentRepo.findByDepartmentName(departmentDTO.getDepartmentName()).isPresent()) {
				throw new BadRequestAlertException("Department name already exists", ENTITY_NAME, "exist");
			}
			Department department = departmentRepo.findById(departmentDTO.getDepartmentId()).get();
			if(department!=null) {
				department.setDepartmentName(departmentDTO.getDepartmentName());
				//System.err.println(departmentDTO.toString());
			}
			departmentRepo.save(department);
			return departmentDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public DepartmentDTO findById(String id) {
		Department department = departmentRepo.findById(id).orElseThrow(NoResultException::new);
		ModelMapper mapper = new ModelMapper();
		return mapper.map(department, DepartmentDTO.class);
	}

	@Override
	public DepartmentDTO findByName(String name) {
		Department department = departmentRepo.findByDepartmentName(name).get();
		ModelMapper mapper = new ModelMapper();
		return mapper.map(department, DepartmentDTO.class);
	}

	@Override
	public List<DepartmentDTO> getAllDepartment() {
		ModelMapper mapper = new ModelMapper();
		List<Department> departments = departmentRepo.findAll();
		return departments
				  .stream()
				  .map(department -> mapper.map(department, DepartmentDTO.class))
				  .collect(Collectors.toList());
	}

	@Override
	public Boolean deleteAllbyName(String name) {
		List<Department> list = departmentRepo.findByName(name);
		if (!list.isEmpty()) {
			departmentRepo.deleteAll(list);
			return true;
		}
		System.err.println(list.toString());
		return false;
	}

	@Override
	public Boolean deleteAllByIds(List<String> ids) {
		if(ids.isEmpty()) return false;
		for (String string : ids) {
			List<Employee> employees = employeeRepo.findByDepartment(string);
			if(employees.size()>0) throw new BadRequestAlertException(string + "have employee", ENTITY_NAME, "references");
		}
		departmentRepo.deleteAllById(ids);
		
		return true;
		
	}
	
}
