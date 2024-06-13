package com.istt.staff_notification_v2.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.istt.staff_notification_v2.entity.Department;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
	public void testFindByName() {
		Department department = new Department();
		department.setDepartmentId("string");
		department.setDepartmentName("string");
		
		Department department2 = departmentRepo.save(department);
		
		Department item = departmentRepo.findByDepartmentName("string").get();
		
		assertNotNull(item);
		assertEquals("string",item.getDepartmentName());
	}
	
	@Test
	public void testFindByID() {
		Department department = new Department();
		department.setDepartmentId("string");
		department.setDepartmentName("string");
		
		Department department2 = departmentRepo.save(department);
		
		Department item = departmentRepo.findByDepartmentId("string").get();
		
		assertNotNull(item);
		assertEquals("string",item.getDepartmentId());
	}
	
	@Test
	public void testGetAll() {
		List<Department> list = departmentRepo.getAll().get();
//		for (Department department : list) {
//			System.err.println(department.toString());
//		}
		assertNotNull(list);
	}
	
}
