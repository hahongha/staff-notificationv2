package com.istt.staff_notification_v2.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance")
public class Attendance {
	@Id
	@Column(name = "attendance_id", updatable = false, nullable = false)
	private String attendanceId;
	
	private String status;
	
	private Date startDate;
	
	private Date endDate;
	
	private String createBy;
	
	private String updateBy;
	
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	@Column(columnDefinition = "TEXT")
	private String note;
	
	
	
}
