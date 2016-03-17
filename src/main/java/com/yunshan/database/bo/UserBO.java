package com.yunshan.database.bo;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yunshan.database.ConnectionFactory;
import com.yunshan.database.dao.UserDAO;
import com.yunshan.database.vo.UserVO;

public class UserBO {

	public UserVO getUserByName(String name) throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		UserDAO dao = session.getMapper(UserDAO.class);
		UserVO user = dao.getUserByName(name);
		session.close();
		return user;
	}
	
	public UserVO getUserById(int id) throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		UserDAO dao = session.getMapper(UserDAO.class);
		UserVO user = dao.getUserByID(id);
		session.close();
		return user;
	}
	
	public List<UserVO> getUsers() throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		UserDAO dao = session.getMapper(UserDAO.class);
		List<UserVO> users = dao.getUsers();
		session.close();
		return users;
	}
	
}
