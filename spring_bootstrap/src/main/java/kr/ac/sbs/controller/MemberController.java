package kr.ac.sbs.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsp.command.SearchCriteria;
import com.jsp.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	@GetMapping("/main")
	public void main() { }
	
	@GetMapping("/list")
	public void list(SearchCriteria cri, HttpServletRequest request) throws Exception{
		Map<String, Object> dataMap = memberService.getMemberListForPage(cri);
		request.setAttribute("dataMap", dataMap);
	}
	
	
	
	
}
