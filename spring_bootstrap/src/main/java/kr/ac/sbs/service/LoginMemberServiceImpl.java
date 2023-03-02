package kr.ac.sbs.service;

import java.sql.SQLException;

import com.jsp.dto.MemberVO;
import com.spring.dao.MemberDAO;

import kr.ac.sbs.exception.InvalidPasswordException;
import kr.ac.sbs.exception.NotFoundIdException;


public class LoginMemberServiceImpl extends com.spring.service.MemberServiceImpl implements MemberService {
	
	private MemberDAO memberDAO;
	public void setChildMemberDAO(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}


	
//	private Logger logger = LoggerFactory.getLogger(LoginMemberServiceImpl.class);
//	{
//		logger.debug("DEBUG : message");
//		logger.info("INFO : message");
//		logger.warn("WARN : message");
//		logger.error("ERROR : message");
//	}
	
	@Override
	public void login(String id, String pwd) throws NotFoundIdException, InvalidPasswordException, SQLException {
		MemberVO member = memberDAO.selectMemberById(id);
		if(member == null) throw new NotFoundIdException();
		if(!pwd.equals(member.getPwd())) throw new InvalidPasswordException();
	}

	
	
	
	
}
