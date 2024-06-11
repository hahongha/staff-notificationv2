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
import com.istt.staff_notification_v2.dto.AttendanceDTO;
import com.istt.staff_notification_v2.dto.BusinessDaysDTO;
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.BusinessDaysService;

@RestController
@RequestMapping("/businessdays")
public class BusinessDaysAPI {
	@Autowired
	private BusinessDaysService businessDaysService;

	private static final String ENTITY_NAME = "isttBusinessDays";
	private static final Logger logger = LogManager.getLogger(BusinessDaysAPI.class);

	@PostMapping("")
	public ResponseDTO<BusinessDaysDTO> create(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid BusinessDaysDTO businessDaysDTO)
			throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if ( businessDaysDTO.getStartdate() == null
				|| businessDaysDTO.getType()== null
				||businessDaysDTO.getEnddate() == null
				) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missin_name");
		}
		businessDaysService.create(businessDaysDTO);
		return ResponseDTO.<BusinessDaysDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(businessDaysDTO)
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentuser,@PathVariable(value = "id") String id) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		businessDaysService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

//	@PostMapping("/search")
//	public ResponseDTO<List<BusinessDaysDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
////		return departmentService.search(searchDTO);
//		return null;
//	}

	@DeleteMapping("/ids")
	public ResponseDTO<List<String>> deletebyListId(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid List<String> ids) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (ids.isEmpty()) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing departments", ENTITY_NAME, "missing_departments");
		}
		businessDaysService.deleteByListId(ids);
		return ResponseDTO.<List<String>>builder().code(String.valueOf(HttpStatus.OK.value())).data(ids).build();
	}

	@PutMapping("/")
	public ResponseDTO<BusinessDaysDTO> update(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid BusinessDaysDTO businessDaysDTO) throws IOException {
		logger.info("create by :" + currentuser.getUsername());
		businessDaysService.update(businessDaysDTO);
		return ResponseDTO.<BusinessDaysDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(businessDaysDTO)
				.build();

	}

	@GetMapping("/all")
	public ResponseDTO<List<BusinessDaysDTO>> getAll() throws IOException {
		return ResponseDTO.<List<BusinessDaysDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(businessDaysService.getAll()).build();
	}
	
	@PostMapping("/searchByType")
	public ResponseDTO<List<BusinessDaysDTO>> search(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid SearchDTO searchDTO) {
		logger.info("Create by"+ currentuser.getUsername());
		return businessDaysService.searchByType(searchDTO);
	}
	
	
}
