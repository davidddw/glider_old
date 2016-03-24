package com.yunshan.database.domain;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 4872640461000241018L;
	private int id;
	private String name;
	private String uuid;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
