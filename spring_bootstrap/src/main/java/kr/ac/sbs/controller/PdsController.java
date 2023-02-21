package kr.ac.sbs.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.action.utils.MakeFileName;
import com.jsp.command.SearchCriteria;
import com.jsp.dto.AttachVO;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;

import kr.ac.sbs.command.PdsRegistCommand;

@Controller
@RequestMapping("/pds")
public class PdsController {
   
   @Resource(name="pdsService")
   private PdsService pdsService;
   
   @GetMapping("/main")
   public void main() throws Exception {}
   
   @RequestMapping("/list")
   public ModelAndView list(SearchCriteria cri, ModelAndView mnv) throws Exception {
      
      String url = "/pds/list";
      
      Map<String, Object> dataMap = pdsService.getList(cri);
      
      mnv.addObject("dataMap", dataMap);
      mnv.setViewName(url);
      
      return mnv;
      
   }
   
   @RequestMapping("/registForm")
   public String registForm() throws Exception {
      
      String url = "pds/regist";
      
      return url;
      
   }
   
   @Resource(name="fileUploadPath")
   private String fileUploadPath;
   
   @PostMapping(value="/regist", produces="text/plain;charset=UTF-8")
   public String regist(PdsRegistCommand registReq, RedirectAttributes rttr) throws Exception {
      
      String url = "redirect:/pds/list.do";
      
      List<AttachVO> attachList = new ArrayList<AttachVO>();
      
      List<MultipartFile> multiFiles = registReq.getUploadFile();
      String savePath = this.fileUploadPath;
      
      if (multiFiles != null) {
         
         for (MultipartFile multi : multiFiles) {
            
            String fileName = MakeFileName.toUUIDFileName(multi.getOriginalFilename(), "$");
            File target = new File(savePath, fileName);
            
            target.mkdir();
            
            multi.transferTo(target);
            
            AttachVO attach = new AttachVO();
            attach.setUploadPath(savePath);
            attach.setFileName(fileName);
            attach.setFileType(fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase());
            
            attachList.add(attach);
            
         }
         
      }
      
      // DB
      PdsVO pds = registReq.toPdsVO();
      pds.setAttachList(attachList);
      //pdsService
      
      return url;
      
   }
   
   @GetMapping("/detail")
   public ModelAndView detail(int pno, String from, RedirectAttributes rttr, ModelAndView mnv) throws Exception{
	   String url = "/pds/detail";
	   PdsVO pds = null;
	   if (from != null && from.equals("list")) {
		   pds = pdsService.read(pno);
		   url = "redirect:/pds/detail.do";
		   
		   rttr.addAttribute("pno",pno);
		   mnv.setViewName(url);
		   return mnv;
	   }
	   
	   pds = pdsService.getPds(pno);
	   
	   if(pds!=null) {
		   List<AttachVO> attachList = pds.getAttachList();
		   if(attachList != null) {
			   for (AttachVO attach : attachList) {
				   String fileName = attach.getFileName().split("\\$\\$")[1];
				   attach.setFileName(fileName);
			   }
		   }
	   }
	   
	   mnv.addObject("pds",pds);
	   mnv.setViewName(url);
	   
	   return mnv;
   }
   
   @GetMapping("/modifyForm")
   public ModelAndView modifyForm(ModelAndView mnv, int pno,RedirectAttributes rttr) throws Exception {
	   String url = "/pds/modify";
	   
	   mnv = detail(pno,"modify", rttr, mnv);
	   mnv.setViewName(url);
	   
	   return mnv;
	   
   }
   
}