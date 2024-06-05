package com.istt.staff_notification_v2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.entity.Role;
import com.istt.staff_notification_v2.entity.User;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {

	Optional<Role> findByRoleId(String roleId);
	
	@Query("SELECT e FROM Role e ")
	List<Role> findAll();
	
	List<Role> findByUsers(@Param("x")User user);
	
	Optional<Role> findByRole(String role);

}
