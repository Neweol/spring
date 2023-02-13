package com.spring.controller;


import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jsp.command.SearchCriteria;
import com.spring.command.MultipartCommand;

@Controller
public class MVCController {
	
	@GetMapping("/login")
	public void loginForm() {
	}
	
	@PostMapping("/login")
	public String loginPost(String id, String pwd) {
		String url = "/member/list";
		System.out.println(id);
		System.out.println(pwd);
		return url;
	}
	
	@GetMapping("/member/list")
	public void memberGet(SearchCriteria cri) {
		System.out.println();
		
	}
	
	@GetMapping("/fileUploadForm")
	public void fileUpload() {}
	
	@PostMapping(value="multipartFile", produces="text/plain;charset=utf-8")
	public String uploadByMultipartFile(String title, MultipartFile file,HttpServletRequest request) throws Exception {
		String url = "fileUploaded";
		
		String uploadPath = "c:\\upload\\file";
		String fileName = file.getOriginalFilename();
		File dest = new File(uploadPath, fileName);
		dest.mkdirs();
		
		file.transferTo(dest);
		
		
		request.setAttribute("title", title);
		request.setAttribute("originalFileName", file.getOriginalFilename());
		request.setAttribute("uploadedFileName", dest.getName());
		request.setAttribute("uploadPath", dest.getAbsoluteFile());
		request.setAttribute("fileName", file.getName());
		
		return url;
	}
	
	
	@PostMapping(value="/multipartHttpServletRequest", produces="text/plain;charset=utf-8")
	public String uploadByMultipartHttpServletRequest(MultipartHttpServletRequest request)throws Exception{
		String title = request.getParameter("title");
		MultipartFile multi= request.getFile("file");
		
		return uploadByMultipartFile(title,multi,request);
	}
	

	@PostMapping(value="/commandObject", produces="text/plain;charset=utf-8")
	public String uploadByCommandObject(MultipartCommand command,MultipartHttpServletRequest request)throws Exception{

		MultipartFile multi = command.getFile();
		String title = command.getTitle();
		
		return uploadByMultipartFile(title,multi,request);
	}
	
	

}
