package com.istt.staff_notification_v2.apis;

import java.net.URISyntaxException;
import java.util.List;

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
import com.istt.staff_notification_v2.dto.RoleDTO;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleAPI {
	@Autowired
	private RoleService roleService;

	private static final String ENTITY_NAME = "isttRole";

	@PostMapping("")
//	@PreAuthorize("hasAuthority('ADMIN') and hasAuthority('USER')")
	public ResponseDTO<RoleDTO> create(@RequestBody RoleDTO roleDTO) throws URISyntaxException {

		if (roleDTO.getRole() == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_role");
		}
		roleService.create(roleDTO);
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(roleDTO).build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentUser
			, @PathVariable(value = "id") String id)
			throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing data",
					ENTITY_NAME, "missing_id");
		}
		roleService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@DeleteMapping("/deleterole/{id}")
	public ResponseDTO<Void> deleteRole(@PathVariable(value = "id") String id)
			throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_id");
		}
		roleService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("")
//	@PreAuthorize("hasAuthority('ADMIN') and hasAuthority('USER')")
	public ResponseDTO<RoleDTO> update(@RequestBody RoleDTO roleDTO) throws URISyntaxException {

		if (roleDTO.getRole() == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_role");
		}
		roleService.update(roleDTO);
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(roleDTO).build();
	}
	
	@GetMapping("getRole")
    public List<RoleDTO> getAll() {
        return roleService.getRole();
    }
}
