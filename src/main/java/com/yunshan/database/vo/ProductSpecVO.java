package com.yunshan.database.vo;

public class ProductSpecVO {
	private int id;
	private String lcuuid;
	private String name;
	private String domain;
	private String plan_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLcuuid() {
		return lcuuid;
	}
	public void setLcuuid(String lcuuid) {
		this.lcuuid = lcuuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getPlan_name() {
		return plan_name;
	}
	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}
}
