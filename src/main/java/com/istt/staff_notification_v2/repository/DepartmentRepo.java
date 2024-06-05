package com.istt.staff_notification_v2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.LeaveType;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, String> {
//	Optional<Department> findByDepartmentId(@Param("x") String value);
//	
	//tim kiem theo ten
	@Query("SELECT e FROM Department e WHERE e.departmentName like %:x% ")
	List<Department> findByName(@Param("x") String value);
	
	@Query("SELECT e FROM Department e ")
	List<Department> findAll();
	
	Optional<Department> findByDepartmentName(String departmentName);
}
