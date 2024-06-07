package com.istt.staff_notification_v2.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.configuration.ApplicationProperties;
import com.istt.staff_notification_v2.configuration.ApplicationProperties.StatusAttendanceRef;
import com.istt.staff_notification_v2.configuration.ApplicationProperties.StatusEmployeeRef;
import com.istt.staff_notification_v2.dto.AttendanceDTO;
import com.istt.staff_notification_v2.dto.LeaveRequestDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.entity.Attendance;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.repository.AttendanceRepo;
import com.istt.staff_notification_v2.repository.EmployeeRepo;

public interface AttendanceService {
	
	AttendanceDTO create(AttendanceDTO attendanceDTO);
	
	ResponseDTO<List<AttendanceDTO>> searchByEmployeeName(SearchDTO searchDTO);

	AttendanceDTO createWhenApprover(LeaveRequestDTO leaveRequestDTO);
	
	AttendanceDTO update(AttendanceDTO attendanceDTO);
	
	Boolean deleteById(String id);
	
	Boolean deletebylistId(List<String> id);
		
	AttendanceDTO get(String id);
	
	List<AttendanceDTO> getAll();
	
	List<AttendanceDTO> getStatus(String status);
	
	ResponseDTO<List<AttendanceDTO>> search(SearchDTO searchDTO);
}

@Service
class AttendanceServiceImpl implements AttendanceService{

	@Autowired
	private AttendanceRepo attendanceRepo;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	
	@Autowired
	ApplicationProperties props;
	
	private static final String ENTITY_NAME = "isttAttendance";
	
	@Transactional
	@Override
	public AttendanceDTO create(AttendanceDTO attendanceDTO) {
		try {
			
			ModelMapper mapper = new ModelMapper();
			Attendance attendance = mapper.map(attendanceDTO, Attendance.class);
			attendance.setAttendanceId(UUID.randomUUID().toString().replaceAll("-", ""));
			
			Employee employee = employeeRepo.findById(attendanceDTO.getEmployee().getEmployeeId()).orElseThrow(NoResultException::new);
			if(employee.getStatus().compareTo(props.getSTATUS_EMPLOYEE().get(StatusEmployeeRef.SUSPEND.ordinal()))==0)
				throw new BadRequestAlertException("employee is SUSPEND", ENTITY_NAME, "exists");
			attendance.setEmployee(employee);
			
			if (!props.getSTATUS_ATTENDANCE().contains(attendanceDTO.getStatus())) {
				attendance.setStatus(props.getSTATUS_ATTENDANCE().get(StatusAttendanceRef.ABSENT.ordinal()));
			}else attendance.setStatus(attendance.getStatus());
			
			attendanceRepo.save(attendance);
			
			return attendanceDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	@Transactional
	@Override
	public AttendanceDTO update(AttendanceDTO attendanceDTO) {
		try {
			Attendance attendance = attendanceRepo.findById(attendanceDTO.getAttendanceId()).orElseThrow(NoResultException::new);
			
			attendance.setStartDate(attendanceDTO.getStartDate());
			attendance.setEndDate(attendanceDTO.getEndDate());
			
			if (!props.getSTATUS_ATTENDANCE().contains(attendanceDTO.getStatus())) {
				attendance.setStatus(props.getSTATUS_ATTENDANCE().get(StatusAttendanceRef.ABSENT.ordinal()));
			}else attendance.setStatus(attendance.getStatus());
			
			attendance.setNote(attendanceDTO.getNote());
			
			attendanceRepo.save(attendance);
			
			return attendanceDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	@Transactional
	@Override
	public Boolean deleteById(String id) {
		Attendance attendance = attendanceRepo.findById(id).orElseThrow(NoResultException::new);
		if(attendance!= null) {
			attendanceRepo.deleteById(id);
		}
		return null;
	}
	@Transactional
	@Override
	public Boolean deletebylistId(List<String> ids) {
		List<Attendance> attendances = attendanceRepo.findAllById(ids);
		if(attendances.size()>0) {
			attendanceRepo.deleteAllById(ids);
//			attendanceRepo.deleteAll(attendances);
			return true;
		}
		return false;
	}

	@Override
	public AttendanceDTO get(String id) {
		ModelMapper mapper = new ModelMapper();
		Attendance attendance = attendanceRepo.findById(id).get();
		if(attendance==null) throw new NoResultException();
		return mapper.map(attendance, AttendanceDTO.class);
	}
	@Override
	public AttendanceDTO createWhenApprover(LeaveRequestDTO leaveRequestDTO) {
		try {
			
			ModelMapper mapper = new ModelMapper();
			Attendance attendance = new Attendance();
			attendance.setAttendanceId(UUID.randomUUID().toString().replaceAll("-", ""));
			attendance.setStartDate(leaveRequestDTO.getStartDate());
			attendance.setEndDate(leaveRequestDTO.getEndDate());
			attendance.setStatus(props.getSTATUS_ATTENDANCE().get(StatusAttendanceRef.ABSENT.ordinal()));
			Employee employee = employeeRepo.findById(leaveRequestDTO.getEmployee().getEmployeeId()).orElseThrow(NoResultException::new);
			attendance.setEmployee(employee);
			
			attendanceRepo.save(attendance);
			
			return mapper.map(attendance, AttendanceDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	@Override
	public List<AttendanceDTO> getAll() {
		List<Attendance> attendances = attendanceRepo.getAll();
		ModelMapper mapper = new ModelMapper();
		return attendances
				  .stream()
				  .map(attendance -> mapper.map(attendance, AttendanceDTO.class))
				  .collect(Collectors.toList());
	}
	@Override
	public List<AttendanceDTO> getStatus(String status) {
		if (!props.getSTATUS_ATTENDANCE().contains(status)) {
		throw new BadRequestAlertException("not have status", ENTITY_NAME, "exists");
	}
		
		List<Attendance> attendances = attendanceRepo.getStatus(status);
		
		ModelMapper mapper = new ModelMapper();
		
		if(attendances.size()<1) return null;
		return attendances.stream()
				  .map(attendance -> mapper.map(attendance, AttendanceDTO.class))
				  .collect(Collectors.toList());
	}
	@Override
	public ResponseDTO<List<AttendanceDTO>> search(SearchDTO searchDTO) {
		try {
			List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList)
					.stream().map(order -> {
						if (order.getOrder().equals(SearchDTO.ASC))
							return Sort.Order.asc(order.getProperty());

						return Sort.Order.desc(order.getProperty());
					}).collect(Collectors.toList());
			Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

			Page<Attendance> page = attendanceRepo.findByStatus(searchDTO.getValue(), pageable);
			ModelMapper mapper = new ModelMapper();
			List<AttendanceDTO> levelDTOs = page.getContent().stream()
					  .map(attendance -> mapper.map(attendance, AttendanceDTO.class))
					  .collect(Collectors.toList());
			
			
			ResponseDTO<List<AttendanceDTO>> responseDTO = mapper.map(page, ResponseDTO.class);
			responseDTO.setData(levelDTOs);
			return responseDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
//		return null;
	}
	
	@Override
	public ResponseDTO<List<AttendanceDTO>> searchByEmployeeName(SearchDTO searchDTO) {
		try {
			List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList)
					.stream().map(order -> {
						if (order.getOrder().equals(SearchDTO.ASC))
							return Sort.Order.asc(order.getProperty());

						return Sort.Order.desc(order.getProperty());
					}).collect(Collectors.toList());
			Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

			Page<Attendance> page = attendanceRepo.searchByEmployeeName(searchDTO.getValue(), pageable);
			ModelMapper mapper = new ModelMapper();
			List<AttendanceDTO> levelDTOs = page.getContent().stream()
					  .map(attendance -> mapper.map(attendance, AttendanceDTO.class))
					  .collect(Collectors.toList());
			
			
			ResponseDTO<List<AttendanceDTO>> responseDTO = mapper.map(page, ResponseDTO.class);
			responseDTO.setData(levelDTOs);
			return responseDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
//		return null;
	}
	
	
}
