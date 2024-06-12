package com.istt.staff_notification_v2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.repository.DepartmentRepo;
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

	@Mock
	private DepartmentRepo departmentRepo;
	
	@InjectMocks
	private DepartmentServiceImpl departmentServiceImpl;
	
	
	@Test
	void DepartmentService_Create_ReturnsDepartmentDto() {
		Department department = Department.builder()
				.departmentId("string")
				.departmentName("string").build();
		DepartmentDTO departmentDTO = DepartmentDTO.builder()
				.departmentId("string")
				.departmentName("string").build();
		when(departmentRepo.save(Mockito.any(Department.class))).thenReturn(department);
		
        DepartmentDTO departmentDTO2 = departmentServiceImpl.create(departmentDTO);        
        System.err.println(departmentDTO2.toString());
//        List<Department> list = departmentRepo.getAll().get();
//        System.err.println(list.size());
        
        List<DepartmentDTO> list = departmentServiceImpl.getAll();
        for (DepartmentDTO departmentDTO3 : list) {
			System.err.println(departmentDTO3);
		}
        assertNotNull(departmentDTO2);
        
	}

	@Test
    public void DepartmentService_UpdateDepartment_ReturnDepartmentDto() {
		String departmentId = "string";
		Department department = Department.builder()
				.departmentId("string")
				.departmentName("string").build();
		DepartmentDTO departmentDTO = DepartmentDTO.builder()
				.departmentId("string")
				.departmentName("string").build();
//		Department department2 = departmentRepo.save(department);
//		System.err.println(department2.toString());
//		when(departmentRepo.findById(departmentId)).thenReturn(Optional.ofNullable(department));
//		when(departmentRepo.save(department)).thenReturn(department);
//		
//		DepartmentDTO departmentDTO2 = departmentServiceImpl.update(departmentDTO);
//		assertNotNull(departmentDTO2);
    }
	
	
}
