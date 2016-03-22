package com.yunshan.config.normal;

import java.util.List;

public class NormalConfig {
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getPool_name() {
		return pool_name;
	}
	public void setPool_name(String pool_name) {
		this.pool_name = pool_name;
	}
	public String getEpc_name() {
		return epc_name;
	}
	public void setEpc_name(String epc_name) {
		this.epc_name = epc_name;
	}
	public List<VMInfo> getVms() {
		return vms;
	}
	public void setVms(List<VMInfo> vms) {
		this.vms = vms;
	}
	public List<VL2Info> getVl2s() {
		return vl2s;
	}
	public void setVl2s(List<VL2Info> vl2s) {
		this.vl2s = vl2s;
	}
	public List<VGWInfo> getVgateways() {
		return vgateways;
	}
	public void setVgateways(List<VGWInfo> vgateways) {
		this.vgateways = vgateways;
	}
	public List<LBInfo> getLbs() {
		return lbs;
	}
	public void setLbs(List<LBInfo> lbs) {
		this.lbs = lbs;
	}
	public List<ValveInfo> getValves() {
		return valves;
	}
	public void setValves(List<ValveInfo> valves) {
		this.valves = valves;
	}
	public List<IPInfo> getIps() {
		return ips;
	}
	public void setIps(List<IPInfo> ips) {
		this.ips = ips;
	}
	public List<BWInfo> getBandws() {
		return bandws;
	}
	public void setBandws(List<BWInfo> bandws) {
		this.bandws = bandws;
	}
	private String host;
	private String user;
	private int orderid;
	private String domain;
	private String pool_name;
	private String epc_name;
	private List<VMInfo> vms;
	private List<VL2Info> vl2s;
	private List<VGWInfo> vgateways;
	private List<LBInfo> lbs;
	private List<ValveInfo> valves;
	private List<IPInfo> ips;
	private List<BWInfo> bandws;

}
