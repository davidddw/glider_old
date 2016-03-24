package com.yunshan.database.service;

import java.util.List;
import javax.inject.Inject;

import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import com.yunshan.database.domain.Domain;
import com.yunshan.database.domain.ProductSpec;
import com.yunshan.database.domain.User;
import com.yunshan.database.mapper.DomainMapper;
import com.yunshan.database.mapper.ProductSpecMapper;
import com.yunshan.database.mapper.UserMapper;

public class LivecloudServiceImpl implements LivecloudService {

	private DomainMapper domainMapper;
	private ProductSpecMapper productSpecMapper;
	private UserMapper userMapper;

	public DomainMapper getDomainMapper() {
		return domainMapper;
	}

	public ProductSpecMapper getProductSpecMapper() {
		return productSpecMapper;
	}

	public UserMapper getUserMapper() {
		return userMapper;
	}

	@Inject
	public void setProductSpecMapper(ProductSpecMapper productSpecMapper) {
		this.productSpecMapper = productSpecMapper;
	}

	@Inject
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Inject
	public void setDomainMapper(DomainMapper domainMapper) {
		this.domainMapper = domainMapper;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public Domain getDomainByName(String name) {
		return this.domainMapper.getDomainByName(name);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public List<Domain> getDomains() {
		return this.domainMapper.getAllDomains();
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public ProductSpec getProductSpecByName(String name) {
		return productSpecMapper.getProductSpecByName(name);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public List<ProductSpec> getProductSpecs() {
		return productSpecMapper.getProductSpecs();
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public User getUserByName(String name) {
		return userMapper.getUserByName(name);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public User getUserById(int id) {
		return userMapper.getUserByID(id);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = IllegalArgumentException.class)
	public List<User> getUsers() {
		return userMapper.getUsers();
	}

}
