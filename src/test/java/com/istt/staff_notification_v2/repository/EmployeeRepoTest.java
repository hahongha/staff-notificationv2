package com.istt.staff_notification_v2.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.istt.staff_notification_v2.entity.Employee;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepoTest {

	@BeforeEach
	void setup() {
		Employee employee = new Employee();
		
	}
	@Test
	void testEmployeeOne() {
		fail("Not yet implemented");
	}

}
