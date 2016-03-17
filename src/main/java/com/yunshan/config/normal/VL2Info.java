package com.yunshan.config.normal;

public class VL2Info {
    private String name;
    private String info;
    private String prefix;
    private int netmask;
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
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public int getNetmask() {
        return netmask;
    }
    public void setNetmask(int netmask) {
        this.netmask = netmask;
    }
    @Override
    public String toString() {
        return "VL2Info [name=" + name + ", info=" + info + ", prefix=" + prefix + ", netmask="
                + netmask + "]";
    }
}
