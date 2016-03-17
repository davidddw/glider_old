package com.yunshan.config.normal;

import java.util.List;
import java.util.Map;

public class LBListenerInfo {
    private String name;
    private String info;
    private String protocol;
    private int port;
    private String balance;
    private List<Map<String, Object>> vms;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public List<Map<String, Object>> getVms() {
        return vms;
    }
    public void setVms(List<Map<String, Object>> vms) {
        this.vms = vms;
    }
}
