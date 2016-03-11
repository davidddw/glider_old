package com.yunshan.config;

public class VMNetInfo {
    private String vl2_name;
    private String info;
    private int vl2_net_index;
    private int if_index;
    private String address;
    
    private String wan_ip;
    private int bandwidth;
    public String getVl2_name() {
        return vl2_name;
    }
    public void setVl2_name(String vl2_name) {
        this.vl2_name = vl2_name;
    }
    public int getVl2_net_index() {
        return vl2_net_index;
    }
    public void setVl2_net_index(int vl2_net_index) {
        this.vl2_net_index = vl2_net_index;
    }
    public int getIf_index() {
        return if_index;
    }
    public void setIf_index(int if_index) {
        this.if_index = if_index;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getWan_ip() {
        return wan_ip;
    }
    public void setWan_ip(String wan_ip) {
        this.wan_ip = wan_ip;
    }
    public int getBandwidth() {
        return bandwidth;
    }
    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }
    @Override
    public String toString() {
        return "VMNetInfo [vl2_name=" + vl2_name + ", vl2_net_index=" + vl2_net_index
                + ", if_index=" + if_index + ", address=" + address + ", wan_ip=" + wan_ip
                + ", bandwidth=" + bandwidth + "]";
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
