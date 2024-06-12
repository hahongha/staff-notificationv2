package com.istt.staff_notification_v2.dto;

import lombok.Data;

@Data
public class MailDTO {
	private String from = "phamha03122003@gmail.com";
	private String to = "hahongha2003@gmail.com";
	private String toName;
	private String subject;
	private String content;

}