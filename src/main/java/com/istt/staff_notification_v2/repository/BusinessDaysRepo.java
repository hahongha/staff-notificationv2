package com.istt.staff_notification_v2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.entity.Attendance;
import com.istt.staff_notification_v2.entity.BusinessDays;

@Repository
public interface BusinessDaysRepo  extends JpaRepository<BusinessDays, String> {
	@Query("Select u from BusinessDays u")
	List<BusinessDays> getAll();
	
	@Query("Select a from BusinessDays a where a.type = :x ")
	Page<BusinessDays> findByType(@Param("x") String value, Pageable pageable);
}
