package com.yunshan.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.yunshan.database.bo.DomainBO;
import com.yunshan.database.bo.ProductSpecBO;
import com.yunshan.database.bo.UserBO;
import com.yunshan.database.vo.DomainVO;
import com.yunshan.database.vo.ProductSpecVO;
import com.yunshan.database.vo.UserVO;
import com.yunshan.utils.Util;

public abstract class LivecloudBase {

	protected static final Logger s_logger = Util.getLogger();
	
	protected Map<String, String> uuidMapper;

	public LivecloudBase(String filename) throws Exception {
		
	}

	protected static String getMapValueSafely(Map<String, String> map, String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			s_logger.error("No properly uuid in the database");
			System.exit(1);
			return null;
		}
	}

	protected static Map<String, String> initMapper() throws Exception {
		s_logger.info("Init Environment, get uuid from database. please wait...");
		Map<String, String> dbUuidMapper = new HashMap<String, String>();
		UserBO userBO = new UserBO();
		for (UserVO userVO : userBO.getUsers()) {
			dbUuidMapper.put(userVO.getUsername(), userVO.getUseruuid());
		}
		DomainBO domainBO = new DomainBO();
		for (DomainVO domainVO : domainBO.getDomains()) {
			dbUuidMapper.put(domainVO.getName(), domainVO.getLcuuid());
		}
		ProductSpecBO productSpecBO = new ProductSpecBO();
		for (ProductSpecVO ProductSpecVO : productSpecBO.getProductSpecs()) {
			dbUuidMapper.put(ProductSpecVO.getPlan_name(), ProductSpecVO.getLcuuid());
		}
		return dbUuidMapper;
	}

	protected <T> T getConfigFromYaml(String filename, Class<T> type) {
		try {
			Yaml yaml = new Yaml();
			InputStream stream = new FileInputStream(new File(filename));
			s_logger.info("Read config from yaml file: " + filename);
			return (T)yaml.loadAs(stream, type);
		} catch (FileNotFoundException e) {
			s_logger.error("Cannot find file " + filename, e);
		}
		return null;
	}

	public abstract void build();
}
