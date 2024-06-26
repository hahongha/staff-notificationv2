package com.istt.staff_notification_v2.dto;

import java.util.Date;

import com.istt.staff_notification_v2.entity.Employee;

import lombok.Data;

@Data
public class AttendanceDTO {
	private String attendanceId;
	
	private Employee employee;
	
	private String status;
	
	private Date startDate;
	
	private Date endDate;
	
	private String createBy;
	
	private String updateBy;
	
	private String note;
}
