package com.yunshan.config;

import java.util.List;
import java.util.Map;

public class RuleInfo {
    private List<Map<String, String>> snat_rules;
    private List<Map<String, String>> dnat_rules;
    public List<Map<String, String>> getSnat_rules() {
        return snat_rules;
    }
    public void setSnat_rules(List<Map<String, String>> snat_rules) {
        this.snat_rules = snat_rules;
    }
    public List<Map<String, String>> getDnat_rules() {
        return dnat_rules;
    }
    public void setDnat_rules(List<Map<String, String>> dnat_rules) {
        this.dnat_rules = dnat_rules;
    }
}
