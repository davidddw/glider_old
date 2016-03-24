package com.yunshan.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.yunshan.database.domain.ProductSpec;

public interface ProductSpecMapper {
	
	String MQL_GET_ALL  = "select id, lcuuid, name, domain, plan_name from product_specification_v2_2";
	String MQL_GET_BY_NAME = "select id, lcuuid, name, domain, plan_name from product_specification_v2_2 where plan_name=#{plan_name}";
	
	@Select(MQL_GET_ALL)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="uuid", column="lcuuid"),
			@Result(property="name", column="name"),
			@Result(property="role", column="domain"),
			@Result(property="planName", column="plan_name")
	})
	List<ProductSpec> getProductSpecs();
	
	@Select(MQL_GET_BY_NAME)
	@Results(value = {
			@Result(property="id", column="id"),
			@Result(property="uuid", column="lcuuid"),
			@Result(property="name", column="name"),
			@Result(property="role", column="domain"),
			@Result(property="planName", column="plan_name")
	})
	ProductSpec getProductSpecByName(String plan_name);

}
