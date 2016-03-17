package com.yunshan.config.normal;

import java.util.List;
import java.util.Map;

public class LBInfo {
    private String name;
    private String info;
    private String lb_gw;
    private String product_spec;
    private List<Map<String, Object>> lb_ip;
    private List<LBListenerInfo> lb_listener;
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
    public String getLb_gw() {
        return lb_gw;
    }
    public void setLb_gw(String lb_gw) {
        this.lb_gw = lb_gw;
    }
    public String getProduct_spec() {
        return product_spec;
    }
    public void setProduct_spec(String product_spec) {
        this.product_spec = product_spec;
    }
    public List<Map<String, Object>> getLb_ip() {
        return lb_ip;
    }
    public void setLb_ip(List<Map<String, Object>> lb_ip) {
        this.lb_ip = lb_ip;
    }
    public List<LBListenerInfo> getLb_listener() {
        return lb_listener;
    }
    public void setLb_listener(List<LBListenerInfo> lb_listener) {
        this.lb_listener = lb_listener;
    }
    @Override
    public String toString() {
        return "LBInfo [name=" + name + ", info=" + info + ", lb_gw=" + lb_gw + ", product_spec="
                + product_spec + ", lb_ip=" + lb_ip + ", lb_listener=" + lb_listener + "]";
    }
}
