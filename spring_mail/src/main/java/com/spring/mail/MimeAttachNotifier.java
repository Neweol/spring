package com.spring.mail;

import java.io.File;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.spring.command.MailRequestCommand;

public class MimeAttachNotifier {
	private JavaMailSender mailSender;
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendMail(MailRequestCommand command, String uploadPath) throws Exception{
		
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
		
		messageHelper.setTo(new InternetAddress(command.getReceiver()));
		
		messageHelper.setFrom(command.getSender(),"운영자");
		
		messageHelper.setSubject(command.getTitle());
		
		messageHelper.setText(command.getContent(),true);
		
		if(command.getFile() !=null && !command.getFile().isEmpty()) {
			String fileName = command.getFile().getOriginalFilename();
			DataSource dataSource = new FileDataSource(new File(uploadPath, fileName));
			messageHelper.addAttachment(MimeUtility.encodeText(fileName,"utf-8","B"), dataSource);
		}
		
		mailSender.send(message);
	}
	
}
