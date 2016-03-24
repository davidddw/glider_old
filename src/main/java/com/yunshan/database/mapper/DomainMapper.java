package com.yunshan.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import com.yunshan.database.domain.Domain;

public interface DomainMapper {
	String MQL_GET_ALL_USERS  = "select * from domain_v2_2";
	String MQL_GET_BY_NAME = "select * from domain_v2_2 where name=#{name}";

	@Select(MQL_GET_ALL_USERS)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="name", column="name"),
			@Result(property="ip", column="ip"),
			@Result(property="role", column="role"),
			@Result(property="uuid", column="lcuuid"),
			@Result(property="publicIp", column="public_ip")
	})
	List<Domain> getAllDomains();
	
	@Select(MQL_GET_BY_NAME)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="name", column="name"),
			@Result(property="ip", column="ip"),
			@Result(property="role", column="role"),
			@Result(property="uuid", column="lcuuid"),
			@Result(property="publicIp", column="public_ip")
	})
	Domain getDomainByName(String name);

}
