package com.istt.staff_notification_v2.apis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.istt.staff_notification_v2.dto.EmployeeDTO;
import com.istt.staff_notification_v2.dto.MailRequestDTO;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.repository.EmployeeRepo;
import com.istt.staff_notification_v2.security.securityv2.CurrentUser;
import com.istt.staff_notification_v2.security.securityv2.UserPrincipal;
import com.istt.staff_notification_v2.service.MailService;

@RestController
@RequestMapping("/email")
public class MailAPI {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	private static final Logger logger = LogManager.getLogger(MailAPI.class);

	@PostMapping("")
	public ResponseEntity<String> sendNotification(@CurrentUser UserPrincipal currentuser,@RequestBody MailRequestDTO mailRequestDTO) {
		logger.info("create by :" + currentuser.getUsername());
		try {
//			System.err.println(mailRequestDTO.getRecceiverList().size());
//			for (int i = 0; i < mailRequestDTO.getRecceiverList().size(); i++) {
//				EmployeeDTO receiver = new EmployeeDTO();
//				System.err.println(receiver.getFullname());
//				receiver = mailRequestDTO.getRecceiverList().get(i);
//				mailService.sendEmail(mailRequestDTO.getLeaveRequestDTO(), receiver, mailRequestDTO.getSubject());
//			}
			Employee employee = employeeRepo.findById("a8a2b006c8104988b806ae2826f560d9").get();
			System.err.println(employee.getEmployeeId());
			EmployeeDTO employeeDTO = new ModelMapper().map(employee, EmployeeDTO.class);
			mailService.sendEmail(mailRequestDTO.getLeaveRequestDTO(),employeeDTO , mailRequestDTO.getSubject());
			return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");

		} catch (Exception e) {
			logger.error(e.getMessage());
			System.err.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");

		}

	}

}
