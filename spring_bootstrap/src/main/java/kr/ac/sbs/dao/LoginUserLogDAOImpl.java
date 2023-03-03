package kr.ac.sbs.dao;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class LoginUserLogDAOImpl implements LoginUserLogDAO{
	
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void insertLoginUserLog(Map<String, Object> logVO) throws SQLException {
		sqlSession.update("LoginUserLog-Mapper.insertLoginUserLog",logVO);
	}

	@Override
	public void deleteLoginUserLog() throws SQLException {
		sqlSession.update("LoginuserLog-Mapper.deleteLoginUserLog");
	}

}
