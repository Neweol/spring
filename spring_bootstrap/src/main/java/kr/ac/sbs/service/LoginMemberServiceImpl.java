package kr.ac.sbs.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.dto.MemberVO;
import com.spring.dao.MemberDAO;

import kr.ac.sbs.exception.InvalidPasswordException;
import kr.ac.sbs.exception.NotFoundIdException;

@Service("loginMemberService")
public class LoginMemberServiceImpl extends com.spring.service.MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	public LoginMemberServiceImpl(MemberDAO memberDAO) {
		super.setMemberDAO(memberDAO);
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
