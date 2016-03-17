package com.yunshan.database.vo;

public class DomainVO {
	private int id;
	private String name;
	private String ip;
	private String role;
	private String lcuuid;
	private String public_ip;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLcuuid() {
		return lcuuid;
	}
	public void setLcuuid(String lcuuid) {
		this.lcuuid = lcuuid;
	}
	public String getPublic_ip() {
		return public_ip;
	}
	public void setPublic_ip(String public_ip) {
		this.public_ip = public_ip;
	}
	@Override
	public String toString() {
		return "DomainVO [id=" + id + ", name=" + name + ", ip=" + ip + ", role=" + role + ", lcuuid=" + lcuuid
				+ ", public_ip=" + public_ip + "]";
	}
}
