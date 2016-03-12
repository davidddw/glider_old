package com.yunshan.cloudbuilder.op;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class EPCRequest extends RESTClient {
    
    protected static final Logger s_logger = Logger.getLogger(EPCRequest.class);
    
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
	    String freemarkerTemplate = "{"
	            + "\"userid\": $userid,"
	            + "\"name\": \"$name\","
	            + "\"domain\": \"$domain\""
	            + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("name", name);
	    params.put("userid", this.userid);
	    params.put("domain", this.domain);
	    String ret = Utils.velocityProcess(params, freemarkerTemplate);
	    s_logger.info("Execute -> createEPC function.");
	    return this.RequestAPP("post", "epcs", ret, null);
	}
	
	public ResultSet modifyEPC(String name, int epcid) {
	    /*
	     * @params: name, epcid
	     * @method: PATCH /v1/epcs/<epcid>
	     */
	    String freemarkerTemplate = "{"
	            + "\"userid\": $userid,"
	            + "\"name\": \"$name\""
	            + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("userid", this.userid);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("execute -> modifyEPC function.");
        return this.RequestAPP("patch", "epcs", ret, String.valueOf(epcid));
	}
	
	public ResultSet getEPCs() {
	    /*
         * @params: 
         * @method: GET /v1/epcs
         */
	    s_logger.info("execute -> getEPCs function.");
	    return this.RequestAPP("get", "epcs", null, null);
	}
	
	public ResultSet getEPCById(int epcid) {
	    /*
	     * @params: epc_id
	     * @method: GET /v1/epcs/<epc_id>/
	     */
	    s_logger.info("execute -> getEPCById function.");
        return this.RequestAPP("get", "epcs", null, String.valueOf(epcid));
    }
	
	public ResultSet deleteEPC(int epcid) {
	    /*
         * @params: epc_id
         * @method: DELETE /v1/epcs/<epc_id>/
         */
	    s_logger.info("execute -> deleteEPC function.");
        return this.RequestAPP("delete", "epcs", null, String.valueOf(epcid));
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
	
	public ResultSet DeleteEPCIfExist(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet epcs = getEPCByName(name);
        if (epcs.content()!=null) {
            int epcid = getIntRecordsByKey(epcs, "ID");
            return this.deleteEPC(epcid);
        } else {
            return simpleResponse("not exist");
        }
    }
	
}
