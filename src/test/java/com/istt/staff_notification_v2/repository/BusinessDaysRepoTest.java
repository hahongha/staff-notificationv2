package com.istt.staff_notification_v2.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.istt.staff_notification_v2.entity.BusinessDays;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BusinessDaysRepoTest {

	@Autowired
	private BusinessDaysRepo BusinessDaysRepo;

	@Test
	public void testFindOne() {
		BusinessDays  BusinessDays = new BusinessDays();
		BusinessDays.setBussinessdaysId("string");
		BusinessDays.setDescription("string");
		BusinessDays.setStartdate(new Date());
		BusinessDays.setStartdate(new Date());
		BusinessDays.setType("HOLIDAYS");
		
		BusinessDays BusinessDays2 = BusinessDaysRepo.save(BusinessDays);
		
		BusinessDays item = BusinessDaysRepo.findById("string").get();
		
		assertNotNull(item);
	}
	
//	@Test
//	public void testFindByName() {
//		BusinessDays BusinessDays = new BusinessDays();
//		BusinessDays.setBussinessdaysId("string");
//		BusinessDays.setDescription("string");
//		BusinessDays.setStartdate(new Date());
//		BusinessDays.setStartdate(new Date());
//		
//		BusinessDays BusinessDays2 = BusinessDaysRepo.save(BusinessDays);
//		
//		BusinessDays item = BusinessDaysRepo.findByType("string").get();
//		
//		assertNotNull(item);
//		assertEquals("string",item.getBusinessDaysName());
//	}
	
//	@Test
//	public void testFindByID() {
//		BusinessDays BusinessDays = new BusinessDays();
//		BusinessDays.setBusinessDaysId("string");
//		BusinessDays.setBusinessDaysName("string");
//		
//		BusinessDays BusinessDays2 = BusinessDaysRepo.save(BusinessDays);
//		
//		BusinessDays item = BusinessDaysRepo.findByBusinessDaysId("string").get();
//		
//		assertNotNull(item);
//		assertEquals("string",item.getBusinessDaysId());
//	}
//	
//	@Test
//	public void testGetAll() {
//		List<BusinessDays> list = BusinessDaysRepo.getAll().get();
////		for (BusinessDays BusinessDays : list) {
////			System.err.println(BusinessDays.toString());
////		}
//		assertNotNull(list);
//	}
}
