package com.yunshan.config.normal;

import java.util.List;
import java.util.Map;

public class RuleInfo {
    private List<Map<String, Object>> snat_rules;
    private List<Map<String, Object>> dnat_rules;
    public List<Map<String, Object>> getSnat_rules() {
        return snat_rules;
    }
    public void setSnat_rules(List<Map<String, Object>> snat_rules) {
        this.snat_rules = snat_rules;
    }
    public List<Map<String, Object>> getDnat_rules() {
        return dnat_rules;
    }
    public void setDnat_rules(List<Map<String, Object>> dnat_rules) {
        this.dnat_rules = dnat_rules;
    }
}
