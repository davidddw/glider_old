package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class OrderRequest extends RESTClient {
    
    private int userid;
    private String domain;
    
    private List<Map<String, Object>> vm_info = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> lb_info = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> vgateway_info = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> valve_info = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> ip_info = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> bandw_info = new ArrayList<Map<String, Object>>();
    private VGWRequest vgwRequest = null;
    private ValveRequest valveRequest = null;
    private VMRequest vmRequest = null;
    private LBSRequest lbRequest = null;
    
	public OrderRequest(String host, String poolName, String domain, int userid) {
		super(host);
		this.userid = userid;
		this.domain = domain;
		vgwRequest = new VGWRequest(host, domain, userid);
        valveRequest = new ValveRequest(host, domain, userid);
        vmRequest = new VMRequest(host, poolName, domain, userid);
        lbRequest = new LBSRequest(host, poolName, domain, userid);
	}
	
	private List<String> generateVmData(List<Map<String, Object>> vm_info) {
	    /*
	     * @params: name, product_spec, os_template, domain_lcuuid
	     */
	    List<String> list = new ArrayList<String>();
	    String velocityTemplate = "{"
	        + "\"NAME\": \"$!name\","
	        + "\"PASSWD\": \"yunshan3302\","
	        + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
	        + "\"OS\": \"$!os_template\","
	        + "\"ROLE\": \"GENERAL_PURPOSE\","
	        + "\"VCPU_NUM\":$!VCPU_NUM,"
	        + "\"MEM_SIZE\":$!MEM_SIZE,"
	        + "\"SYS_DISK_SIZE\":$!SYS_DISK_SIZE,"
	        + "\"USER_DISK_SIZE\":$!USER_DISK_SIZE,"
	        + "\"DOMAIN\":\"$!domain_lcuuid\""
	        + "}";
	    if (vm_info==null)
            return list;
        for (Map<String, Object> map : vm_info) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list;
	}
	
	private List<String> generateLBData(List<Map<String, Object>> lb_info) {
        /*
         * @params: name, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
            + "\"NAME\": \"$!name\","
            + "\"PASSWD\": \"yunshan3302\","
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
            + "\"ROLE\" : \"LOAD_BALANCER\","
            + "\"VCPU_NUM\":$!VCPU_NUM,"
            + "\"MEM_SIZE\":$!MEM_SIZE,"
            + "\"SYS_DISK_SIZE\":$!SYS_DISK_SIZE,"
            + "\"USER_DISK_SIZE\":$!USER_DISK_SIZE,"
            + "\"DOMAIN\":\"$!domain_lcuuid\""
            + "}";
        
        if (lb_info==null)
            return list;
        for (Map<String, Object> map : lb_info) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list;
    }
	
	private List<String> generateVGWData(List<Map<String, Object>> vgateway_info) {
        /*
         * @params: name, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
            + "\"NAME\": \"$!name\","
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
            + "\"DOMAIN\":\"$!domain_lcuuid\""
            + "}";
        if (vgateway_info==null)
            return list;
        for (Map<String, Object> map : vgateway_info) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list;
    }
	
	private List<String> generateValveData(List<Map<String, Object>> valve_info) {
        /*
         * @params: name, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
            + "\"NAME\": \"$!name\","
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
            + "\"DOMAIN\":\"$!domain_lcuuid\""
            + "}";
        if (valve_info==null)
            return list;
        for (Map<String, Object> map : valve_info) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list;
    }
	
	private List<String> generateIPData(List<Map<String, Object>> ip_info) {
        /*
         * @params: isp, number, product_spec
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
            + "\"ISP\": $!isp,"
            + "\"IP_NUM\": $!number,"
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
            + "\"DOMAIN\":\"$!domain_lcuuid\""
            + "}";
        
        if (ip_info==null)
            return list;
        for (Map<String, Object> map : ip_info) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list;
    }
	
	private List<String> generateBWData(List<Map<String, Object>> bandw_info) {
        /*
         * @params: isp, bandw, product_spec, domain_lcuuid
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
            + "\"ISP\": $!isp,"
            + "\"BANDWIDTH\": $!bandw,"
            + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
            + "\"DOMAIN\":\"$!domain_lcuuid\""
            + "}";
        if (bandw_info==null)
            return list;
        for (Map<String, Object> map : bandw_info) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list;
    }

	public ResultSet execute() {
		/*
		 * @params: id, userid, domain_lcuuid
		 * @method: POST /v1/orders/
		 * 
		 */
	    String velocityTemplate = "{"
	            + "\"ORDER_ID\": $!id,"
	            + "\"USERID\": $!userid,"
	            + "\"DOMAIN\": \"$!domain_lcuuid\","
	            + "\"VMS\": $!vms,"
	            + "\"LBS\": $!lbs,"
	            + "\"VGWS\": $!vgs,"
	            + "\"BWTS\": $!vas,"
	            + "\"IPS\": $!ips,"
	            + "\"BANDWIDTHS\": $!bandw"
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
	    String ret = Utils.velocityProcess(params, velocityTemplate);
	    return this.RequestAPP(HttpMethod.POST, "orders", ret, null);
	}
	
	public OrderRequest orderVM(String name, String product_spec, String os_template) {
	    /*
	     * @params: name, product_spec, template
	     */
	    Map<String, Object> map = new HashMap<String, Object>();
	    ResultSet resultSet = vmRequest.getVmByName(name);
	    if (resultSet.content()==null) {
    	    map.put("name", name);
    	    map.put("product_spec", product_spec);
    	    map.put("os_template", os_template);
    	    map.put("domain_lcuuid", this.domain);
    	    map.put("VCPU_NUM", Integer.parseInt(props.getProperty("VM.VCPU_NUM")));
            map.put("MEM_SIZE", Integer.parseInt(props.getProperty("VM.MEM_SIZE")));
            map.put("SYS_DISK_SIZE", Integer.parseInt(props.getProperty("VM.SYS_DISK_SIZE")));
            map.put("USER_DISK_SIZE", Integer.parseInt(props.getProperty("VM.USER_DISK_SIZE")));
    	    vm_info.add(map);
	    }
	    return this;
	}
	
	public OrderRequest orderLB(String name, String product_spec) {
        /*
         * @params: name, product_spec
         */
        Map<String, Object> map = new HashMap<String, Object>();
        ResultSet resultSet = lbRequest.getLBByName(name);
        if (resultSet.content()==null) {
            map.put("name", name);
            map.put("product_spec", product_spec);
            map.put("domain_lcuuid", this.domain);
            map.put("VCPU_NUM", Integer.parseInt(props.getProperty("LB.VCPU_NUM")));
            map.put("MEM_SIZE", Integer.parseInt(props.getProperty("LB.MEM_SIZE")));
            map.put("SYS_DISK_SIZE", Integer.parseInt(props.getProperty("LB.SYS_DISK_SIZE")));
            map.put("USER_DISK_SIZE", Integer.parseInt(props.getProperty("LB.USER_DISK_SIZE")));
            lb_info.add(map);
        }
        return this;
    }

	public OrderRequest orderVGW(String name, String product_spec) {
        /*
         * @params: name, product_spec
         */
	    Map<String, Object> map = new HashMap<String, Object>();
	    ResultSet resultSet = vgwRequest.getVgatewayByName(name);
	    if (resultSet.content()==null) {
	        map.put("name", name);
	        map.put("product_spec", product_spec);
	        map.put("domain_lcuuid", this.domain);
	        vgateway_info.add(map);
	    } 
	    return this;
    }

	public OrderRequest orderValve(String name, String product_spec) {
        /*
         * @params: name, product_spec
         */
        Map<String, Object> map = new HashMap<String, Object>();
        ResultSet resultSet = valveRequest.getValveByName(name);
        if (resultSet.content()==null) {
            map.put("name", name);
            map.put("product_spec", product_spec);
            map.put("domain_lcuuid", this.domain);
            valve_info.add(map);
        }
        return this;
    }
    
	public OrderRequest orderIP(int isp, int number, String product_spec) {
        /*
         * @params: isp, number, product_spec
         */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isp", isp);
        map.put("number", number);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        ip_info.add(map);
        return this;
    }
	
	public OrderRequest orderBW(int isp, int bandw, String product_spec) {
        /*
         * @params: isp, bandw, product_spec
         */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isp", isp);
        map.put("bandw", bandw);
        map.put("product_spec", product_spec);
        map.put("domain_lcuuid", this.domain);
        bandw_info.add(map);
        return this;
    }
	
}
