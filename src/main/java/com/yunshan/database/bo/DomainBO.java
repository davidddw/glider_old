package com.yunshan.database.bo;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yunshan.database.ConnectionFactory;
import com.yunshan.database.dao.DomainDAO;
import com.yunshan.database.vo.DomainVO;

public class DomainBO {

	public DomainVO getDomainByName(String name) throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		DomainDAO dao = session.getMapper(DomainDAO.class);
		DomainVO domain = dao.getDomainByName(name);
		session.close();
		return domain;
	}
	
	public List<DomainVO> getDomains() throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		DomainDAO dao = session.getMapper(DomainDAO.class);
		List<DomainVO> domain = dao.getAllDomains();
		session.close();
		return domain;
	}
}
