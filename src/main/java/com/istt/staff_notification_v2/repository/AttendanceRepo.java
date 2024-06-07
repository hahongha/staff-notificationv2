package com.istt.staff_notification_v2.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.entity.Attendance;
import com.istt.staff_notification_v2.entity.User;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, String>{
	@Query("Select e from Attendance e")
	List<Attendance> getAll();
	
//	@Query("Select e from Attendance e where e.status = :x ")
	Page<Attendance> findByStatus(@Param("x") String value, Pageable pageable);
	
	@Query("Select e from Attendance e where e.employee.fullname like :x ")
	Page<Attendance> searchByEmployeeName(@Param("x") String value, Pageable pageable);
	
	
	@Query("Select e from Attendance e where e.status = :x ")
	List<Attendance> getStatus(@Param("x") String value);
	
//	@Query("SELECT E FROM Attendance e where e.employee.employeeName like ")
}
