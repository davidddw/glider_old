package com.yunshan.cloudbuilder.op;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class EPCRequest extends RESTClient {
    
    private String domain;
    private int userid;

	public EPCRequest(String host, String domain, int userid) {
		super(host);
		this.domain = domain;
		this.userid = userid;
	}

	public ResultSet createEPC(String name) {
		/*
		 * @params: name
		 * @method: POST /v1/epcs/
		 * 
		 */
	    String epcTmpl = "{\"userid\": ${userid},\"name\": \"${name}\",\"domain\": \"${domain}\"}";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("name", name);
	    params.put("userid", this.userid);
	    params.put("domain", this.domain);
	    String ret = Utils.freemarkerProcess(params, epcTmpl);
	    return this.RequestAPP("post", "epcs", ret, null);
	}
	
	public ResultSet modifyEPC(String name, String epcid) {
	    /*
	     * @params: name, epcid
	     * @method: PATCH /v1/epcs/<epcid>
	     */
	    String epcTmpl = "{\"userid\": ${userid},\"name\": \"${name}\"}";
	    Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("userid", this.userid);
        String ret = Utils.freemarkerProcess(params, epcTmpl);
        return this.RequestAPP("patch", "epcs", ret, epcid);
	}
	
	public ResultSet getEPCs() {
	    return this.RequestAPP("get", "epcs", null, null);
	}
	
	public ResultSet getEPCById(String epcid) {
	    /*
	     * @params: epc_id
	     * @method: GET /v1/epcs/<epc_id>/
	     */
        return this.RequestAPP("get", "epcs", null, epcid);
    }
	
	public ResultSet deleteEPC(String epcid) {
	    /*
         * @params: epc_id
         * @method: DELETE /v1/epcs/<epc_id>/
         */
        return this.RequestAPP("delete", "epcs", null, epcid);
	}
	
	public ResultSet getEPCByName(String name) {
	    /*
         * @params: name
         * @method: 
         */
	    ResultSet epcs = this.getEPCs();
	    return filterRecordsByKey(epcs, "NAME", name);
	}
	
	public int getEPCIdByName(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet epcs = getEPCByName(name);
        return getIntRecordsByKey(epcs, "ID");
    }
	
	public ResultSet CreateEPCIfNotExist(String name) {
	    /*
         * @params: name
         * @method: 
         */
	    ResultSet epcs = getEPCByName(name);
	    if (epcs.content()==null) {
	        return this.createEPC(name);
	    } else {
	        return epcs;
	    }
	}
	
	public ResultSet DeleteEPCIfNotExist(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet epcs = getEPCByName(name);
        if (epcs.content()!=null) {
            int epcid = getIntRecordsByKey(epcs, "ID");
            return this.deleteEPC(String.valueOf(epcid));
        } else {
            return simpleResponse("not exist");
        }
    }
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
	    EPCRequest rc = new EPCRequest("10.33.37.28", "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
	    System.out.println(rc.DeleteEPCIfNotExist("ddd"));
	    //System.out.println(rc.getEPCByName("ddw"));
    }
	
}
