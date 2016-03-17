package com.yunshan.database.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.yunshan.database.vo.UserVO;

public interface UserDAO {
	String MQL_GET_ALL_USERS  = "select * from user_v2_2";
	String MQL_GET_USER_BY_NAME = "select id, username, useruuid from user_v2_2 where username=#{username}";
	String MQL_GET_USER_BY_ID = "select id, username, useruuid from user_v2_2 where id=#{id}";

	@Select(MQL_GET_USER_BY_NAME)
	public UserVO getUserByName(String username) throws Exception;
	
	@Select(MQL_GET_USER_BY_ID)
	public UserVO getUserByID(int id) throws Exception;
	
	@Select(MQL_GET_ALL_USERS)
	public List<UserVO> getUsers() throws Exception;

}
