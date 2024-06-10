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
import org.springframework.web.bind.annotation.RestController;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentAPI {
	@Autowired
	private DepartmentService departmentService;

	private static final String ENTITY_NAME = "isttDepartment";
	private static final Logger logger = LogManager.getLogger(DepartmentAPI.class);

	@PostMapping("")
	public ResponseDTO<DepartmentDTO> create(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid DepartmentDTO departmentDTO)
			throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (departmentDTO.getDepartmentName() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missin_name");
		}
		departmentService.create(departmentDTO);
		return ResponseDTO.<DepartmentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(departmentDTO)
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "id") String id) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		departmentService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<DepartmentDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return departmentService.search(searchDTO);
	}

	@DeleteMapping("/ids")
	public ResponseDTO<List<String>> deletebyListId(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid List<String> ids) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (ids.isEmpty()) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing departments", ENTITY_NAME, "missing_departments");
		}
		departmentService.deleteAllbyIds(ids);
		return ResponseDTO.<List<String>>builder().code(String.valueOf(HttpStatus.OK.value())).data(ids).build();
	}

	@PutMapping("/")
	public ResponseDTO<DepartmentDTO> update(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid DepartmentDTO departmentDTO) throws IOException {
		logger.info("create by :" + currentuser.getUsername());
		departmentService.update(departmentDTO);
		return ResponseDTO.<DepartmentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(departmentDTO)
				.build();

	}

	@GetMapping("/all")
	public ResponseDTO<List<DepartmentDTO>> getAll() throws IOException {
		return ResponseDTO.<List<DepartmentDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(departmentService.getAll()).build();
	}

}