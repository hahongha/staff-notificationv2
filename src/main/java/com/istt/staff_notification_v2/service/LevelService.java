package com.istt.staff_notification_v2.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.dto.LevelDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.Level;
import com.istt.staff_notification_v2.entity.Role;
import com.istt.staff_notification_v2.entity.User;
import com.istt.staff_notification_v2.repository.EmployeeRepo;
import com.istt.staff_notification_v2.repository.LevelRepo;

public interface LevelService {
	LevelDTO create(LevelDTO levelDTO);

	LevelDTO delete(String id);
	
	LevelDTO update(LevelDTO levelDTO);
	
	List<LevelDTO> getAllLevel();
	
	Boolean deleteAllByListId(List<String> id);

}

@Service
class LevelServiceImpl implements LevelService {

	@Autowired
	private LevelRepo levelRepo;
	
	@Autowired
	private EmployeeRepo employeeRepo;

	private static final String ENTITY_NAME = "isttLevel";

	private void removeEmployee(Level level) {
		List<Employee> employees = employeeRepo.findByLevels(level);
		if(employees.size()>0)
			for (Employee employee : employees) {
				employee.getLevels().remove(level);
			}
	}
	
	@Override
	public LevelDTO create(LevelDTO levelDTO) {
		try {
			if (levelRepo.findByLevelNameorLevelCode(levelDTO.getLevelName(), levelDTO.getLevelCode()).isPresent()) {
				throw new BadRequestAlertException("Level Name or Level code already exists", ENTITY_NAME, "exist");
			}
			ModelMapper mapper = new ModelMapper();
			Level level = mapper.map(levelDTO, Level.class);
			level.setLevelId(UUID.randomUUID().toString().replaceAll("-", ""));
			levelRepo.save(level);
			return levelDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public LevelDTO delete(String id) {
		try {
			Level level = levelRepo.findByLevelId(id).orElseThrow(NoResultException::new);
			if (level != null) {
				removeEmployee(level);
				levelRepo.deleteById(id);
				return new ModelMapper().map(level, LevelDTO.class);
			}
			return null;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public LevelDTO update(LevelDTO levelDTO) {
		try {
			if(levelRepo.findByLevelNameorLevelCode(levelDTO.getLevelName(), levelDTO.getLevelCode()).isPresent()) {
				if(levelRepo.findByLevelCode(levelDTO.getLevelCode()).get().getLevelId().compareTo(levelDTO.getLevelId().trim())!=0) {
					throw new BadRequestAlertException("Level Code is exist", ENTITY_NAME, "exists");
				}
				if(levelRepo.findByLevelName(levelDTO.getLevelName()).get().getLevelId().compareTo(levelDTO.getLevelId().trim())!=0) {
					System.err.println(levelRepo.findByLevelName(levelDTO.getLevelName()).get().getLevelId());
					System.err.println(levelDTO.getLevelId());
					throw new BadRequestAlertException("Level Name is exist", ENTITY_NAME, "exists");
				}
				}
			
			Level level = levelRepo.findById(levelDTO.getLevelId()).get();
			
			if(level!= null) {
				level.setDescription(levelDTO.getDescription());
				level.setLevelCode(levelDTO.getLevelCode());
				level.setLevelName(levelDTO.getLevelName());
			}
			
			levelRepo.save(level);
			return levelDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public List<LevelDTO> getAllLevel() {
		ModelMapper mapper = new ModelMapper();
		List<Level> levels = levelRepo.findAll();
		return levels
				  .stream()
				  .map(level -> mapper.map(level, LevelDTO.class))
				  .collect(Collectors.toList());
	}

	@Override
	public Boolean deleteAllByListId(List<String> id) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
