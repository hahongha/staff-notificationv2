package com.istt.staff_notification_v2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "department")
@EqualsAndHashCode(callSuper = false)

@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Department {
	@Id
	@Column(name = "department_id", updatable = false, nullable = false)
	private String departmentId;

	@Column(name = "department_name", nullable = false, unique = true)
	private String departmentName;

}
