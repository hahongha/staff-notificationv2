package com.istt.staff_notification_v2.apis;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.EmployeeDTO;
import com.istt.staff_notification_v2.dto.EmployeeRelationshipResponse;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeAPI {
	@Autowired
	private EmployeeService employeeService;

	private static final String ENTITY_NAME = "isttEmployee";
	private static final Logger logger = LogManager.getLogger(EmployeeAPI.class);

	@PostMapping("")
	public ResponseDTO<EmployeeDTO> create(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid EmployeeDTO employeeDTO) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (employeeDTO.getEmail() == null || employeeDTO.getFullname() == null
				|| employeeDTO.getDepartment() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing");
		}

		employeeService.create(employeeDTO);
		return ResponseDTO.<EmployeeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(employeeDTO).build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "id") String id) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		employeeService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@DeleteMapping("/ids")
	public ResponseDTO<Void> deleteByList(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid List<String> ids) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (ids == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		employeeService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseDTO<EmployeeDTO> get(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "id") String id) {
		logger.info("create by :" + currentuser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		return ResponseDTO.<EmployeeDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(employeeService.get(id)).build();
	}

	@GetMapping("/dependence/{id}")
	public ResponseDTO<List<EmployeeDTO>> getEmployeeDependence(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "id") String id) {
		logger.info("create by :" + currentuser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		return ResponseDTO.<List<EmployeeDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(employeeService.getEmployeeDependence(id)).build();
	}

	@PutMapping("/")
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseDTO<EmployeeDTO> update(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid EmployeeDTO employeeDTO) throws IOException {
		logger.info("create by :" + currentuser.getUsername());
		employeeService.update(employeeDTO);
		return ResponseDTO.<EmployeeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(employeeDTO).build();

	}

	@PostMapping("/search")
//	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseDTO<List<EmployeeDTO>> search(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid SearchDTO searchDTO) {
		logger.info("create by :" + currentuser.getUsername());
		return employeeService.search(searchDTO);
	}

	@GetMapping("/employeeRelationship")
	public ResponseDTO<List<List<EmployeeRelationshipResponse>>> getEmployeeRelationship(@CurrentUser UserPrincipal currentuser) {
		logger.info("create by :" + currentuser.getUsername());
		return ResponseDTO.<List<List<EmployeeRelationshipResponse>>>builder()
				.code(String.valueOf(HttpStatus.OK.value())).data(employeeService.getEmployeeRelationship()).build();
	}

	@GetMapping("/test")
	public ResponseDTO<List<EmployeeDTO>> test() {
		return ResponseDTO.<List<EmployeeDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(employeeService.test()).build();
	}
}
