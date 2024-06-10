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
import com.istt.staff_notification_v2.dto.LevelDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.LevelService;

@RestController
@RequestMapping("/level")
public class LevelAPI {
	@Autowired
	private LevelService levelService;

	private static final String ENTITY_NAME = "isttLevel";
	private static final Logger logger = LogManager.getLogger(LevelAPI.class);

	@PostMapping("")
//	@PreAuthorize("hasAuthority('ADMIN') and hasAuthority('USER')")
	public ResponseDTO<LevelDTO> create(@CurrentUser UserPrincipal currentuser,@RequestBody LevelDTO levelDTO) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (levelDTO.getLevelCode() == null || levelDTO.getLevelName() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_level");
		}
		levelService.create(levelDTO);
		return ResponseDTO.<LevelDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(levelDTO).build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@CurrentUser UserPrincipal currentUser, @PathVariable(value = "id") String id)
			throws URISyntaxException {
		logger.info("create by :" + currentUser.getUsername());
		if (id == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_id");
		}
		levelService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<LevelDTO>> search(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid SearchDTO searchDTO) {
		logger.info("create by :" + currentuser.getUsername());
		return levelService.search(searchDTO);
	}

	@DeleteMapping("/ids")
	public ResponseDTO<List<String>> deletebyListId(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid List<String> ids) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (ids.isEmpty()) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing levels", ENTITY_NAME, "missing_levels");
		}
		levelService.deleteAllbyIds(ids);
		return ResponseDTO.<List<String>>builder().code(String.valueOf(HttpStatus.OK.value())).data(ids).build();
	}

	@PutMapping("/")
	public ResponseDTO<LevelDTO> update(@CurrentUser UserPrincipal currentuser,@RequestBody @Valid LevelDTO levelDTO) throws IOException {
		logger.error("missing data");
		levelService.update(levelDTO);
		return ResponseDTO.<LevelDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(levelDTO).build();

	}

	@GetMapping("/all")
	public ResponseDTO<List<LevelDTO>> getAll(@CurrentUser UserPrincipal currentuser) throws IOException {
		logger.info("create by :" + currentuser.getUsername());
		return ResponseDTO.<List<LevelDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(levelService.getAll()).build();
	}

}
