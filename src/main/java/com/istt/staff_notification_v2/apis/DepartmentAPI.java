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
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentAPI {
	@Autowired
	private DepartmentService departmentService;

	private static final String ENTITY_NAME = "isttDepartment";

	@PostMapping("")
	public ResponseDTO<DepartmentDTO> create(@RequestBody @Valid DepartmentDTO departmentDTO)
			throws URISyntaxException {
		if (departmentDTO.getDepartmentName()==null || departmentDTO.getDepartmentName().isEmpty()) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missin_name");
		}
		departmentService.create(departmentDTO);
		return ResponseDTO.<DepartmentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(departmentDTO)
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		departmentService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("getDepartment")
    public List<DepartmentDTO> getAll() {
        return departmentService.getAllDepartment();
    }
	
	@PutMapping("/")
	public ResponseDTO<DepartmentDTO> update(@RequestBody @Valid DepartmentDTO departmentDTO)
			throws URISyntaxException {
		if (departmentDTO.getDepartmentName()==null || departmentDTO.getDepartmentName().isEmpty()) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missin_name");
		}
		departmentService.update(departmentDTO);
		return ResponseDTO.<DepartmentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(departmentDTO)
				.build();
	}
	
	@DeleteMapping("/byName/{name}")
	public ResponseDTO<Void> deletebyName(@PathVariable(value = "name") String name) throws URISyntaxException {
		if (name == null) {
			throw new BadRequestAlertException("Bad request: missing name", ENTITY_NAME, "missing_name");
		}
		departmentService.deleteAllbyName(name);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@DeleteMapping("/byListId")
	public ResponseDTO<Void> deletebyListId(@RequestBody @Valid List<String> ids) throws URISyntaxException {
		if (ids == null) {
			throw new BadRequestAlertException("Bad request: missing name", ENTITY_NAME, "missing_name");
		}
		departmentService.deleteAllByIds(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
}