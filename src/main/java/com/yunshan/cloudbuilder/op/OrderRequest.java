package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class OrderRequest extends RESTClient {
    
    private int userid;
    private String domain;

	public OrderRequest(String host, String domain, int userid) {
		super(host);
		this.userid = userid;
		this.domain = domain;
	}
	
	private String generateVmData(List<Map<String, Object>> vm_info) {
	    /*
	     * @params: name, product_spec, os_template, domain_lcuuid
	     */
	    List<String> list = new ArrayList<String>();
	    String vmTmpl = "{"
	        + "\"NAME\": \"${name}\","
	        + "\"PASSWD\": \"yunshan3302\","
	        + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\","
	        + "\"OS\": \"${os_template}\","
	        + "\"ROLE\": \"GENERAL_PURPOSE\","
	        + "\"VCPU_NUM\":1,"
	        + "\"MEM_SIZE\":1024,"
	        + "\"SYS_DISK_SIZE\":40,"
	        + "\"USER_DISK_SIZE\":0,"
	        + "\"DOMAIN\":\"${domain_lcuuid}\""
	        + "}";
	    if (vm_info==null)
            return list.toString();
        for (Map<String, Object> map : vm_info) {
            String ret = Utils.freemarkerProcess(map, vmTmpl);
            list.add(ret);
        }
        return  "[" + String.join(",", list) + "]";
	}
	
	private String generateLBData(List<Map<String, Object>> lb_info) {
        /*
         * @params: name, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String vmTmpl = "{"
            + "\"NAME\": \"${name}\","
            + "\"PASSWD\": \"yunshan3302\","
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\","
            + "\"ROLE\" : \"LOAD_BALANCER\","
            + "\"VCPU_NUM\":2,"
            + "\"MEM_SIZE\":2048,"
            + "\"SYS_DISK_SIZE\":40,"
            + "\"USER_DISK_SIZE\":0,"
            + "\"DOMAIN\":\"${domain_lcuuid}\""
            + "}";
        
        if (lb_info==null)
            return list.toString();
        for (Map<String, Object> map : lb_info) {
            String ret = Utils.freemarkerProcess(map, vmTmpl);
            list.add(ret);
        }
        return  "[" + String.join(",", list) + "]";
    }
	
	private String generateVGWData(List<Map<String, Object>> vgateway_info) {
        /*
         * @params: name, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String vmTmpl = "{"
            + "\"NAME\": \"${name}\","
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\","
            + "\"DOMAIN\":\"${domain_lcuuid}\""
            + "}";
        
        if (vgateway_info==null)
            return list.toString();
        for (Map<String, Object> map : vgateway_info) {
            String ret = Utils.freemarkerProcess(map, vmTmpl);
            list.add(ret);
        }
        return  "[" + String.join(",", list) + "]";
    }
	
	private String generateValveData(List<Map<String, Object>> valve_info) {
        /*
         * @params: name, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String vmTmpl = "{"
            + "\"NAME\": \"${name}\","
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\","
            + "\"DOMAIN\":\"${domain_lcuuid}\""
            + "}";
        
        if (valve_info==null)
            return list.toString();
        for (Map<String, Object> map : valve_info) {
            String ret = Utils.freemarkerProcess(map, vmTmpl);
            list.add(ret);
        }
        return  "[" + String.join(",", list) + "]";
    }
	
	private String generateIPData(List<Map<String, Object>> ip_info) {
        /*
         * @params: isp, number, product_spec, os_template, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String vmTmpl = "{"
            + "\"ISP\": ${isp},"
            + "\"IP_NUM\": ${number},"
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\","
            + "\"DOMAIN\":\"${domain_lcuuid}\""
            + "}";
        
        if (ip_info==null)
            return list.toString();
        for (Map<String, Object> map : ip_info) {
            String ret = Utils.freemarkerProcess(map, vmTmpl);
            list.add(ret);
        }
        return  "[" + String.join(",", list) + "]";
    }
	
	private String generateBWData(List<Map<String, Object>> bandw_info) {
        /*
         * @params: isp, bandw, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String vmTmpl = "{"
            + "\"ISP\": ${isp},"
            + "\"BANDWIDTH\": ${bandw},"
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\","
            + "\"DOMAIN\":\"${domain_lcuuid}\""
            + "}";
        if (bandw_info==null)
            return list.toString();
        for (Map<String, Object> map : bandw_info) {
            String ret = Utils.freemarkerProcess(map, vmTmpl);
            list.add(ret);
        }
        return  "[" + String.join(",", list) + "]";
    }

	public ResultSet customOrder(List<Map<String, Object>> vm_info, 
	        List<Map<String, Object>> lb_info,
	        List<Map<String, Object>> vgateway_info,
	        List<Map<String, Object>> valve_info,
	        List<Map<String, Object>> ip_info,
	        List<Map<String, Object>> bandw_info) {
		/*
		 * @params: id, userid, domain_lcuuid
		 * @method: POST /v1/orders/
		 * 
		 */
	    String orderTmpl = "{"
	            + "\"ORDER_ID\": ${id?c},"
	            + "\"USERID\": ${userid?c},"
	            + "\"DOMAIN\": \"${domain_lcuuid}\","
	            + "\"VMS\": ${vms},"
	            + "\"LBS\": ${lbs},"
	            + "\"VGWS\": ${vgs},"
	            + "\"BWTS\": ${vas},"
	            + "\"IPS\": ${ips},"
	            + "\"BANDWIDTHS\": ${bandw}"
	            + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("vms", this.generateVmData(vm_info));
	    params.put("lbs", this.generateLBData(lb_info));
	    params.put("vgs", this.generateVGWData(vgateway_info));
	    params.put("vas", this.generateValveData(valve_info));
	    params.put("ips", this.generateIPData(ip_info));
	    params.put("bandw", this.generateBWData(bandw_info));
	    params.put("userid", this.userid);
	    params.put("domain_lcuuid", this.domain);
	    params.put("id", 10000);
	    String ret = Utils.freemarkerProcess(params, orderTmpl);
	    s_logger.error(ret);
	    return this.RequestAPP("post", "orders", ret, null);
	}
	
	public ResultSet orderVM(String name, String product_spec, String os_template) {
	    /*
	     * @params: name, product_spec, template
	     */
	    List<Map<String, Object>> vm_info = new ArrayList<Map<String, Object>>();
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("name", name);
	    map.put("product_spec", product_spec);
	    map.put("os_template", os_template);
	    map.put("domain_lcuuid", this.domain);
	    vm_info.add(map);
	    return this.customOrder(vm_info, null, null, null, null, null);
	}
	
	public ResultSet orderLB(String name, String product_spec) {
        /*
         * @params: name, product_spec
         */
        List<Map<String, Object>> lb_info = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        lb_info.add(map);
        return this.customOrder(null, lb_info, null, null, null, null);
    }

	public ResultSet orderVGW(String name, String product_spec) {
        /*
         * @params: name, product_spec
         */
        List<Map<String, Object>> vgateway_info = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        vgateway_info.add(map);
        return this.customOrder(null, null, vgateway_info, null, null, null);
    }

	public ResultSet orderValve(String name, String product_spec) {
        /*
         * @params: name, product_spec
         */
        List<Map<String, Object>> valve_info = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        valve_info.add(map);
        return this.customOrder(null, null, null, valve_info, null, null);
    }
    
	public ResultSet orderIP(String isp, String number, String product_spec) {
        /*
         * @params: isp, number, product_spec
         */
        List<Map<String, Object>> ip_info = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isp", isp);
        map.put("number", number);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        ip_info.add(map);
        return this.customOrder(null, null, null, null, ip_info, null);
    }
	
	public ResultSet orderBW(String isp, String bandw, String product_spec) {
        /*
         * @params: isp, bandw, product_spec
         */
        List<Map<String, Object>> bandw_info = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isp", isp);
        map.put("bandw", bandw);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        bandw_info.add(map);
        return this.customOrder(null, null, null, null, null, bandw_info);
    }
    
	public static void main(String[] args) {
	    OrderRequest rc = new OrderRequest("10.33.37.28", "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
	    System.out.println(rc.orderVM("dd2", "2c02e128-1881-4fd8-90e5-e106c81c25b1", "centos6.5"));
	    //System.out.println(rc.getEPCByName("ddw"));
    }
	
}
