package com.istt.staff_notification_v2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DepartmentDTO {
	private String departmentId;

	private String departmentName;

}
