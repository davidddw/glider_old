package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.utils.Util;

public class VL2Request extends RESTClient {
    
    private String domain;
    private int userid;
    private EPCRequest epcRequest = null;

	public VL2Request(String host, String domain, int userid) {
		super(host);
		this.domain = domain;
		this.userid = userid;
		epcRequest = new EPCRequest(host, domain, userid);
	}

	private ResultSet createVL2(String name, int epcId, String prefix, int netmask) {
		/*
		 * @params: name, epc_id, prefix, netmask
		 * @method: POST /v1/vl2s/
		 */
	    String velocityTemplate = "{"
	            + "\"name\": \"$!name\","
	            + "\"vlantag\": 0,"
	            + "\"epc_id\": $!epc_id,"
	            + "\"userid\": $!userid,"
	            + "\"domain\": \"$!domain\","
	            + "\"nets\": "
	            + "[{\"prefix\": \"$!prefix\",\"netmask\": $!netmask }]"
	            + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("name", name);
	    params.put("epc_id", epcId);
	    params.put("prefix", prefix);
	    params.put("netmask", netmask);
	    params.put("userid", this.userid);
	    params.put("domain", this.domain);
	    String ret = Util.velocityProcess(params, velocityTemplate);
	    return this.RequestAPP(HttpMethod.POST, "vl2s", ret, null);
	}
	
	private ResultSet getVL2s() {
	    /*
         * @params: 
         * @method: GET /v1/vl2s/
         */
        return this.RequestAPP(HttpMethod.GET, "vl2s", null, null);
    }
	
	private ResultSet modifyVL2(int vl2Id, List<Map<String, Object>> netList) {
        /*
         * @params: vl2id, netList
         * @method: PATCH /v1/vl2s/<vl2_id>/
         */
	    String velocityTemplate = "{"
	            + "\"prefix\": \"$!prefix\","
	            + "\"netmask\": $!netmask"
	            + "}";
	    List<String> interf = new ArrayList<String>();
	    
	    for(Map<String, Object> map : netList) {
	        interf.add(Util.velocityProcess(map, velocityTemplate));
        }
	    String finalvelocityTemplate = "{"
                + "\"nets\": $!net_data"
                + "}";
        Map<String, Object> patchData = new HashMap<String, Object>();
        patchData.put("net_data", interf);
        String finalret = Util.velocityProcess(patchData, finalvelocityTemplate);
        return this.RequestAPP(HttpMethod.PATCH, "vl2s", finalret, String.valueOf(vl2Id));
    }
    
	private ResultSet deleteVL2s(int vl2Id) {
        /*
         * @params: vl2id
         * @method: DELETE /v1/vl2s/<vl2_id>/
         */
        return this.RequestAPP(HttpMethod.DELETE, "vl2s", null, String.valueOf(vl2Id));
    }

    public ResultSet getVL2ByName(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet vl2s = this.getVL2s();
        return filterRecordsByKey(vl2s, "NAME", name);
    }
    
    public int getVL2IdByName(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet vl2 = getVL2ByName(name);
        return getIntRecordsByKey(vl2, "ID");
    }
    
    public ResultSet getVL2ById(int vl2Id) {
        /*
         * @params: vl2_id
         * @method: GET /v1/vl2s/<vl2_id>/
         */
        return this.RequestAPP(HttpMethod.GET, "vl2s", null, String.valueOf(vl2Id));
    }
    
    public String getVL2LcuuidByName(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet vl2 = getVL2ByName(name);
        return getStringRecordsByKey(vl2, "LCUUID");
    }

    public ResultSet CreateVL2IfNotExist(String name, String epcName, String prefix,
            int netmask) {
        /*
         * @params: name, epc_id, prefix, netmask
         * @method: 
         */
        ResultSet vl2 = getVL2ByName(name);
        int epcId = epcRequest.getEPCIdByName(epcName);
        if (vl2.content()==null) {
            return this.createVL2(name, epcId, prefix, netmask);
        } else {
            return vl2;
        }
    }

    public ResultSet deleteVL2IfExist(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet vl2 = getVL2ByName(name);
        if (vl2.content()!=null) {
            int vl2Id = getIntRecordsByKey(vl2, "ID");
            return this.deleteVL2s(vl2Id);
        } else {
            return simpleResponse("not exist");
        }
    }
    
    public ResultSet ModifyVL2Finely(String name, List<Map<String,Object>> net) {
        /*
         * @params: name, net
         * @method: 
         */
        int vl2Id = getVL2IdByName(name);
        if (vl2Id!=0) {
            return this.modifyVL2(vl2Id, net);
        } else {
            return simpleResponse("not exist");
        }
    }

}
