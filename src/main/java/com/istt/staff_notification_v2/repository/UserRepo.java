package com.istt.staff_notification_v2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.dto.UserDTO;
import com.istt.staff_notification_v2.entity.Employee;
import com.istt.staff_notification_v2.entity.Role;
import com.istt.staff_notification_v2.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.username LIKE :x ")
	Page<User> search(@Param("x") String value, Pageable pageable);

//	@Query("SELECT u FROM User u WHERE u.userId = :x ")
	@EntityGraph(attributePaths = { "roles", "employee" })
	Optional<User> findByUserId(String s);

	Optional<User> findByAccessToken(String accesstoken);

	Boolean existsByUsername(String username);
	
	Optional<User> findByEmployee(@Param("x")Employee employee);
	
	List<User> findByRoles(@Param("x")Role role);
	
	@Query("SELECT u FROM User u ")
	Page<User> getAll(@Param("x") String value, Pageable pageable);

}