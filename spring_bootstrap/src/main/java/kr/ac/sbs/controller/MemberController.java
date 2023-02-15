package kr.ac.sbs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.action.utils.MakeFileName;
import com.jsp.command.SearchCriteria;
import com.jsp.dto.MemberVO;
import com.jsp.service.MemberService;

import kr.ac.sbs.command.MemberModifyCommand;
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
	public String regist(MemberRegistCommand memberReq,RedirectAttributes rttr) throws Exception{
		String url = "redirect:/member/list.do";
		
		MemberVO member = memberReq.toMemberVO();
		memberService.regist(member);
		
		rttr.addFlashAttribute("member",member);
		rttr.addAttribute("from","regist");
		
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
	
	@RequestMapping(value="/getPicture")
	public ResponseEntity<byte[]> getPicture(String id) throws Exception{
		MemberVO member = memberService.getMember(id);
		
		if(member==null) return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		
		String picture = member.getPicture();
		String imgPath = this.picturePath;
		
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		try {
			in = new FileInputStream(new File(imgPath, picture));
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),HttpStatus.OK);
		} finally {
			if(in!=null) in.close();
		}
		return entity;
	}
	
	@GetMapping("/detail")
	public void detail(String id, HttpServletRequest request) throws Exception{
		MemberVO member = memberService.getMember(id);
		request.setAttribute("member", member);
	}
	
	@GetMapping("/modifyForm")
	public String modify(String id, HttpServletRequest request) throws Exception{
		String url = "/member/modify";
		MemberVO member = memberService.getMember(id);
		String picture = MakeFileName.parseFileNameFromUUID(member.getPicture(), "\\$\\$");
		member.setPicture(picture);
		request.setAttribute("member", member);
		return url;
	}
	
	@PostMapping(value="/modify",produces="text/plain;charset=utf-8")
	public String modify(MemberModifyCommand memberReq, HttpSession session, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/member/detail.do";
		MemberVO member = memberReq.toMemberVO();
		String oldPicture = memberService.getMember(member.getId()).getPicture();
		if(memberReq.getPicture()!=null && memberReq.getPicture().getSize()>0) {
			String fileName = savePicture(oldPicture, memberReq.getPicture());
			member.setPicture(fileName);
		}else {
			member.setPicture(oldPicture);
		}
		
		memberService.modify(member);
		
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(loginUser != null && member.getId().equals(loginUser.getId())) {
			session.setAttribute("loginUser", memberService.getMember(member.getId()));
		}
		
		
		
		rttr.addAttribute("id",member.getId());
		rttr.addFlashAttribute("name",member.getName());
		
		
		return url;
	}
	
	@PostMapping("/remove")
	public String remove(String id,RedirectAttributes rttr, HttpSession session) throws Exception{
		String url = "redirect:/member/detail.do";
		
		MemberVO loginUser = (MemberVO)session.getAttribute("loginUser");
		MemberVO member = memberService.getMember(id);
		String savePath = this.picturePath;
		File imageFile = new File(savePath, member.getPicture());
		if(imageFile.exists()) {
			imageFile.delete();
		}
		
		memberService.remove(id);
		
		if(loginUser!=null && loginUser.getId().equals(member.getId())) {
			session.invalidate();
		}
		
		rttr.addFlashAttribute("removeMember",member);
		rttr.addAttribute("from","remove");
		rttr.addAttribute("id",id);
		
		return url;
	}
	
}
