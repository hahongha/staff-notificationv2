package com.istt.staff_notification_v2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.repository.DepartmentRepo;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class DepartmentServiceTest {

	@Mock
	private DepartmentRepo departmentRepo;
	
	@InjectMocks
//	@MockBean
	private DepartmentServiceImpl departmentServiceImpl;
	
	private Department department;
	
	AutoCloseable autoCloseable;
	
//	@BeforeEach
//  public void setup(){
//		department = Department.builder()
//				.departmentId("string")
//				.departmentName("string").build();
//		given(departmentRepo.save(department)).willReturn(department);
//  }
	
	
	
	
	@Test
	@Transactional
	@Rollback(false)
	void DepartmentService_Create_ReturnsDepartmentDto() {
		Department department = Department.builder()
				.departmentId("string")
				.departmentName("string").build();
		DepartmentDTO departmentDTO = DepartmentDTO.builder()
				.departmentId("string")
				.departmentName("string").build();
		when(departmentRepo.save(Mockito.any(Department.class))).thenReturn(department);
		
        DepartmentDTO departmentDTO2 = departmentServiceImpl.create(departmentDTO);
        
        assertNotNull(departmentDTO2);
        
	}
	
	@Test
    public void DepartmentService_UpdateDepartment_ReturnDepartmentDto() {
		String id = "string";
		Department department = Department.builder()
				.departmentId("string")
				.departmentName("string").build();
		DepartmentDTO departmentDTO = DepartmentDTO.builder()
				.departmentId("string")
				.departmentName("string").build();
		
		when(departmentRepo.findById(id)).thenReturn(Optional.ofNullable(department));
		when(departmentRepo.findByDepartmentId(id)).thenReturn(Optional.ofNullable(department));
		when(departmentRepo.save(department)).thenReturn(department);
//        
        DepartmentDTO departmentDTO2 = departmentServiceImpl.update(departmentDTO);
        assertNotNull(departmentDTO2);
    }
	
	@Test
    public void DepartmentService_DeleteServiceById_ReturnTrue() {
		String id = "string";
		Department department = Department.builder()
				.departmentId("string")
				.departmentName("string").build();
		when(departmentRepo.findById(id)).thenReturn(Optional.ofNullable(department));
		when(departmentRepo.findByDepartmentId(id)).thenReturn(Optional.ofNullable(department));
		when(departmentRepo.save(department)).thenReturn(department);
//        
		doNothing().when(departmentRepo).delete(department);

        assertTrue(departmentServiceImpl.delete(id));
    }
	
	
}
