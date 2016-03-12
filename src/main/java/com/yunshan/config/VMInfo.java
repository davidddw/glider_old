package com.yunshan.config;

import java.util.List;
import java.util.Map;

public class VMInfo {
    private String name;
    private String info;
    private String product_spec;
    private String template;
    private String vm_gw;
    private List<Map<String, Object>> vm_ip;
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
    public String getTemplate() {
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public String getVm_gw() {
        return vm_gw;
    }
    public void setVm_gw(String vm_gw) {
        this.vm_gw = vm_gw;
    }
    public List<Map<String, Object>> getVm_ip() {
        return vm_ip;
    }
    public void setVm_ip(List<Map<String, Object>> vm_ip) {
        this.vm_ip = vm_ip;
    }
    @Override
    public String toString() {
        return "VMInfo [name=" + name + ", info=" + info + ", product_spec=" + product_spec
                + ", template=" + template + ", vm_gw=" + vm_gw + ", vm_ip=" + vm_ip + "]";
    }
    
}
