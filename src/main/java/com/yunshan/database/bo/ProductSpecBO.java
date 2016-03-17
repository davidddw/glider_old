package com.yunshan.database.bo;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yunshan.database.ConnectionFactory;
import com.yunshan.database.dao.ProductSpecDAO;
import com.yunshan.database.vo.ProductSpecVO;

public class ProductSpecBO {

	public ProductSpecVO getProductSpecByName(String name) throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		ProductSpecDAO dao = session.getMapper(ProductSpecDAO.class);
		ProductSpecVO productSpec = dao.getProductSpecByName(name);
		session.close();
		return productSpec;
	}
	
	public List<ProductSpecVO> getProductSpecs() throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		ProductSpecDAO dao = session.getMapper(ProductSpecDAO.class);
		List<ProductSpecVO> productSpec = dao.getProductSpecs();
		session.close();
		return productSpec;
	}
}
