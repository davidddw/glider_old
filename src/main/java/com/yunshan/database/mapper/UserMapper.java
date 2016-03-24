package com.yunshan.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.yunshan.database.domain.User;

public interface UserMapper {
	String MQL_GET_ALL_USERS  = "select * from user_v2_2";
	String MQL_GET_USER_BY_NAME = "select * from user_v2_2 where username=#{username}";
	String MQL_GET_USER_BY_ID = "select * from user_v2_2 where id=#{id}";

	@Select(MQL_GET_USER_BY_NAME)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="name", column="username"),
			@Result(property="uuid", column="useruuid")
	})
	User getUserByName(String username);
	
	@Select(MQL_GET_USER_BY_ID)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="name", column="username"),
			@Result(property="uuid", column="useruuid")
	})
	User getUserByID(int id);
	
	@Select(MQL_GET_ALL_USERS)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="name", column="username"),
			@Result(property="uuid", column="useruuid")
	})
	List<User> getUsers();

}
