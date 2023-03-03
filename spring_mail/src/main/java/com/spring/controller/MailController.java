package com.spring.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.command.MailRequestCommand;
import com.spring.mail.MimeAttachNotifier;

@Controller
public class MailController {
	
	@Autowired
	private MimeAttachNotifier notifier;
	
	@GetMapping("/toMail")
	public void mailGet() throws Exception{}
	
	@PostMapping(value="/sendMail", produces="text/plain;charset=utf-8")
	public String mailPost(MailRequestCommand mailReq, HttpServletRequest request, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/mail/success";
		MultipartFile attach = mailReq.getFile();
		String uploadPath = request.getSession().getServletContext().getRealPath("resources/mail_attach");
		String message = null;
		
		if(attach!=null && !attach.isEmpty()) {
			if(attach.getSize()<1024*1024*5) {
				File file = new File(uploadPath,attach.getOriginalFilename());
				file.mkdirs();
				attach.transferTo(file);
			} else {
				message = "허용용량 초과입니다.";
				url = "redirect:/mail/fail";
				rttr.addFlashAttribute("message",message);
				
				return url;
			}
		}
		try {
			notifier.sendMail(mailReq, uploadPath);
			rttr.addFlashAttribute("mailRequest",mailReq);
		} catch(Exception e) {
			e.printStackTrace();
			url="redirect:/mail/fail";
			rttr.addFlashAttribute("message","메일 보내기 싯빠이데시타..");
		}
		return url;
	}
	
	@GetMapping("/mail/success")
	public void mailSuccess() {}
	
	@GetMapping("/mail/fail")
	public void mailFail() {}

}
