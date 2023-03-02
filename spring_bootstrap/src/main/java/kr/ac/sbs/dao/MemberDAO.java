package kr.ac.sbs.dao;

import java.sql.SQLException;

import com.jsp.dto.MemberVO;

public interface MemberDAO extends com.spring.dao.MemberDAO{
	
	MemberVO selectMemberByPicture(String picture) throws SQLException;
	
	
}
