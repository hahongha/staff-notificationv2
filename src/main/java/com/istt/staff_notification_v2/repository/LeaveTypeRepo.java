package com.istt.staff_notification_v2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.LeaveType;

public interface LeaveTypeRepo extends JpaRepository<LeaveType, String> {
	
	
	Optional<LeaveType> findByLeavetypeId(String leavetypeId);

	Optional<LeaveType> findByLeavetypeName(String leavetypeName);
	
	@Query("SELECT e FROM LeaveType e ")
	List<LeaveType> findAll();

}
