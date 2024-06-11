package com.istt.staff_notification_v2.apis;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.AttendanceDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.AttendanceService;
import com.istt.staff_notification_v2.service.UserService;

@RestController
@RequestMapping("/attendance")
public class AttendanceAPI {
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private UserService userService;

	private static final String ENTITY_NAME = "isttAttendance";
	
	private static final Logger logger = LogManager.getLogger(AttendanceAPI.class);

	@PostMapping("")
	public ResponseDTO<AttendanceDTO> create(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid AttendanceDTO attendanceDTO)
			throws URISyntaxException {
//		String employeeId = userService.getEmployeebyUserName(currentuser.getUsername()).getEmployeeId();
		logger.info("Create by"+ currentuser.getUsername());
//		System.err.println(currentuser.getUsername());
		if (attendanceDTO.getStartDate() == null || attendanceDTO.getEndDate() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing");
		}
		if(attendanceDTO.getApprovedBy()==null) attendanceDTO.setApprovedBy(currentuser.getUser_id());
//		attendanceDTO.setUpdateBy(currentuser.getUser_id().toString());
		
		attendanceService.create(attendanceDTO);
		logger.info("create successfull");
		return ResponseDTO.<AttendanceDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(attendanceDTO)
				.build();
	}

	@DeleteMapping("/deletebyList")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid List<String> ids) throws URISyntaxException {
		logger.info("Create by"+ currentuser.getUsername());
		if (ids == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		attendanceService.deletebylistId(ids);
		logger.info("delete successfull");
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("delete/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal userPrincipal,@PathVariable(value = "id") String id) throws URISyntaxException {
		logger.info("Create by"+ userPrincipal.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		attendanceService.deleteById(id);
		logger.info("delete successfull");
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseDTO<AttendanceDTO> get(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "id") String id) {
		logger.info("Create by"+ currentuser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		return ResponseDTO.<AttendanceDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(attendanceService.get(id)).build();
	}

	@GetMapping("/type/{type}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseDTO<List<AttendanceDTO>> getStatus(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "type") String type) {
		logger.info("Create by"+ currentuser.getUsername());
		if (type == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing status", ENTITY_NAME, "missing_status");
		}
		return ResponseDTO.<List<AttendanceDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(attendanceService.getStatus(type)).build();
	}

	@PostMapping("/searchByType")
	public ResponseDTO<List<AttendanceDTO>> search(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid SearchDTO searchDTO) {
		logger.info("Create by"+ currentuser.getUsername());
		return attendanceService.search(searchDTO);
	}

	@PostMapping("/searchByEmployeeName")
	public ResponseDTO<List<AttendanceDTO>> searchbyName(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid SearchDTO searchDTO) {
		logger.info("Create by"+ currentuser.getUsername());
		return attendanceService.searchByEmployeeName(searchDTO);
	}

	@PutMapping("")
	public ResponseDTO<AttendanceDTO> update(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid AttendanceDTO attendanceDTO)
			throws URISyntaxException {
		logger.info("Create by"+ currentuser.getUsername());
		if (attendanceDTO.getAttendanceId() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing");
		}

		attendanceService.update(attendanceDTO);
		logger.info("update successfull");
		return ResponseDTO.<AttendanceDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(attendanceDTO)
				.build();
	}

	@GetMapping("/getAll")
	public List<AttendanceDTO> getAll() {
		return attendanceService.getAll();
	}

}
