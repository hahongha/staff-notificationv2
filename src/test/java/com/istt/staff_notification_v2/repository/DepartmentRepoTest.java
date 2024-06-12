package com.istt.staff_notification_v2.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.istt.staff_notification_v2.entity.Department;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DepartmentRepoTest {

	@Autowired
	private DepartmentRepo departmentRepo;

	@Test
	public void testFindOne() {
		Department department = new Department();
		department.setDepartmentId("string");
		department.setDepartmentName("string");
		
		Department department2 = departmentRepo.save(department);
		
		Department item = departmentRepo.findById("string").get();
		
		assertNotNull(item);
		assertEquals("string",item.getDepartmentName());
	}
	
	@Test
	public void testGetAll() {
		List<Department> list = departmentRepo.getAll().get();
		assertNotNull(list);
	}
}
