package com.yunshan.database.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.yunshan.database.vo.ProductSpecVO;

public interface ProductSpecDAO {
	
	String MQL_GET_ALL  = "select id, lcuuid, name, domain, plan_name from product_specification_v2_2";
	String MQL_GET_BY_NAME = "select id, lcuuid, name, domain, plan_name from product_specification_v2_2 where plan_name=#{plan_name}";
	
	@Select(MQL_GET_ALL)
	public List<ProductSpecVO> getProductSpecs() throws Exception;
	
	@Select(MQL_GET_BY_NAME)
	public ProductSpecVO getProductSpecByName(String plan_name) throws Exception;

}
