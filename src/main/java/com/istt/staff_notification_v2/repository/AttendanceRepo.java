package com.istt.staff_notification_v2.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.entity.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, String>{
	@Query("Select e from Attendance e")
	List<Attendance> getAll();
}
