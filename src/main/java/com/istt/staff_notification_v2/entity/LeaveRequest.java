package com.istt.staff_notification_v2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "leaverequest")
@EqualsAndHashCode(callSuper = false)
public class LeaveRequest {

	@Id
	@Column(name = "leaveqequest_id", updatable = false, nullable = false)
	private String leaveqequestId;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "leavetype_id")
	private LeaveType leavetype;

//	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "request_date")
	private Date requestDate;
	
//	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "start_date")
	private Date startDate;
	
//	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "end_date")
	private Date endDate;

	private float duration;

	@Column(columnDefinition = "TEXT")
	private String reason;

	private String status;

	@Column(columnDefinition = "TEXT")
	private String anrreason;
	
	@Override
	public String toString() {
		return this.leaveqequestId.toString()+ "/employee:"+ this.employee.getEmployeeId().toString()
				+"/leavetype:"+this.leavetype.getLeavetypeId()+"/requestdate:"+this.requestDate.toString();
	}

}
