package com.istt.staff_notification_v2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.configuration.ApplicationProperties;
import com.istt.staff_notification_v2.configuration.ApplicationProperties.StatusLeaveRequestRef;
import com.istt.staff_notification_v2.dto.LeaveRequestDTO;
import com.istt.staff_notification_v2.dto.LeaveTypeDTO;
import com.istt.staff_notification_v2.dto.MailRequestDTO;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.LeaveRequest;
import com.istt.staff_notification_v2.entity.LeaveType;
import com.istt.staff_notification_v2.entity.User;
import com.istt.staff_notification_v2.repository.EmployeeRepo;
import com.istt.staff_notification_v2.repository.LeaveRequestRepo;
import com.istt.staff_notification_v2.repository.LeaveTypeRepo;
import com.istt.staff_notification_v2.repository.UserRepo;

public interface LeaveRequestService {

	LeaveRequestDTO create(MailRequestDTO leaveRequestDTO);

	LeaveRequestDTO update(LeaveRequestDTO leaveRequestDTO);

	Boolean delete(String id);
	
	Boolean deleteALL(List<String> ids);

	LeaveRequestDTO get(String id);
	
	List<LeaveRequestDTO> getAll();

}

@Service
class LeaveRequestServiceImpl implements LeaveRequestService {

	@Autowired
	LeaveRequestRepo leaveRequestRepo;
	
	@Autowired
	LeaveTypeRepo leaveTypeRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	ApplicationProperties props;

	@Autowired
	private UserRepo userRepo;

	private static final String ENTITY_NAME = "isttLeaveRequestType";

	@Override
	public LeaveRequestDTO create( MailRequestDTO mailRequestDTO) {
		try {
			LeaveRequestDTO leaveRequestDTO = mailRequestDTO.getLeaveRequestDTO();
			ModelMapper mapper = new ModelMapper();
			LeaveRequest leaveRequest = mapper.map(leaveRequestDTO, LeaveRequest.class);
			leaveRequest.setLeaveqequestId(UUID.randomUUID().toString().replaceAll("-", ""));

			if (leaveRequestDTO.getEmployee().getEmployeeId() != null) { // map employee
				Employee employee = employeeRepo.findById(leaveRequestDTO.getEmployee().getEmployeeId())
						.orElseThrow(NoResultException::new);
				leaveRequest.setEmployee(employee);

				if (leaveRequestDTO.getLeavetype().getLeavetypeId() == null)
					throw new BadRequestAlertException("Bad request: Missing LeaveRequestId", ENTITY_NAME, "Missing");

				LeaveType leaveType = leaveTypeRepo.findByLeavetypeId(leaveRequestDTO.getLeavetype().getLeavetypeId())
						.orElseThrow(NoResultException::new);

				if (leaveType.isSpecialType()) {
					leaveRequest
							.setStatus(props.getSTATUS_LEAVER_REQUEST().get(StatusLeaveRequestRef.APPROVED.ordinal()));
				} else {
					leaveRequest.setStatus(
							props.getSTATUS_LEAVER_REQUEST().get(StatusLeaveRequestRef.NOT_APPROVED.ordinal()));
				}

//				Handle send mail:
				List<Employee> employeesRaw = employeeRepo
						.findAllById(employee.getEmployeeDependence());
				System.out.println("level: " + employee.getLevels().stream().collect(Collectors.toList()).getClass());

				if (employeesRaw.isEmpty())
					throw new BadRequestAlertException("Bad request: Not found employee in employee` department",
							ENTITY_NAME, "Not found");
//				List employees = employeesRaw.get().stream().filter(e -> Integer.valueOf(e.getLevels().stream().sorted())).collect(Collectors.toList());
				List<Employee> employees= new ArrayList<Employee>();

//				for (Employee e : employeesRaw.get()) {
//					for (Level l : e.getLevels()) {
//						System.out.println(e.getEmail() + "-l: " + l.getLevelCode() + " " + l.getLevelName());
//					}
//				}

			} else {
				throw new BadRequestAlertException("Bad request: Employee not found!", ENTITY_NAME, "Not Found!");
			}

//			leaveRequestRepo.save(leaveRequest);
			return leaveRequestDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}

	}

	@Override
	public LeaveRequestDTO update(LeaveRequestDTO leaveRequestDTO) {
		LeaveRequest leaveRequest = leaveRequestRepo.findById(leaveRequestDTO.getLeaveqequestId()).get();
		if(leaveRequest!=null) {
			leaveRequest.setLeavetype(leaveRequestDTO.getLeavetype());
			leaveRequest.setRequestDate(leaveRequestDTO.getRequestDate());
			leaveRequest.setDuration(leaveRequestDTO.getDuration());
			leaveRequest.setReason(leaveRequestDTO.getReason());
		}
		leaveRequestRepo.save(leaveRequest);
		
		return leaveRequestDTO;
	}

	@Override
	public LeaveRequestDTO get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LeaveRequestDTO> getAll() {
		ModelMapper mapper = new ModelMapper();
		List<LeaveRequest> leaveRequests = leaveRequestRepo.findAll();
		return leaveRequests
				  .stream()
				  .map(leaverequest -> mapper.map(leaverequest, LeaveRequestDTO.class))
				  .collect(Collectors.toList());
	}

	@Override
	public Boolean delete(String id) {
		LeaveRequest leaveRequest = leaveRequestRepo.findById(id).get();
		if(leaveRequest!= null 
//				|| leaveRequest.getStatus()=="waiting"
				) {
			leaveRequestRepo.delete(leaveRequest);
			return true;
		}
		return false;
	}

	@Override
	public Boolean deleteALL(List<String> ids) {
		List<LeaveRequest> leaveRequests = leaveRequestRepo.findAllById(ids);
		if(leaveRequests.size()>0) {
			leaveRequestRepo.deleteAll(leaveRequests);
			return true;
		}
		return false;
	}

}
