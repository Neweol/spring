package kr.ac.sbs.dao;

import java.sql.SQLException;
import java.util.Map;

public interface LoginUserLogDAO {
	
	public void insertLoginUserLog(Map<String,Object> logVO) throws SQLException;
	public void deleteLoginUserLog() throws SQLException;
	
	
	
	
}
