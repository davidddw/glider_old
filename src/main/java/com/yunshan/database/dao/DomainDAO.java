package com.yunshan.database.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.yunshan.database.vo.DomainVO;

public interface DomainDAO {
	String MQL_GET_ALL_USERS  = "select * from domain_v2_2";
	String MQL_GET_BY_NAME = "select * from domain_v2_2 where name=#{name}";

	@Select(MQL_GET_ALL_USERS)
	public List<DomainVO> getAllDomains() throws Exception;
	
	@Select(MQL_GET_BY_NAME)
	public DomainVO getDomainByName(String name) throws Exception;

}
