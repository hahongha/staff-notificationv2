//package com.istt.staff_notification_v2.service;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.Test;
//
//public class EmailValidationTest {
//	    @Test
//	    public void testEmailWithDomain() {
//	        String emailAddress = "username@domain.com";
//	        String validatedEmail = EmailValidation.addDefaultDomainIfNeeded(emailAddress);
//	        assertTrue(EmailValidation.patternMatches(validatedEmail));
//	    }
//
//	    @Test
//	    public void testEmailWithoutDomain() {
//	        String emailAddress = "username";
//	        String expectedEmail = "username@example.com";
//	        String validatedEmail = EmailValidation.addDefaultDomainIfNeeded(emailAddress);
//	        assertEquals(expectedEmail, validatedEmail);
//	        assertTrue(EmailValidation.patternMatches(validatedEmail));
//	    }
//	
//}
//
