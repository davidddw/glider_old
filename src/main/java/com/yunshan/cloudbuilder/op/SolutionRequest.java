package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class SolutionRequest extends RESTClient {
    
    private int userid;
    private String domain;
    private String epc_name;
    
	public SolutionRequest(String host, String domain, int userid, String epc_name) {
		super(host);
		this.userid = userid;
		this.domain = domain;
		this.epc_name = epc_name;
	}
	
	public String generateVGWInterfaceData(List<Map<String, Object>> wan_list, 
            List<Map<String, Object>> lan_list) {
        /*
         * @params: isp, product_spec, qos_product_spec
         * @params: vl2_name
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplateW = "{"
        	+ "\"WAN\":" 
        	+ "{\"ISP\": $isp,"
        	+ "\"IP_NUM\": 2,"
        	+ "\"IP_PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
        	+ "\"QOS\":"
        	+ "{\"PRODUCT_SPECIFICATION_LCUUID\": \"$!qos_product_spec\","
            + "\"BANDWIDTH\": 10485760"
            + "},"
            + "\"IPS\": [ {}, {} ]"
            + "}"
            + "}";
        for (Map<String, Object> map : Utils.emptyIfNull(wan_list)) {
        	String ret = Utils.velocityProcess(map, velocityTemplateW);
        	list.add(ret);
        }
        String velocityTemplateL = "{"
        		+ "\"LAN\": { \"VL2_NAME\": \"$!vl2_name\" }"
                + "}";
        for (Map<String, Object> map : Utils.emptyIfNull(lan_list)) {
            String ret = Utils.velocityProcess(map, velocityTemplateL);
            list.add(ret);
        }
        return list.toString();
    }
	
	public String generateVMInterfaceData(List<Map<String, Object>> wan_list, 
            List<Map<String, Object>> lan_list) {
        /*
         * @params: isp, product_spec, qos_product_spec
         * @params: vl2_name
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplateW = "{"
        	+ "\"WAN\":" 
        	+ "{\"ISP\": $isp,"
        	+ "\"IP_NUM\": 2,"
        	+ "\"IP_PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
        	+ "\"QOS\":"
        	+ "{\"PRODUCT_SPECIFICATION_LCUUID\": \"$!qos_product_spec\","
            + "\"BANDWIDTH\": 10485760"
            + "},"
            + "\"IPS\": [ {} ]"
            + "}"
            + "}";
        for (Map<String, Object> map : Utils.emptyIfNull(wan_list)) {
        	String ret = Utils.velocityProcess(map, velocityTemplateW);
        	list.add(ret);
        }
        String velocityTemplateL = "{"
        		+ "\"LAN\": { \"VL2_NAME\": \"$!vl2_name\" }"
                + "}";
        for (Map<String, Object> map : Utils.emptyIfNull(lan_list)) {
            String ret = Utils.velocityProcess(map, velocityTemplateL);
            list.add(ret);
        }
        return list.toString();
    }
	
	public String generateVGWData(List<Map<String, Object>> vgw_info) {
        /*
         * @params: name, product_spec, interfaces_data
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
        	+ "\"LCUUID\": \"$!product_spec\","
        	+ "\"VGWCFG\": { \"lans\": 3, \"wans\": 3 },"
        	+ "\"VGWNAME\": \"$!name\","
        	+ "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
        	+ "\"NAME\": \"$!name\","
        	+ "\"INTERFACES\": $!interfaces"
            + "}";
        for (Map<String, Object> map : Utils.emptyIfNull(vgw_info)) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list.toString();
    }
	
	public String generateVmData(List<Map<String, Object>> vm_info) {
	    /*
	     * @params: name, product_spec, template, interfaces_data
	     */
	    List<String> list = new ArrayList<String>();
	    String velocityTemplate = "{"
	        + "\"NAME\": \"$!name\","
	        + "\"PASSWD\": \"yunshan3302\","
	        + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\","
	        + "\"OS\": \"$!os_template\","
	        + "\"ROLE\": \"GENERAL_PURPOSE\","
	        + "\"VCPU_NUM\":1,"
	        + "\"MEM_SIZE\":1024,"
	        + "\"SYS_DISK_SIZE\":50,"
	        + "\"USER_DISK_SIZE\":0,"
	        + "\"INTERFACES\": $!interfaces"
	        + "}";
	    for (Map<String, Object> map : Utils.emptyIfNull(vm_info)) {
	        String ret = Utils.velocityProcess(map, velocityTemplate);
	        list.add(ret);
	    }
        return list.toString();
	}
	
	public String generateVl2Data(List<Map<String, Object>> vl2_info) {
        /*
         * @params: name, prefix_id
         */
        List<String> list = new ArrayList<String>();
        String velocityTemplate = "{"
            + "\"NAME\": \"$!name\""
            + "}";
        for (Map<String, Object> map : Utils.emptyIfNull(vl2_info)) {
            String ret = Utils.velocityProcess(map, velocityTemplate);
            list.add(ret);
        }
        return list.toString();
    }

	public ResultSet execute(List<Map<String, Object>> vgw_info, List<Map<String, Object>> vms_info, 
			List<Map<String, Object>> vl2_info) {
		/*
		 * @params: id, userid, domain_lcuuid
		 * @method: POST /v1/orders/
		 * 
		 */
	    String velocityTemplate = "{"
	    		+ "\"DOMAIN\": \"$!domain_lcuuid\","
	            + "\"NAME\": \"$!epc_name\","
	            + "\"USERID\": $!userid,"
	            + "\"ORDER_ID\": $!id,"
	            + "\"CHARGE_DAYS\": 7,"
	            + "\"VGATEWAYS\": $!vgw,"
	            + "\"VMS\": $!vms,"
	            + "\"VL2S\": $!vl2s"
	            + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("vgw", this.generateVGWData(vgw_info));
	    params.put("vms", this.generateVmData(vms_info));
	    params.put("vl2s", this.generateVl2Data(vl2_info));
	    params.put("userid", this.userid);
	    params.put("domain_lcuuid", this.domain);
	    params.put("epc_name", this.epc_name);
	    params.put("id", 10000);
	    String ret = Utils.velocityProcess(params, velocityTemplate);
	    return this.RequestAPP(HttpMethod.POST, "solutions", ret, null);
	}
	
	
	
}
