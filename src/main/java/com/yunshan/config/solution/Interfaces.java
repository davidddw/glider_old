package com.yunshan.config.solution;

import java.util.List;
import java.util.Map;

public class Interfaces {
	private List<Map<String, Object>> wan;
	private List<Map<String, Object>> lan;
    public List<Map<String, Object>> getWan() {
		return wan;
	}
	public void setWan(List<Map<String, Object>> wan) {
		this.wan = wan;
	}
	public List<Map<String, Object>> getLan() {
		return lan;
	}
	public void setLan(List<Map<String, Object>> lan) {
		this.lan = lan;
	}
	@Override
	public String toString() {
		return "VGWInterface [wan=" + wan + ", lan=" + lan + "]";
	}
}
