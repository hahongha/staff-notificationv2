package com.istt.staff_notification_v2.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.istt.staff_notification_v2.dto.EmployeeDTO;
import com.istt.staff_notification_v2.dto.LeaveRequestDTO;
import com.istt.staff_notification_v2.dto.MailDTO;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.repository.EmployeeRepo;

public interface MailService {
	void sendEmail(LeaveRequestDTO leaveRequestDTO, EmployeeDTO receiver, String subject);
}

@Service
class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	EmployeeRepo employeeRepo;

	@Override
	public void sendEmail(LeaveRequestDTO leaveRequestDTO, EmployeeDTO receiver, String subject) {
		MailDTO mailDTO = new MailDTO();

		try {
			Employee employeeSender = employeeRepo.findById(leaveRequestDTO.getEmployee().getEmployeeId()).get();
			String senderEmail = employeeSender.getEmail();
			String senderDepartment = employeeSender.getDepartment().getDepartmentName();
			String senderName = employeeSender.getFullname();
//			String senderLevel = "Master";
//			System.out.println(11);

			Employee employeeReceiver = employeeRepo
					.findByEmployeeIdOrEmail(receiver.getEmployeeId(), receiver.getEmail()).get();
			String receiverName = employeeReceiver.getFullname();
			String receiverEmail = employeeReceiver.getEmail();

			mailDTO.setSubject(subject);

			MimeMessage email = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(email, StandardCharsets.UTF_8.name());

			mailDTO.setContent(leaveRequestDTO.getReason());

			// Load template email with content
			Context context = new Context();
			context.setVariable("senderName", senderName);
			context.setVariable("senderEmail", senderEmail);
			context.setVariable("senderDepartment", senderDepartment);
			context.setVariable("reason", leaveRequestDTO.getReason());
			context.setVariable("startDate", leaveRequestDTO.getStartDate());
			context.setVariable("endDate", leaveRequestDTO.getEndDate());
			context.setVariable("receiverName", receiverName);
			System.out.println(22);

			String html = templateEngine.process("email", context);
			System.out.println(33);

			// Send email
			helper.setTo(receiverEmail);
			helper.setText(html, true);
			helper.setSubject(mailDTO.getSubject());
			helper.setFrom(senderEmail);

			javaMailSender.send(email);

		} catch (MessagingException e) {
			System.out.println("Email sent with error: " + e.getMessage());
		}
	}
}