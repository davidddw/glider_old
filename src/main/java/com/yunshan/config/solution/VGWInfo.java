package com.yunshan.config.solution;

public class VGWInfo {
	private String name;
    private String product_spec;
    private Interfaces interfaces;
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProduct_spec() {
		return product_spec;
	}
	public void setProduct_spec(String product_spec) {
		this.product_spec = product_spec;
	}
	public Interfaces getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(Interfaces interfaces) {
		this.interfaces = interfaces;
	}
	@Override
	public String toString() {
		return "VGWInfo [name=" + name + ", product_spec=" + product_spec + ", interfaces=" + interfaces + "]";
	}
}
