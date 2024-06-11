package com.istt.staff_notification_v2.dto;

import java.util.Date;
import lombok.Data;

@Data
public class BusinessDaysDTO {
	private String bussinessdaysId;
	
	private Date date;
	
	private String type;
	
	private String description;
}
