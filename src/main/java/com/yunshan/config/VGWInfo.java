package com.yunshan.config;

import java.util.List;
import java.util.Map;

public class VGWInfo {
    private String name;
    private String info;
    private String product_spec;
    private List<Map<String, String>> wan;
    private List<Map<String, String>> lan;
    private RuleInfo rules;
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
    public String getProduct_spec() {
        return product_spec;
    }
    public void setProduct_spec(String product_spec) {
        this.product_spec = product_spec;
    }
    public List<Map<String, String>> getWan() {
        return wan;
    }
    public void setWan(List<Map<String, String>> wan) {
        this.wan = wan;
    }
    public List<Map<String, String>> getLan() {
        return lan;
    }
    public void setLan(List<Map<String, String>> lan) {
        this.lan = lan;
    }
    public RuleInfo getRules() {
        return rules;
    }
    public void setRules(RuleInfo rules) {
        this.rules = rules;
    }
}
