package com.yunshan.config.solution;

import java.util.List;

public class SolutionConfig {
	private String host;
    private int userid;
    private int orderid;
    private String domain;
    private String pool_name;
    private String epc_name;
    public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	private List<VMInfo> vms;
    private List<VL2Info> vl2s;
    private List<VGWInfo> vgw;
    public List<VGWInfo> getVgw() {
        return vgw;
    }
    public void setVgw(List<VGWInfo> vgw) {
        this.vgw = vgw;
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

}
