package com.yunshan.config;

import java.util.List;
import java.util.Map;

public class ValveInfo {
    private String name;
    private String info;
    private int general_bandwidth;
    private String product_spec;
    private List<Map<String, Object>> wan;
    private List<Map<String, Object>> lan;
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
    public int getGeneral_bandwidth() {
        return general_bandwidth;
    }
    public void setGeneral_bandwidth(int general_bandwidth) {
        this.general_bandwidth = general_bandwidth;
    }
    public String getProduct_spec() {
        return product_spec;
    }
    public void setProduct_spec(String product_spec) {
        this.product_spec = product_spec;
    }
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
}
