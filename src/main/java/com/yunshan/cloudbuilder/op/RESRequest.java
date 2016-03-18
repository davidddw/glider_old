package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.utils.Util;

public class RESRequest extends RESTClient {
    
	public RESRequest(String host) {
        super(host);
    }

    public ResultSet getPoolByName(String name) {
        /*
         * @params: name
         * @method: POST /v1/epcs/
         * 
         */
	    ResultSet rs = this.RequestTalker(HttpMethod.GET, "pools", null, null);
	    return filterRecordsByKey(rs, "NAME", name);
    }
	
	public String getPoolLcuuidByName(String name) {
        /*
         * @params: name
         * @method: POST /v1/epcs/
         * 
         */
        ResultSet rs = this.getPoolByName(name);
        return getStringRecordsByKey(rs, "LCUUID");
    }
	
	public ResultSet getValidIp(String name) {
        /*
         * @params: name
         * @method: POST /v1/epcs/
         * 
         */
        ResultSet rs = this.RequestTalker(HttpMethod.GET, "ip-resources", null, null);
        return filterRecordsByKey(rs, "VIFID", "0");
    }

	public String getValidIpLcuuid(String name) {
        /*
         * @params: name
         * @method: POST /v1/epcs/
         * 
         */
        ResultSet rs = this.getValidIp(name);
        return getStringRecordsByKey(rs, "LCUUID");
    }
	
	public String getUuidByIp(String ip) {
        /*
         * @params: ip
         * 
         */
	    ResultSet rs = this.RequestTalker(HttpMethod.GET, "ip-resources", null, null);
	    return getStringRecordsByKey(filterRecordsByKey(rs, "IP", ip), "LCUUID");
	}
	
	public static void main(String[] args) {
	    List<String> list = new ArrayList<String>();
	    String velocityTemplate = "{"
                + "\"userid\": $!userid,"
                + "\"domain\": $!domain"
                + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", 111);
        params.put("domain", 333);
        String ret = Util.velocityProcess(params, velocityTemplate);
        list.add(ret);
        String velocityTemplate1 = "{"
                + "\"userid1\": $!userid,"
                + "\"domain1\": $!domain"
                + "}";
        Map<String, Object> params1 = new HashMap<String, Object>();
        params1.put("userid1", 111);
        params1.put("domain", list);
        String ret2 = Util.velocityProcess(params1, velocityTemplate1);
	    System.out.println(ret2);
	}
}
