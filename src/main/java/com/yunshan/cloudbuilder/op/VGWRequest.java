package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class VGWRequest extends RESTClient {
    
    protected static final Logger s_logger = Logger.getLogger(VGWRequest.class);
    
    private String domain;
    private int userid;
    private EPCRequest epcRequest = null;

	public VGWRequest(String host, String domain, int userid) {
		super(host);
		this.domain = domain;
		this.userid = userid;
		epcRequest = new EPCRequest(host, domain, userid);
	}
	
	private ResultSet createVgateway(String name, String product_spec) {
        /*
         * @params: name, product_spec
         * @method: POST /v1/vgateways/
         * 
         */
        String velocityTemplate = "{"
                + "\"USERID\": $userid,"
                + "\"WANS\": 3,"
                + "\"LANS\": 3,"
                + "\"NAME\": \"$name\","
                + "\"DOMAIN\": \"$domain\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$product_spec\""
                + "}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.POST, "vgateways", ret, null);
    }

	private ResultSet createVgatewayManually(String name, String product_spec, String pool_lcuuid, 
	        String launch_server) {
        /*
         * @params: name, product_spec, pool_lcuuid, launch_server
         * @method: POST /v1/vgateways/
         * 
         */
        String velocityTemplate = "{"
                + "\"USERID\": $userid,"
                + "\"WANS\": 3,"
                + "\"LANS\": 3,"
                + "\"RATE\": 1572864000,"
                + "\"NAME\": \"$name\","
                + "\"DOMAIN\": \"$domain\","
                + "\"GW_POOL_LCUUID\": \"$pool_lcuuid\","
                + "\"GW_LAUNCH_SERVER\": \"$launch_server\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$product_spec\""
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        params.put("pool_lcuuid", pool_lcuuid);
        params.put("launch_server", launch_server);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.POST, "vgateways", ret, null);
    }
    
	private ResultSet getVgateways() {
        /*
         * @params: 
         * @method: GET /v1/vgateways/
         * 
         */
        return this.RequestTalker(HttpMethod.GET, "vgateways", null, null);
    }
	
	public ResultSet getVgatewayByLcuuid(String lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: GET /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        return this.RequestTalker(HttpMethod.GET, "vgateways", null, lcuuid);
    }
	
	public ResultSet modifyVgatewayLaunchServer(String vgateway_lcuuid, String launch_server) {
        /*
         * @params: vgateway_lcuuid, launch_server
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        String velocityTemplate = "{"
                + "\"GW_LAUNCH_SERVER\": \"$launch_server\""
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("launch_server", launch_server);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.PATCH, "vgateways", ret, vgateway_lcuuid);
    }
	
	private ResultSet modifyVgatewayEPCId(String vgateway_lcuuid, int epc_id) {
        /*
         * @params: vgateway_lcuuid, epc_id
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        String velocityTemplate = "{"
                + "\"EPC_ID\": $epc_id"
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("epc_id", epc_id);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.PATCH, "vgateways", ret, vgateway_lcuuid);
    }
	
	private ResultSet getSnat(String vgateway_lcuuid) {
	    /*
         * @params: vgateway_lcuuid
         * @method: get /v1/vgateways/<vgateway_lcuuid>/snats/
         */
	    String param = vgateway_lcuuid + "/snats";
	    return this.RequestTalker(HttpMethod.GET, "vgateways", null, param);
	}
	
	private ResultSet getDnat(String vgateway_lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: get /v1/vgateways/<vgateway_lcuuid>/dnats/
         */
        String param = vgateway_lcuuid + "/dnats";
        return this.RequestTalker(HttpMethod.GET, "vgateways", null, param);
    }
	
	public ResultSet getRoutes(String vgateway_lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: get /v1/vgateways/<vgateway_lcuuid>/routes/
         */
        String param = vgateway_lcuuid + "/routes";
        return this.RequestTalker(HttpMethod.GET, "vgateways", null, param);
    }
	
	private ResultSet getForwardAcls(String vgateway_lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: get /v1/vgateways/<vgateway_lcuuid>/forward_acls/
         */
        String param = vgateway_lcuuid + "/forward_acls";
        return this.RequestTalker(HttpMethod.GET, "vgateways", null, param);
    }
	
	private ResultSet getVpns(String vgateway_lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: get /v1/vgateways/<vgateway_lcuuid>/dnats/
         */
        String param = vgateway_lcuuid + "/vpns";
        return this.RequestTalker(HttpMethod.GET, "vgateways", null, param);
    }
	
	private ResultSet modifyVgatewaySnat(String vgateway_lcuuid, String name, String isp, String if_index, 
	        String sip1, String sip2, String dip, boolean override) {
        /*
         * @params: vgateway_lcuuid name, isp, if_index, sip1, sip2, dip
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/snats/
         * 
         */
        String velocityTemplate = "{"
                + "\"NAME\": \"$name\","
                + "\"STATE\": 1,"
                + "\"RULE_ID\": $ruleId,"
                + "\"ISP\": \"$isp\","
                + "\"PROTOCOL\": 0,"
                + "\"MATCH\": {"
                + "\"IF_TYPE\": \"ANY\","
                + "\"IF_INDEX\": 0,"
                + "\"MIN_ADDRESS\": \"$sip1\","
                + "\"MAX_ADDRESS\": \"$sip2\","
                + "\"MIN_PORT\": 1,"
                + "\"MAX_PORT\": 65535"
                + "}"
                + "\"TARGET\": {"
                + "\"IF_TYPE\": \"WAN\","
                + "\"IF_INDEX\": \"$if_index\","
                + "\"MIN_ADDRESS\": \"$dip\","
                + "\"MAX_ADDRESS\": \"$dip\","
                + "\"MIN_PORT\": 1,"
                + "\"MAX_PORT\": 65535"
                + "}"
                + "}";
        int ruleId = 1;
        List<String> interf = new ArrayList<String>();
        if (!override) {
            ResultSet resultSet = this.getSnat(vgateway_lcuuid);
            if (resultSet.content()!=null) {
                interf = Arrays.asList(StringUtils.substringBetween(resultSet.content().toString(), "[", "]").split(","));
                ruleId += interf.size();
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("if_index", if_index);
        params.put("isp", isp);
        params.put("sip1", sip1);
        params.put("sip2", sip2);
        params.put("dip", dip);
        params.put("ruleId", ruleId);
        interf.add(Utils.velocityProcess(params, velocityTemplate));
        String finalData = (interf.size() > 1) ? "[" + String.join(",", interf) + "]" : String.join(",", interf);
        String param = vgateway_lcuuid + "/snats";
        return this.RequestTalker(HttpMethod.PUT, "vgateways", finalData, param);
    }
	
	private ResultSet modifyVgatewayDnat(String vgateway_lcuuid, String name, String isp, String if_index, 
            String sip, int sport, String dip, int dport, boolean override) {
        /*
         * @params: vgateway_lcuuid, name, isp, if_index, sip, sport, dip, dport, override
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/dnats/
         * 
         */
        String velocityTemplate = "{"
                + "\"NAME\": \"$name\","
                + "\"STATE\": 1,"
                + "\"RULE_ID\": $ruleId,"
                + "\"ISP\": \"$isp\","
                + "\"PROTOCOL\": 0,"
                + "\"MATCH\": {"
                + "\"IF_TYPE\": \"WAN\","
                + "\"IF_INDEX\": \"$if_index\","
                + "\"MIN_ADDRESS\": \"$sip\","
                + "\"MAX_ADDRESS\": \"$sip\","
                + "\"MIN_PORT\": $sport,"
                + "\"MAX_PORT\": $sport"
                + "}"
                + "\"TARGET\": {"
                + "\"IF_TYPE\": \"ANY\","
                + "\"IF_INDEX\": 0,"
                + "\"MIN_ADDRESS\": \"$dip\","
                + "\"MAX_ADDRESS\": \"$dip\","
                + "\"MIN_PORT\": $dport,"
                + "\"MAX_PORT\": $dport"
                + "}"
                + "}";
        int ruleId = 1;
        List<String> interf = new ArrayList<String>();
        if (!override) {
            ResultSet resultSet = this.getDnat(vgateway_lcuuid);
            if (resultSet.content()!=null) {
                interf = Arrays.asList(StringUtils.substringBetween(resultSet.content().toString(), "[", "]").split(","));
                ruleId += interf.size();
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("if_index", if_index);
        params.put("isp", isp);
        params.put("sip", sip);
        params.put("sport", sport);
        params.put("dip", dip);
        params.put("dport", dport);
        params.put("ruleId", ruleId);
        interf.add(Utils.velocityProcess(params, velocityTemplate));
        String finalData = (interf.size() > 1) ? "[" + String.join(",", interf) + "]" : String.join(",", interf);
        String param = vgateway_lcuuid + "/dnats";
        return this.RequestTalker(HttpMethod.PUT, "vgateways", finalData, param);
    }
	
	private ResultSet modifyVgatewayAcl(String vgateway_lcuuid, String name, String isp, String if_index, 
            String sip1, String sip2, int sport, boolean override) {
        /*
         * @params: vgateway_lcuuid, name, isp, if_index, sip, sport, dip, dport, override
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/forward_acls/
         * 
         */
        String velocityTemplate = "{"
                + "\"NAME\": \"$name\","
                + "\"STATE\": 1,"
                + "\"RULE_ID\": $ruleId,"
                + "\"ISP\": \"$isp}\","
                + "\"PROTOCOL\": 0,"
                + "\"MATCH_SRC\": {"
                + "\"IF_TYPE\": \"LAN\","
                + "\"IF_INDEX\": \"$if_index\","
                + "\"MIN_ADDRESS\": \"$sip1\","
                + "\"MAX_ADDRESS\": \"$sip2\","
                + "\"MIN_PORT\": $sport,"
                + "\"MAX_PORT\": $sport"
                + "}"
                + "\"MATCH_DST\": {"
                + "\"IF_TYPE\": \"ANY\","
                + "\"IF_INDEX\": 0,"
                + "\"MIN_ADDRESS\": \"0.0.0.0\","
                + "\"MAX_ADDRESS\": \"255.255.255.255\","
                + "\"MIN_PORT\": 1,"
                + "\"MAX_PORT\": 65535"
                + "}"
                + "}";
        int ruleId = 1;
        List<String> interf = new ArrayList<String>();
        if (!override) {
            ResultSet resultSet = this.getForwardAcls(vgateway_lcuuid);
            if (resultSet.content()!=null) {
                interf = Arrays.asList(StringUtils.substringBetween(resultSet.content().toString(), "[", "]").split(","));
                ruleId += interf.size();
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("if_index", if_index);
        params.put("isp", isp);
        params.put("sip1", sip1);
        params.put("sip2", sip2);
        params.put("sport", sport);
        params.put("ruleId", ruleId);
        interf.add(Utils.velocityProcess(params, velocityTemplate));
        String finalData = (interf.size() > 1) ? "[" + String.join(",", interf) + "]" : String.join(",", interf);
        String param = vgateway_lcuuid + "/forward_acls";
        return this.RequestTalker(HttpMethod.PUT, "vgateways", finalData, param);
    }
	
	private ResultSet modifyVgatewayVpn(String vgateway_lcuuid, String name, String local_ip_addr, 
	        String local_net_addr, String local_net_mask, String remote_ip_addr, String remote_net_addr, 
	        String remote_net_mask, String psk, boolean override) {
        /*
         * @params: vgateway_lcuuid, vpn
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/vpns/
         * 
         */
        String velocityTemplate = "{"
                + "\"NAME\": \"$name\","
                + "\"RULE_ID\": $ruleId,"
                + "\"STATE\": 1,"
                + "\"LEFT\": \"$local_ip_addr\","
                + "\"LNETWORK\": {"
                + "\"ADDRESS\": \"$local_net_addr\","
                + "\"NETMASK\": \"$local_net_mask\""
                + "}"
                + "\"RIGHT\": \"$remote_ip_addr\","
                + "\"RNETWORK\": {"
                + "\"ADDRESS\": \"$remote_net_addr\","
                + "\"NETMASK\": \"$remote_net_mask\""
                + "}"
                + "\"PSK\": \"$psk}\""
                + "}";
        int ruleId = 1;
        List<String> interf = new ArrayList<String>();
        if (!override) {
            ResultSet resultSet = this.getVpns(vgateway_lcuuid);
            if (resultSet.content()!=null) {
                interf = Arrays.asList(StringUtils.substringBetween(resultSet.content().toString(), "[", "]").split(","));
                ruleId += interf.size();
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("local_ip_addr", local_ip_addr);
        params.put("local_net_addr", local_net_addr);
        params.put("local_net_mask", local_net_mask);
        params.put("remote_ip_addr", remote_ip_addr);
        params.put("remote_net_addr", remote_net_addr);
        params.put("remote_net_mask", remote_net_mask);
        params.put("ruleId", ruleId);
        interf.add(Utils.velocityProcess(params, velocityTemplate));
        String finalData = (interf.size() > 1) ? "[" + String.join(",", interf) + "]" : String.join(",", interf);
        String param = vgateway_lcuuid + "/vpns";
        return this.RequestTalker(HttpMethod.PUT, "vgateways", finalData, param);
    }

	private ResultSet deleteVgateway(String vgateway_lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: DELETE /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        return this.RequestTalker(HttpMethod.DELETE, "vgateways", null, vgateway_lcuuid);
    }
	
	private ResultSet modifyVgateway(String vgateway_lcuuid, List<Map<String, Object>> wan_list, 
            List<Map<String, Object>> lan_list) {
        /*
         * @params: vgateway_lcuuid, wan_list, lan_list
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        class Data {
            private List<Map<String, Object>> wan_list;
            private List<Map<String, Object>> lan_list;
            private List<String> interf;
            
            public Data(List<Map<String, Object>> wan_list, List<Map<String, Object>> lan_list) {
                this.wan_list = wan_list;
                this.lan_list = lan_list;
                interf = new ArrayList<String>();
            }
            
            public String getData() {
                return (interf.size() > 1) ? "[" + String.join(",", interf) + "]" : String.join(",", interf);
            }
            
            public Data generateWanData() {
                /*
                 * @params: wan_list(if_index,ip_resource_lcuuid,min_bandwidth,max_bandwidth)
                 * 
                 */
                String velocityTemplate = "{"
                        + "\"IF_INDEX\": $if_index,"
                        + "\"STATE\": 1,"
                        + "\"IF_TYPE\": \"WAN\","
                        + "\"WAN\": { "
                        + "\"IPS\": ["
                        + "{\"IP_RESOURCE_LCUUID\": \"$ip_resource_lcuuid\"}"
                        + "],"
                        + "\"QOS\": {"
                        + "\"MIN_BANDWIDTH\": $min_bandwidth,"
                        + "\"MAX_BANDWIDTH\": $max_bandwidth"
                        + "}"
                        + "}"
                        + "}";
                int index = 1;
                for (Map<String, Object> map : wan_list) {
                    map.put("if_index", index);
                    map.put("min_bandwidth", map.get("bandwidth"));
                    map.put("max_bandwidth", map.get("bandwidth"));
                    index += 1;
                    interf.add(Utils.velocityProcess(map, velocityTemplate));
                }
                return this;
            }
            
            public Data generateLanData() {
                /*
                 * @params: lan_list(if_index,vl2_lcuuid,address)
                 * 
                 */
                String velocityTemplate = "{"
                        + "\"IF_INDEX\": $if_index,"
                        + "\"STATE\": 1,"
                        + "\"IF_TYPE\": \"LAN\","
                        + "\"LAN\": { "
                        + "\"VL2_LCUUID\": \"$vl2_lcuuid\","
                        + "\"IPS\": ["
                        + "{\"VL2_NET_INDEX\": 1, "
                        + "\"ADDRESS\": \"$address\"}"
                        + "],"
                        + "\"QOS\": {"
                        + "\"MIN_BANDWIDTH\": 0,"
                        + "\"MAX_BANDWIDTH\": 0"
                        + "}"
                        + "}"
                        + "}";
                int index = 10;
                for (Map<String, Object> map : lan_list) {
                    map.put("if_index", index);
                    index += 1;
                    
                    interf.add(Utils.velocityProcess(map, velocityTemplate));
                }
                return this;
            }
        }
        String finalData = new Data(wan_list, lan_list).generateLanData().generateWanData().getData();
        return this.RequestTalker(HttpMethod.PATCH, "vgateways", finalData, vgateway_lcuuid);
    }
	
	public ResultSet modifyVgatewayFinely(String name, List<Map<String, Object>> wan_list, 
            List<Map<String, Object>> lan_list) {
	    /*
         * @params: name, wan, lan
         * 
         */
	    String lcuuid = this.getVgatewayUuidByName(name);
	    return this.modifyVgateway(lcuuid, wan_list, lan_list);
	}
	
	public ResultSet getVgatewayByName(String name) {
	    /*
         * @params: name
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
	    ResultSet resultSet = this.getVgateways();
        return filterRecordsByKey(resultSet, "NAME", name);
	}
	
	public int getVgatewayIdByName(String name) {
        /*
         * @params: name
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        ResultSet resultSet = this.getVgatewayByName(name);
        if (resultSet.content()!=null) {
            return getIntRecordsByKey(resultSet, "ID");
        } else {
            return 0;
        }
    }

	public String getVgatewayUuidByName(String name) {
        /*
         * @params: name
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        ResultSet resultSet = this.getVgatewayByName(name);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }

	public ResultSet setSnatForVgateway(String vgwname, String name, String isp, String if_index, 
            String sip1, String sip2, String dip, boolean override) {
	    /*
         * @params: snat(name, isp, if_index, sip1, sip2, dip)
         */
	    String lcuuid = this.getVgatewayUuidByName(vgwname);
	    return this.modifyVgatewaySnat(lcuuid, name, isp, if_index, sip1, sip2, dip, override);
	}
	
	public ResultSet setDnatForVgateway(String vgwname, String name, String isp, String if_index, 
            String sip, int sport, String dip, int dport, boolean override) {
        /*
         * @params: snat(name, isp, if_index, sip1, sip2, dip)
         */
        String lcuuid = this.getVgatewayUuidByName(vgwname);
        return this.modifyVgatewayDnat(lcuuid, name, isp, if_index, sip, sport, dip, dport, override);
    }
	
	public ResultSet setForwardAclForVgateway(String vgwname, String name, String isp, String if_index, 
            String sip1, String sip2, int sport, boolean override) {
        /*
         * @params: snat(name, isp, if_index, sip1, sip2, dip)
         */
        String lcuuid = this.getVgatewayUuidByName(vgwname);
        return this.modifyVgatewayAcl(lcuuid, name, isp, if_index, sip1, sip2, sport, override);
    }
	
	public ResultSet setVpnForVgateway(String vgwname, String name, String local_ip_addr, 
            String local_net_addr, String local_net_mask, String remote_ip_addr, String remote_net_addr, 
            String remote_net_mask, String psk, boolean override) {
        /*
         * @params: snat(name, isp, if_index, sip1, sip2, dip)
         */
        String lcuuid = this.getVgatewayUuidByName(vgwname);
        return this.modifyVgatewayVpn(lcuuid, name, local_ip_addr, local_net_addr, local_net_mask, 
                remote_ip_addr, remote_net_addr, remote_net_mask, psk, override);
    }
	
	public ResultSet setEPCForVgateway(String vgwname, String epcName) {
	    /*
         * @params: name, epc_id
         */
	    String lcuuid = this.getVgatewayUuidByName(vgwname);
	    int epcId = epcRequest.getEPCIdByName(epcName);
	    return this.modifyVgatewayEPCId(lcuuid, epcId);
	}
	
	private ResultSet migrateVgateway(String name, String gw_launch_server) {
        /*
         * @params: name, gw_launch_server
         */
        String lcuuid = this.getVgatewayUuidByName(name);
        return this.modifyVgatewayLaunchServer(lcuuid, gw_launch_server);
    }
	
	public ResultSet createVgatewayIfNotExist(String name, String product_spec, String allocation_type, 
	        String gw_pool_lcuuid, String gw_launch_server) {
	    /*
         * @params: name, product_spec, allocation_type, gw_pool_lcuuid, gw_launch_server
         */
	    ResultSet resultSet = this.getVgatewayByName(name);
	    if (resultSet.content()==null) {
	        if(allocation_type.equals("AUTO")) {
	            return this.createVgateway(name, product_spec);
	        } else {
	            return this.createVgatewayManually(name, product_spec, gw_pool_lcuuid, gw_launch_server);
	        }
	    } else {
	        if (gw_launch_server!=null) {
	            return this.migrateVgateway(name, gw_launch_server);
	        } else {
	            return resultSet;
	        }
	    }
	}
	
	public ResultSet deleteVgatewayNotExist(String name) {
        /*
         * @params: name
         */
        String lcuuid = this.getVgatewayUuidByName(name);
        ResultSet resultSet = this.getVgatewayByName(name);
        if (resultSet.content()!=null) {
            return this.deleteVgateway(lcuuid);
        } else {
            return simpleResponse("vgateway is not exists");
        }
    }
	
	/*
    def modify_vgateway_finely_adv(self, **kwargs):
        '''
        @params: name, wan, lan
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_adv(vgateway_lcuuid=lcuuid,
                                           wan_list=kwargs['wan'],
                                           lan_list=kwargs['lan'])
        return result

    def set_vpns_for_vgateway(self, **kwargs):
        '''
        @params: name
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_vpns(vgateway_lcuuid=lcuuid,
                                            vpn=kwargs['vpn'])
        return result

    def get_multi_vgateway_ids_by_name(self, name_list):
        '''
        @params: name_list
        '''
        num = len(name_list)
        vgateway_id_list = list()
        args = {'method': 'get', 'command': 'vgateways'}
        vgateways = self.process_app(**args)
        for i in range(0, num):
            for vgateway in vgateways.content:
                if vgateway['NAME'] == name_list[i]:
                    vgateway_id_list.append(deepcopy(vgateway['ID']))
                    break
        return utils.QueryResult(200, vgateway_id_list)

    def _modify_vgateway_adv(self, **kwargs):
        '''
        @params: vgateway_lcuuid, wan_list, lan_list
        @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
        '''
        def generate_wan_data_adv(**kwargs):
            final_data = list()
            wan_tmpl = """{
                "IF_INDEX": $if_index,
                "STATE": 1,
                "IF_TYPE": "WAN",
                "WAN": {
                    "IPS": [
                    ],
                    "QOS": {
                        "MIN_BANDWIDTH": $min_bandwidth,
                        "MAX_BANDWIDTH": $max_bandwidth
                    }
                }
            }"""
            index = 1
            for wan_data in kwargs['wan_list']:
                wan_data.update({'if_index': index})
                if 'min_bandwidth' not in wan_data:
                    wan_data.update({'min_bandwidth': 10485760})
                if 'max_bandwidth' not in wan_data:
                    wan_data.update({'max_bandwidth': 10485760})
                json_data = utils.template_to_json(wan_tmpl, wan_data)
                temp_data = list()
                for ip in wan_data['ip_resource_lcuuids']:
                    temp_data.append(
                            {"IP_RESOURCE_LCUUID": ip['ip_resource_lcuuid']})
                json_data['WAN']['IPS'].extend(temp_data)
                index += 1
                final_data.append(json_data)
            return final_data

        def generate_lan_data_adv(**kwargs):
            final_data = list()
            lan_tmpl = """{
                "IF_INDEX": $if_index,
                "STATE": 1,
                "IF_TYPE": "LAN",
                "LAN": {
                    "VL2_LCUUID": "$vl2_lcuuid",
                    "IPS": [
                        {"VL2_NET_INDEX": 1, "ADDRESS": "$address"}
                    ],
                    "QOS": {
                        "MIN_BANDWIDTH": $min_bandwidth,
                        "MAX_BANDWIDTH": $max_bandwidth
                    }
                }
            }"""
            index = 10
            for lan_data in kwargs['lan_list']:
                lan_data.update({'if_index': index})
                if 'min_bandwidth' not in lan_data:
                    lan_data.update({'min_bandwidth': 1048576000})
                if 'max_bandwidth' not in lan_data:
                    lan_data.update({'max_bandwidth': 1048576000})
                json_data = utils.template_to_json(lan_tmpl, lan_data)
                index += 1
                final_data.append(json_data)
            return final_data

        post_data = list()
        post_data.extend(generate_wan_data_adv(**kwargs))
        post_data.extend(generate_lan_data_adv(**kwargs))
        args = {'method': 'patch', 'command': 'vgateways', 'body': post_data,
                'param': kwargs['vgateway_lcuuid']}
        return self.process_talker(**args)
*/
}
