package com.istt.staff_notification_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.istt.staff_notification_v2.entity.Attendance;

public interface AttendanceRepo extends JpaRepository<Attendance, String> {

}
