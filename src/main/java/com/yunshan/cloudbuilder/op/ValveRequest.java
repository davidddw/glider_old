package com.yunshan.cloudbuilder.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.utils.Util;

public class ValveRequest extends RESTClient {
    
    private String domain;
    private int userid;
    private EPCRequest epcRequest = null;
    
	public ValveRequest(String host, String domain, int userid) {
		super(host);
		this.domain = domain;
		this.userid = userid;
		epcRequest = new EPCRequest(host, domain, userid);
	}
	
	private ResultSet createValve(String name, String product_spec) {
        /*
         * @params: name, product_spec
         * @method: POST /v1/valves/
         * 
         */
        String velocityTemplate = "{"
                + "\"USERID\": $!userid,"
                + "\"WANS\": 3,"
                + "\"LANS\": 1,"
                + "\"IPS\": 9,"
                + "\"BW_WEIGHT\": {\"1\": 2, \"2\": 3, \"3\": 2, \"4\": 1},"
                + "\"NAME\": \"$!name\","
                + "\"DOMAIN\": \"$!domain\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\""
                + "}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Util.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.POST, "valves", ret, null);
    }

	private ResultSet createValveManually(String name, String product_spec, String pool_lcuuid, 
	        String launch_server) {
        /*
         * @params: name, product_spec, pool_lcuuid, launch_server
         * @method: POST /v1/valves/
         * 
         */
        String velocityTemplate = "{"
                + "\"USERID\": $!userid,"
                + "\"WANS\": 3,"
                + "\"LANS\": 1,"
                + "\"IPS\": 9,"
                + "\"BW_WEIGHT\": {\"1\": 2, \"2\": 3, \"3\": 2, \"4\": 1},"
                + "\"NAME\": \"$!name\","
                + "\"DOMAIN\": \"$!domain\","
                + "\"GW_POOL_LCUUID\": \"$!pool_lcuuid\","
                + "\"GW_LAUNCH_SERVER\": \"$!launch_server\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\""
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        params.put("pool_lcuuid", pool_lcuuid);
        params.put("launch_server", launch_server);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Util.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.POST, "valves", ret, null);
    }
    
	private ResultSet getValves() {
        /*
         * @params: 
         * @method: GET /v1/valves/
         * 
         */
        return this.RequestTalker(HttpMethod.GET, "valves", null, null);
    }
	
	public ResultSet getValveByLcuuid(String lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: GET /v1/vgateways/<valve_lcuuid>/
         * 
         */
        return this.RequestTalker(HttpMethod.GET, "valves", null, lcuuid);
    }
	
	private ResultSet modifyValveLaunchServer(String valve_lcuuid, String launch_server) {
        /*
         * @params: valve_lcuuid, launch_server
         * @method: PATCH /v1/vgateways/<valve_lcuuid>/
         * 
         */
        String velocityTemplate = "{"
                + "\"GW_LAUNCH_SERVER\": \"$!launch_server\""
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("launch_server", launch_server);
        String ret = Util.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.PATCH, "valves", ret, valve_lcuuid);
    }
	
	private ResultSet modifyValveEPCId(String valve_lcuuid, int epc_id) {
        /*
         * @params: valve_lcuuid, epc_id
         * @method: PATCH /v1/vgateways/<valve_lcuuid>/
         * 
         */
        String velocityTemplate = "{"
                + "\"EPC_ID\": $!epc_id"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("epc_id", epc_id);
        String ret = Util.velocityProcess(params, velocityTemplate);
        return this.RequestTalker(HttpMethod.PATCH, "valves", ret, valve_lcuuid);
    }
	
	private ResultSet deleteValve(String valve_lcuuid) {
        /*
         * @params: valve_lcuuid
         * @method: DELETE /v1/vgateways/<valve_lcuuid>/
         * 
         */
        return this.RequestTalker(HttpMethod.DELETE, "valves", null, valve_lcuuid);
    }
	
	private ResultSet modifyValve(String valve_lcuuid, List<Map<String, Object>> wan_list, 
	        List<Map<String, Object>> lan_list) {
        /*
         * @params: vgateway_lcuuid, wan_list, lan_list
         * @method: PATCH /v1/vgateways/<valve_lcuuid>/
         * 
         */
	    List<String> interf = new ArrayList<String>();
	    String velocityWTemplate = "{"
                + "\"IF_INDEX\": $!if_index,"
                + "\"STATE\": 1,"
                + "\"IF_TYPE\": \"WAN\","
                + "\"WAN\": { "
                + "\"IPS\": ["
                + "{\"IP_RESOURCE_LCUUID\": \"$!ip_resource_lcuuid\"}"
                + "],"
                + "\"QOS\": {"
                + "\"MIN_BANDWIDTH\": $!min_bandwidth,"
                + "\"MAX_BANDWIDTH\": $!max_bandwidth"
                + "}"
                + "}"
                + "}";
        int index = 1;
        for (Map<String, Object> map : wan_list) {
            map.put("if_index", index);
            map.put("min_bandwidth", Integer.parseInt(props.getProperty("BANDWIDTH")));
            map.put("max_bandwidth", Integer.parseInt(props.getProperty("BANDWIDTH")));
            index += 1;
            interf.add(Util.velocityProcess(map, velocityWTemplate));
        }
        
        String velocityLTemplate = "{"
                + "\"IF_INDEX\": $!if_index,"
                + "\"STATE\": 1,"
                + "\"IF_TYPE\": \"LAN\","
                + "\"LAN\": { "
                + "\"VL2_LCUUID\": \"$!vl2_lcuuid\","
                + "\"IPS\": ["
                + "{\"VL2_NET_INDEX\": 1, "
                + "\"ADDRESS\": \"0.0.0.0\"}"
                + "],"
                + "\"QOS\": {"
                + "\"MIN_BANDWIDTH\": 0,"
                + "\"MAX_BANDWIDTH\": 0"
                + "}"
                + "}"
                + "}";
        index = 10;
        for (Map<String, Object> map : lan_list) {
            map.put("if_index", index);
            index += 1;
            interf.add(Util.velocityProcess(map, velocityLTemplate));
        }
        
	    String velocityTemplate = "{"
	            + "\"GENERAL_BANDWIDTH\": 1048576,"
                + "\"INTERFACES\": $!interface_data"
                + "}";
	    Map<String, Object> params = new HashMap<String, Object>();
        params.put("interface_data", interf);
        String ret = Util.velocityProcess(params, velocityTemplate);
	    return this.RequestTalker(HttpMethod.PATCH, "valves", ret, valve_lcuuid);
    }
	
	public ResultSet modifyValveFinely(String name, List<Map<String, Object>> wan_list, 
            List<Map<String, Object>> lan_list) {
        /*
         * @params: name, wan, lan
         * 
         */
        String lcuuid = this.getValveUuidByName(name);
        return this.modifyValve(lcuuid, wan_list, lan_list);
    }
	
	public ResultSet getValveByName(String name) {
	    /*
         * @params: name
         * 
         */
	    ResultSet resultSet = this.getValves();
        return filterRecordsByKey(resultSet, "NAME", name);
	}
	
	public int getValveIdByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getValveByName(name);
        if (resultSet.content()!=null) {
            return getIntRecordsByKey(resultSet, "ID");
        } else {
            return 0;
        }
    }

	public String getValveUuidByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getValveByName(name);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }

	public ResultSet setEPCForValve(String vgwname, String epcName) {
	    /*
         * @params: name, epc_id
         */
	    String lcuuid = this.getValveUuidByName(vgwname);
	    int epcId = epcRequest.getEPCIdByName(epcName);
	    return this.modifyValveEPCId(lcuuid, epcId);
	}
	
	public ResultSet migrateValve(String name, String gw_launch_server) {
        /*
         * @params: name, gw_launch_server
         */
        String lcuuid = this.getValveUuidByName(name);
        return this.modifyValveLaunchServer(lcuuid, gw_launch_server);
    }
	
	public ResultSet createValveIfNotExist(String name, String product_spec, String allocation_type, 
	        String gw_pool_lcuuid, String gw_launch_server) {
	    /*
         * @params: name, product_spec, allocation_type, gw_pool_lcuuid, gw_launch_server
         */
	    ResultSet resultSet = this.getValveByName(name);
	    if (resultSet.content()==null) {
	        if(allocation_type.equals("AUTO")) {
	            return this.createValve(name, product_spec);
	        } else {
	            return this.createValveManually(name, product_spec, gw_pool_lcuuid, gw_launch_server);
	        }
	    } else {
	        if (gw_launch_server!=null) {
	            return this.migrateValve(name, gw_launch_server);
	        } else {
	            return resultSet;
	        }
	    }
	}
	
	public ResultSet deleteVgatewayNotExist(String name) {
        /*
         * @params: name
         */
        String lcuuid = this.getValveUuidByName(name);
        ResultSet resultSet = this.getValveByName(name);
        if (resultSet.content()!=null) {
            return this.deleteValve(lcuuid);
        } else {
            return simpleResponse("Valve is not exists");
        }
    }
	
	/*
    def modify_vgateway_finely(self, **kwargs):
        '''
        @params: name, wan, lan
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway(vgateway_lcuuid=lcuuid,
                                       wan_list=kwargs['wan'],
                                       lan_list=kwargs['lan'],
                                       info=kwargs['info'])
        return result

    def modify_vgateway_finely_adv(self, **kwargs):
        '''
        @params: name, wan, lan
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_adv(vgateway_lcuuid=lcuuid,
                                           wan_list=kwargs['wan'],
                                           lan_list=kwargs['lan'])
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

/*
    def _modify_vgateway_adv(self, **kwargs):
        '''
        @params: vgateway_lcuuid, wan_list, lan_list
        @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
        '''
        def generate_wan_data_adv(**kwargs):
            final_data = list()
            wan_tmpl = """{
                "IF_INDEX": $!if_index,
                "STATE": 1,
                "IF_TYPE": "WAN",
                "WAN": {
                    "IPS": [
                    ],
                    "QOS": {
                        "MIN_BANDWIDTH": $!min_bandwidth,
                        "MAX_BANDWIDTH": $!max_bandwidth
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
                "IF_INDEX": $!if_index,
                "STATE": 1,
                "IF_TYPE": "LAN",
                "LAN": {
                    "VL2_LCUUID": "$!vl2_lcuuid",
                    "IPS": [
                        {"VL2_NET_INDEX": 1, "ADDRESS": "$!address"}
                    ],
                    "QOS": {
                        "MIN_BANDWIDTH": $!min_bandwidth,
                        "MAX_BANDWIDTH": $!max_bandwidth
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
