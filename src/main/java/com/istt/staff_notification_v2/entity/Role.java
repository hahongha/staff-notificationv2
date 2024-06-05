package com.istt.staff_notification_v2.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "role")
@EqualsAndHashCode(callSuper = false)
public class Role {

	@Id
	@Column(name = "role_id", updatable = false, nullable = false)
	private String roleId;

	private String role;

	@Column(columnDefinition = "TEXT")
	private String description;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private Set<User> users;
	
	protected void removeUser(User user) {
		this.users.remove(user);
	}

}
