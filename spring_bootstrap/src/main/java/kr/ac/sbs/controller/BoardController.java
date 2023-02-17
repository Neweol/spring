package kr.ac.sbs.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.command.SearchCriteria;
import com.jsp.dto.BoardVO;
import com.jsp.service.BoardService;
import com.jsp.service.ReplyService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Resource(name="boardService")
	private BoardService boardService;
	@Resource(name="replyService")
	private ReplyService replyService;
	
	@GetMapping("/main")
	public void main() {}
	
	@GetMapping("/list")
	public ModelAndView list(SearchCriteria cri, ModelAndView mnv) throws Exception {
		String url = "/list.do";
		Map<String,Object> dataMap = boardService.getBoardList(cri);
		mnv.addObject("dataMap", dataMap);
		mnv.setViewName(url);
		
		return mnv;
	}
	
	@GetMapping("/registForm")
	public String registForm() {
		String url = "/board/regist.do";
		return url;
	}
	
	@PostMapping("/regist")
	public String regist(BoardVO board, RedirectAttributes rttr) throws Exception {
		String url = "redirect:/board/list.do";
		boardService.regist(board);
		
		rttr.addFlashAttribute("from","regist");
		return url;
	}
	
	@GetMapping("/detail")
	public ModelAndView detail (@RequestParam(defaultValue="")String from,ModelAndView mnv, int bno,RedirectAttributes rttr) throws Exception{
		String url = "/board/detail.do";
		BoardVO board = null;
		if(from == "list") {
			board = boardService.getBoard(bno);
		}else {
			board = boardService.getBoardForModify(bno);
			url = "redirect:/board/detail.do";
			rttr.addAttribute("bno",bno);
		}
		mnv.addObject("board", board);
		mnv.setViewName(url);
		
		return mnv;
	}
	
	@GetMapping("/modifyForm")
	public ModelAndView modifyForm(int bno, ModelAndView mnv) throws Exception {
		String url = "/board/modify.do";
		BoardVO board = boardService.getBoard(bno);
		mnv.addObject("board",board);
		mnv.setViewName(url);
		return mnv;
	}
	
	@PostMapping("/modify")
	public String modify(BoardVO board, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/board/detail.do";
		boardService.modify(board);
		rttr.addAttribute("bno",board.getBno());
		rttr.addFlashAttribute("from","modify");
		
		return url;
	}
	
	@PostMapping("/remove")
	public String remove(int bno, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/board/detail.do";
		boardService.remove(bno);
		
		rttr.addFlashAttribute("from","remove");
		
		return url;
	}
}
