package com.yunshan.cloudbuilder.op;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;

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
	    RESRequest rs = new RESRequest("10.33.37.28");
	    System.out.println(rs.getUuidByIp("192.168.182.162"));
	}
}
