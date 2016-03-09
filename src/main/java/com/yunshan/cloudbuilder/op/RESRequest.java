package com.yunshan.cloudbuilder.op;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

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
	    ResultSet rs = this.RequestTalker("get", "pools", null, null);
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
        ResultSet rs = this.RequestTalker("get", "ip-resources", null, null);
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

	public static void main(String[] args) throws ClientProtocolException, IOException {
	    RESRequest rc = new RESRequest("10.33.37.28");
	    System.out.println(rc.getPoolLcuuidByName("KVMPool"));
	    //System.out.println(rc.getEPCByName("ddw"));
    }
	
}
