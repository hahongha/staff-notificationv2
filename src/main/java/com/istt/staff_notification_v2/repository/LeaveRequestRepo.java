package com.istt.staff_notification_v2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.istt.staff_notification_v2.entity.LeaveRequest;

public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, String> {

	@Query("SELECT e FROM LeaveRequest e ")
	List<LeaveRequest> findAll();
}
