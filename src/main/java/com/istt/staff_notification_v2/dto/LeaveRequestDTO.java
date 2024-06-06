package com.istt.staff_notification_v2.dto;

import java.util.Date;

import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.LeaveType;

import lombok.Data;

@Data
public class LeaveRequestDTO {
	private String leaveqequestId;

	private Employee employee;

	private LeaveType leavetype;

	private Date requestDate;

	private float duration;

	private String reason;

	private String status;

	private String anrreason;
	
	private Date startDate;
	
	private Date endDate;
	
	public boolean validRequest() {
		if(this.employee==null||this.leavetype== null|| this.requestDate==null
				||this.reason==null
				||this.reason.isEmpty()
				||this.startDate==null
				||this.endDate== null
				) return true;
		return false;
	}
}
