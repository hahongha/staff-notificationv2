package com.istt.staff_notification_v2.apis;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.LeaveRequestDTO;
import com.istt.staff_notification_v2.dto.MailRequestDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.ResponseLeaveRequest;
import com.istt.staff_notification_v2.dto.SearchLeaveRequest;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.LeaveRequestService;

@RestController
@RequestMapping("/leaveRequest")
public class LeaveRequestAPI {
	@Autowired
	private LeaveRequestService leaveRequestService;

	private static final String ENTITY_NAME = "isttLeaveRequest";
	private static final Logger logger = LogManager.getLogger(LeaveRequestAPI.class);

	@PostMapping("")
	public ResponseDTO<MailRequestDTO> create(@CurrentUser UserPrincipal currentuser,@RequestBody MailRequestDTO mailRequestDTO) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		if (mailRequestDTO.getLeaveRequestDTO().getRequestDate() == null
				|| mailRequestDTO.getLeaveRequestDTO().getReason() == null
				|| mailRequestDTO.getLeaveRequestDTO().getLeavetype() == null
				|| mailRequestDTO.getLeaveRequestDTO().getEmployee() == null
				|| mailRequestDTO.getLeaveRequestDTO().getEmployee().getEmail() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_level");
		}
		leaveRequestService.create(mailRequestDTO);
		return ResponseDTO.<MailRequestDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(mailRequestDTO)
				.build();
	}

	@PostMapping("/changeStatusLeaveRequest")
	public ResponseDTO<ResponseLeaveRequest> changeStatusLeaveRequest(@CurrentUser UserPrincipal currentuser,
			@RequestBody ResponseLeaveRequest responseLeaveRequest) throws URISyntaxException {
		logger.info("create by :" + currentuser.getUsername());
		
		if (responseLeaveRequest.getLeaveqequestId() == null || responseLeaveRequest.getStatus() == null) {
			logger.error("missing data");
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_data");
		}
		leaveRequestService.changeStatusLeaveRequest(responseLeaveRequest);
		return ResponseDTO.<ResponseLeaveRequest>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(responseLeaveRequest).build();
	}

	@PostMapping("/getLeaveRequest")
	public ResponseDTO<List<LeaveRequestDTO>> getLeaveRequest(@CurrentUser UserPrincipal currentuser,@RequestBody SearchLeaveRequest searchLeaveRequest) {
		logger.info("create by :" + currentuser.getUsername());
		return ResponseDTO.<List<LeaveRequestDTO>>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(leaveRequestService.searchLeaveRequest(searchLeaveRequest)).build();
	}

}
