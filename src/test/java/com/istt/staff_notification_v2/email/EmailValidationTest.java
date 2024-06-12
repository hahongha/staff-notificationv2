package com.istt.staff_notification_v2.email;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.istt.staff_notification_v2.configuration.EmailValidation;

@ExtendWith(SpringExtension.class) // tich hop spring và junit
public class EmailValidationTest {
	    @Test
	    public void testEmailWithDomain() {
	        String emailAddress = "username@gmail.com";
	        assertTrue(EmailValidation.patternMatches(emailAddress));
	    }
	    
	    @Test
	    public void testEmailFalse() {
	        String emailAddress = "username@email.com";
	        assertTrue(EmailValidation.patternMatches(emailAddress));
	    }
	
}

