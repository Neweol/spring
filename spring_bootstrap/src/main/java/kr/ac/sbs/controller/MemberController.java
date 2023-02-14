package kr.ac.sbs.controller;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.action.utils.MakeFileName;
import com.jsp.command.SearchCriteria;
import com.jsp.dto.MemberVO;
import com.jsp.service.MemberService;

import kr.ac.sbs.command.MemberRegistCommand;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	@GetMapping("/main")
	public void main() {}
	
	@GetMapping("/list")
	public void list(SearchCriteria cri, HttpServletRequest request) throws Exception{
		Map<String, Object> dataMap = memberService.getMemberListForPage(cri);
		request.setAttribute("dataMap", dataMap);
	}
	
	@GetMapping("/registForm")
	public String registForm() {
		String url = "/member/regist";
		return url;
	}
	
	@PostMapping("/regist")
	public String regist(MemberRegistCommand memberReq) throws Exception{
		String url = "/member/regist_success";
		
		MemberVO member = memberReq.toMemberVO();
		memberService.regist(member);
		
		return url;
	}
	
	@Resource(name="picturePath")
	private String picturePath;
	
	public String savePicture(String oldPicture, MultipartFile multi) throws Exception{
		String fileName = null;
		String uploadPath = this.picturePath;
		if(!(multi==null||multi.isEmpty()||multi.getSize()>1024*1024*1)) {
			fileName = MakeFileName.toUUIDFileName(multi.getOriginalFilename(),"$$");
			File storeFile = new File(uploadPath, fileName);
			
			storeFile.mkdirs();
			
			multi.transferTo(storeFile);
		}
		if(oldPicture != null && !oldPicture.isEmpty()) {
			File oldFile = new File(uploadPath, oldPicture);
			if(oldFile.exists()) {
				oldFile.delete();
			}
		}
		return fileName;
	}
	
	@PostMapping(value="/picture", produces="text/plain;charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> pictureUpload(@RequestParam("pictureFile")MultipartFile multi, String oldPicture) throws Exception{
		ResponseEntity<String> entity = null;
		String result = "";
		HttpStatus status = null;
		
		if((result = savePicture(oldPicture, multi))==null) {
			result = "파일이 없습니다";
			status = HttpStatus.BAD_REQUEST;
		}else {
			status = HttpStatus.OK;
		}
		
		entity = new ResponseEntity<String>(result, status);
		return entity;
	}
	
	@PostMapping("/idCheck")
	@ResponseBody
	public ResponseEntity<String> checkID(String id) throws Exception{
		ResponseEntity<String> entity = null;
		MemberVO check = null;
		String result = null;
		check = memberService.getMember(id);
		
		if(check == null) {//중복아이디 없음
			result = "DUPLICATED";			
		}else {
			result = "";
		}
		entity = new ResponseEntity<String>(result, HttpStatus.OK);
		return entity;
	}
	
}
