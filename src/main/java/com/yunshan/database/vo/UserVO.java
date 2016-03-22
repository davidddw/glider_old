package com.yunshan.database.vo;

import java.io.Serializable;

public class UserVO implements Serializable {

	private static final long serialVersionUID = 4872640461000241018L;
	private int id;
	private String username;
	private String useruuid;
	@Override
	public String toString() {
		return "UserVO [id=" + id + ", username=" + username + ", useruuid=" + useruuid + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUseruuid() {
		return useruuid;
	}
	public void setUseruuid(String useruuid) {
		this.useruuid = useruuid;
	}
}
