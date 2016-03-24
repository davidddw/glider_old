package com.yunshan.database.service;

import java.util.List;

import com.yunshan.database.domain.Domain;
import com.yunshan.database.domain.ProductSpec;
import com.yunshan.database.domain.User;

public interface LivecloudService {

	Domain getDomainByName(String name);
	
	List<Domain> getDomains();
	
	ProductSpec getProductSpecByName(String name);
	
	List<ProductSpec> getProductSpecs();
	
	User getUserByName(String name);
	
	User getUserById(int id);
	
	List<User> getUsers();
}
