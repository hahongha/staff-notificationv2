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
import com.istt.staff_notification_v2.dto.LeaveRequestDTO;
import com.istt.staff_notification_v2.dto.MailRequestDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.service.LeaveRequestService;

@RestController
@RequestMapping("/leaveRequest")
public class LeaveRequestAPI {
	@Autowired
	private LeaveRequestService leaveRequestService;

	private static final String ENTITY_NAME = "isttLeaveRequest";

	@PostMapping("")
	public ResponseDTO<MailRequestDTO> create(@RequestBody MailRequestDTO mailRequestDTO) throws URISyntaxException {
		if (mailRequestDTO.getLeaveRequestDTO().getRequestDate() == null
				|| mailRequestDTO.getLeaveRequestDTO().getReason() == null
				|| mailRequestDTO.getLeaveRequestDTO().getLeavetype() == null
				|| mailRequestDTO.getLeaveRequestDTO().getEmployee() == null
				|| mailRequestDTO.getLeaveRequestDTO().getEmployee().getEmail() == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_level");
		}
		leaveRequestService.create(mailRequestDTO);
		return ResponseDTO.<MailRequestDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(mailRequestDTO)
				.build();
	}
	
	@GetMapping("getAll")
    public List<LeaveRequestDTO> getAll() {
        return leaveRequestService.getAll();
    }
	
	@PutMapping("")
	public ResponseDTO<LeaveRequestDTO> update(@RequestBody LeaveRequestDTO leaveRequestDTO) throws URISyntaxException {		
		if(leaveRequestDTO.validRequest()) 
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing_level");
		leaveRequestService.update(leaveRequestDTO);
		return ResponseDTO.<LeaveRequestDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(leaveRequestDTO)
				.build();
	} 
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		leaveRequestService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	

}
