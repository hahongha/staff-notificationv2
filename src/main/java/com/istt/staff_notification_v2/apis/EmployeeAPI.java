package com.istt.staff_notification_v2.apis;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

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
import com.istt.staff_notification_v2.dto.EmployeeDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.service.EmailValidation;
import com.istt.staff_notification_v2.service.EmployeeService;
import com.istt.staff_notification_v2.service.UserService;

@RestController
@RequestMapping("/employee")
public class EmployeeAPI {
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private UserService userService;

	private static final String ENTITY_NAME = "isttEmployee";

	@PostMapping("")
	public ResponseDTO<EmployeeDTO> create(@RequestBody @Valid EmployeeDTO employeeDTO) throws URISyntaxException {
		System.out.println("email: " + employeeDTO.getEmail());
		if (employeeDTO.getEmail() == null || employeeDTO.getFullname() == null
				|| employeeDTO.getDepartment() == null
				|| employeeDTO.getEmail().isEmpty() || employeeDTO.getFullname().isEmpty()
				|| employeeDTO.getPhone().isEmpty()||employeeDTO.getPhone() == null
				|| employeeDTO.getDateofbirth()== null
				||employeeDTO.getHiredate()== null
				||employeeDTO.getDepartment() == null
				
				) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing");
		}
		
		if(!EmailValidation.patternMatches(employeeDTO.getEmail()))
			throw new BadRequestAlertException("Bad request: email", ENTITY_NAME, "email");
		
		if(employeeDTO.getOffdate()!= null && employeeDTO.getHiredate().after(employeeDTO.getOffdate()))
			throw new BadRequestAlertException("Bad request: hire date is before offdate", ENTITY_NAME, "date");
		
		employeeService.create(employeeDTO);
		return ResponseDTO.<EmployeeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(employeeDTO).build();
	}

	@DeleteMapping("/deletebyList")
	public ResponseDTO<Void> delete(@RequestBody @Valid List<String> ids) throws URISyntaxException {
		if (ids == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		employeeService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		employeeService.delete(id);
		userService.deletebyEmployee(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseDTO<EmployeeDTO> get(@PathVariable(value = "id") String id) {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		return ResponseDTO.<EmployeeDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(employeeService.get(id)).build();
	}
	
	@GetMapping("/getAll")
    public List<EmployeeDTO> getAll() {
        return employeeService.getAllEmployee();
    }
	
	@PutMapping("")
	public ResponseDTO<EmployeeDTO> update(@RequestBody @Valid EmployeeDTO employeeDTO) throws URISyntaxException {
		System.out.println("email: " + employeeDTO.getEmail());
		if (employeeDTO.getEmail() == null || employeeDTO.getFullname() == null
				|| employeeDTO.getDepartment() == null
				|| employeeDTO.getEmail().isEmpty() || employeeDTO.getFullname().isEmpty()
				|| employeeDTO.getPhone().isEmpty()||employeeDTO.getPhone() == null
				|| employeeDTO.getDateofbirth()== null
				||employeeDTO.getHiredate()== null
				
				) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing");
		}
		if(employeeDTO.getOffdate()!= null && employeeDTO.getHiredate().after(employeeDTO.getOffdate()))
			throw new BadRequestAlertException("Bad request: hire date is before offdate", ENTITY_NAME, "date");
		

		employeeService.update(employeeDTO);
		return ResponseDTO.<EmployeeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(employeeDTO).build();
	}

}
