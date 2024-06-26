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
import com.istt.staff_notification_v2.dto.LevelDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.LevelService;

@RestController
@RequestMapping("/level")
public class LevelAPI {
	@Autowired
	private LevelService levelService;

	private static final String ENTITY_NAME = "isttLevel";

	@PostMapping("")
//	@PreAuthorize("hasAuthority('ADMIN') and hasAuthority('USER')")
	public ResponseDTO<LevelDTO> create(@RequestBody LevelDTO levelDTO) throws URISyntaxException {
		if (levelDTO.getLevelCode() == null || levelDTO.getLevelName() == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_level");
		}
		levelService.create(levelDTO);
		return ResponseDTO.<LevelDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(levelDTO).build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentUser, @PathVariable(value = "id") String id)
			throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_id");
		}
		levelService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/")
	public ResponseDTO<LevelDTO> update(@RequestBody @Valid LevelDTO levelDTO)
			throws URISyntaxException {
		if(levelDTO.getLevelCode()== null|| levelDTO.getLevelName()==null||levelDTO.getLevelName().isEmpty())
			throw new BadRequestAlertException("BadRequest:missing data", ENTITY_NAME, "missing_data");
		levelService.update(levelDTO);
		return ResponseDTO.<LevelDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(levelDTO)
				.build();
	}
	
	@GetMapping("getLevel")
    public List<LevelDTO> getAll() {
        return levelService.getAllLevel();
    }

}
