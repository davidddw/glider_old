package com.yunshan.cloudbuilder.op;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class VGWRequest extends RESTClient {
    
    private String domain;
    private int userid;

	public VGWRequest(String host, String domain, int userid) {
		super(host);
		this.domain = domain;
		this.userid = userid;
	}
	
	private ResultSet createVgateway(String name, String product_spec) {
        /*
         * @params: name, product_spec
         * @method: POST /v1/vgateways/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"USERID\": ${userid},"
                + "\"WANS\": 3,"
                + "\"LANS\": 3,"
                + "\"NAME\": \"${name}\","
                + "\"DOMAIN\": \"${domain}\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\""
                + "}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Utils.freemarkerProcess(params, freemarkerTemplate);
        return this.RequestTalker("post", "vgateways", ret, null);
    }

	private ResultSet createVgatewayManually(String name, String product_spec, String pool_lcuuid, 
	        String launch_server) {
        /*
         * @params: name, product_spec, pool_lcuuid, launch_server
         * @method: POST /v1/vgateways/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"USERID\": ${userid},"
                + "\"WANS\": 3,"
                + "\"LANS\": 3,"
                + "\"RATE\": 1572864000,"
                + "\"NAME\": \"${name}\","
                + "\"DOMAIN\": \"${domain}\","
                + "\"GW_POOL_LCUUID\": \"${pool_lcuuid}\","
                + "\"GW_LAUNCH_SERVER\": \"${launch_server}\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\""
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        params.put("pool_lcuuid", pool_lcuuid);
        params.put("launch_server", launch_server);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Utils.freemarkerProcess(params, freemarkerTemplate);
        return this.RequestTalker("post", "vgateways", ret, null);
    }
    
	private ResultSet getVgateways() {
        /*
         * @params: 
         * @method: GET /v1/vgateways/
         * 
         */
        return this.RequestTalker("get", "vgateways", null, null);
    }
	
	private ResultSet getVgatewayByLcuuid(String lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: GET /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        return this.RequestTalker("get", "vgateways", null, lcuuid);
    }
	
	private ResultSet modifyVgatewayLaunchServer(String vgateway_lcuuid, String launch_server) {
        /*
         * @params: vgateway_lcuuid, launch_server
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"GW_LAUNCH_SERVER\": \"${launch_server}\""
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("launch_server", launch_server);
        String ret = Utils.freemarkerProcess(params, freemarkerTemplate);
        return this.RequestTalker("patch", "vgateways", ret, vgateway_lcuuid);
    }
	
	private ResultSet modifyVgatewayEPCId(String vgateway_lcuuid, int epc_id) {
        /*
         * @params: vgateway_lcuuid, epc_id
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"EPC_ID\": ${epc_id?c}"
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("epc_id", epc_id);
        String ret = Utils.freemarkerProcess(params, freemarkerTemplate);
        return this.RequestTalker("patch", "vgateways", ret, vgateway_lcuuid);
    }
	
	private ResultSet modifyVgatewaySnat(String vgateway_lcuuid, List<Map<String, Object>> snat) {
        /*
         * @params: vgateway_lcuuid
         * @params: snat(name, isp, protocol,if_index_1,min_addr_1, max_addr_1
         * min_port_1, max_port_1, if_index_2, min_addr_2, max_addr_2,min_port_2, max_port_2)
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/snats/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"NAME\": \"${name}\","
                + "\"STATE\": 1,"
                + "\"RULE_ID\": 1,"
                + "\"ISP\": \"${isp}\","
                + "\"PROTOCOL\": ${protocol},"
                + "\"MATCH\": {"
                + "\"IF_TYPE\": \"ANY\","
                + "\"IF_INDEX\": \"${if_index_1}\","
                + "\"MIN_ADDRESS\": \"1\","
                + "\"MAX_ADDRESS\": \"65535\","
                + "\"MIN_PORT\": \"${min_port_1}\","
                + "\"MAX_PORT\": \"${max_port_1}\""
                + "}"
                + "\"TARGET\": {"
                + "\"IF_TYPE\": \"WAN\","
                + "\"IF_INDEX\": \"${if_index_2}\","
                + "\"MIN_ADDRESS\": \"${min_addr_2}\","
                + "\"MAX_ADDRESS\": \"${max_addr_2}\","
                + "\"MIN_PORT\": \"${min_port_2}\","
                + "\"MAX_PORT\": \"${max_port_2}\""
                + "}"
                + "}";
        List<String> interf = new ArrayList<String>();
        for (Map<String, Object> map : snat) {
            if (!map.containsKey("protocol")) {
                map.put("protocol", 0);
            } else if (!map.containsKey("min_port_1")) {
                map.put("min_port_1", 1);
            } else if (!map.containsKey("max_port_1")) {
                map.put("max_port_1", 65535);
            } else if (!map.containsKey("min_port_2")) {
                map.put("min_port_2", 1);
            } else if (!map.containsKey("max_port_2")) {
                map.put("max_port_2", 65535);
            }
            interf.add(Utils.freemarkerProcess(map, freemarkerTemplate));
        }
        String finalData = "[" + String.join(",", interf) + "]";
        String param = vgateway_lcuuid + "/snats";
        return this.RequestTalker("put", "vgateways", finalData, param);
    }
	
	private ResultSet modifyVgatewayDnat(String vgateway_lcuuid, List<Map<String, Object>> dnat) {
        /*
         * @params: vgateway_lcuuid, dnat
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/dnats/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"NAME\": \"${name}\","
                + "\"STATE\": 1,"
                + "\"RULE_ID\": 1,"
                + "\"ISP\": \"${isp}\","
                + "\"PROTOCOL\": ${protocol?c},"
                + "\"MATCH\": {"
                + "\"IF_TYPE\": \"WAN\","
                + "\"IF_INDEX\": \"${if_index_1}\","
                + "\"MIN_ADDRESS\": \"${min_addr_1}\","
                + "\"MAX_ADDRESS\": \"${max_addr_1}\","
                + "\"MIN_PORT\": \"${min_port_1}\","
                + "\"MAX_PORT\": \"${max_port_1}\","
                + "}"
                + "\"TARGET\": {"
                + "\"IF_TYPE\": \"ANY\","
                + "\"IF_INDEX\": \"${if_index_2}\","
                + "\"MIN_ADDRESS\": \"${min_addr_2}\","
                + "\"MAX_ADDRESS\": \"${max_addr_2}\","
                + "\"MIN_PORT\": \"${min_port_2}\","
                + "\"MAX_PORT\": \"${max_port_2}\","
                + "}"
                + "}";
        List<String> interf = new ArrayList<String>();
        for (Map<String, Object> map : dnat) {
            if (!map.containsKey("protocol")) {
                map.put("protocol", 0);
            } else if (!map.containsKey("min_port_1")) {
                map.put("min_port_1", 1);
            } else if (!map.containsKey("max_port_1")) {
                map.put("max_port_1", 65535);
            } else if (!map.containsKey("min_port_2")) {
                map.put("min_port_2", 1);
            } else if (!map.containsKey("max_port_2")) {
                map.put("max_port_2", 65535);
            }
            interf.add(Utils.freemarkerProcess(map, freemarkerTemplate));
        }
        String finalData = "[" + String.join(",", interf) + "]";
        String param = vgateway_lcuuid + "/dnats";
        return this.RequestTalker("put", "vgateways", finalData, param);
    }
	
	private ResultSet modifyVgatewayVpn(String vgateway_lcuuid, List<Map<String, Object>> vpn) {
        /*
         * @params: vgateway_lcuuid, vpn
         * @method: PUT /v1/vgateways/<vgateway_lcuuid>/vpns/
         * 
         */
        String freemarkerTemplate = "{"
                + "\"NAME\": \"${name}\","
                + "\"STATE\": 1,"
                + "\"LEFT\": \"${local_ip_addr}\","
                + "\"LNETWORK\": {"
                + "\"ADDRESS\": \"${local_net_addr}\","
                + "\"NETMASK\": \"${local_net_mask}\""
                + "}"
                + "\"RIGHT\": \"${remote_ip_addr}\","
                + "\"RNETWORK\": {"
                + "\"ADDRESS\": \"${remote_net_addr}\","
                + "\"NETMASK\": \"${remote_net_mask}\""
                + "}"
                + "\"PSK\": \"${psk}\""
                + "}";
        
        List<String> interf = new ArrayList<String>();
        for (Map<String, Object> map : vpn) {
            interf.add(Utils.freemarkerProcess(map, freemarkerTemplate));
        }
        String finalData = "[" + String.join(",", interf) + "]";
        String param = vgateway_lcuuid + "/vpns";
        return this.RequestTalker("put", "vgateways", finalData, param);
    }

	private ResultSet deleteVgateway(String vgateway_lcuuid) {
        /*
         * @params: vgateway_lcuuid
         * @method: DELETE /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
        return this.RequestTalker("delete", "vgateways", null, vgateway_lcuuid);
    }
	
	private List<String> generateWanData(List<Map<String, Object>> wan_list) {
	    /*
         * @params: wan_list(if_index,ip_resource_lcuuid,min_bandwidth,max_bandwidth)
         * 
         */
	    String freemarkerTemplate = "{"
                + "\"IF_INDEX\": ${if_index?c},"
                + "\"STATE\": 1,"
                + "\"IF_TYPE\": \"WAN\","
                + "\"WAN\": { "
                + "\"IPS\": ["
                + "{\"IP_RESOURCE_LCUUID\": \"${ip_resource_lcuuid}\"}"
                + "],"
                + "\"QOS\": {"
                + "\"MIN_BANDWIDTH\": ${min_bandwidth?c},\"MAX_BANDWIDTH\": ${max_bandwidth?c}"
                + "}"
                + "}"
                + "}";
	    List<String> interf = new ArrayList<String>();
	    int index = 1;
	    for (Map<String, Object> map : wan_list) {
	        map.put("if_index", index);
	        if (map.containsKey("min_bandwidth")) {
	            map.put("min_bandwidth", 10485760);
	        } else if (map.containsKey("max_bandwidth")) {
	            map.put("max_bandwidth", 10485760);
	        }
	        index += 1;
	        interf.add(Utils.freemarkerProcess(map, freemarkerTemplate));
	    }
	    return interf;
	}
                
	private List<String> generateLanData(List<Map<String, Object>> lan_list) {
        /*
         * @params: lan_list(if_index,vl2_lcuuid,address,min_bandwidth,max_bandwidth)
         * 
         */
        String freemarkerTemplate = "{"
                + "\"IF_INDEX\": ${if_index?c},"
                + "\"STATE\": 1,"
                + "\"IF_TYPE\": \"LAN\","
                + "\"LAN\": { "
                + "\"VL2_LCUUID\": \"${vl2_lcuuid}\","
                + "\"IPS\": ["
                + "{\"VL2_NET_INDEX\": 1, \"ADDRESS\": \"${address}\"}"
                + "],"
                + "\"QOS\": {"
                + "\"MIN_BANDWIDTH\": ${min_bandwidth?c},"
                + "\"MAX_BANDWIDTH\": ${max_bandwidth?c}"
                + "}"
                + "}"
                + "}";
        List<String> interf = new ArrayList<String>();
        int index = 10;
        for (Map<String, Object> map : lan_list) {
            map.put("if_index", index);
            if (map.containsKey("min_bandwidth")) {
                map.put("min_bandwidth", 1048576000);
            } else if (map.containsKey("max_bandwidth")) {
                map.put("max_bandwidth", 1048576000);
            }
            index += 1;
            interf.add(Utils.freemarkerProcess(map, freemarkerTemplate));
        }
        return interf;
    }
	
	private ResultSet modifyVgateway(String vgateway_lcuuid, List<Map<String, Object>> lan_list, 
	        List<Map<String, Object>> wan_list) {
        /*
         * @params: vgateway_lcuuid, wan_list, lan_list
         * @method: PATCH /v1/vgateways/<vgateway_lcuuid>/
         * 
         */
	    List<String> interf = new ArrayList<String>();
	    interf.addAll(this.generateLanData(lan_list));
	    interf.addAll(this.generateWanData(wan_list));
	    String finalData = "[" + String.join(",", interf) + "]";
        return this.RequestTalker("patch", "vgateways", finalData, vgateway_lcuuid);
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
	
	public ResultSet getVgatewayIdByName(String name) {
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

	public ResultSet getVgatewayUuidByName(String name) {
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
	
	public ResultSet setSnatForVgateway(String name) {
	    /*
         * @params: name
         */
	    String lcuuid = this.getVgatewayUuidByName(name);
	    return this.modifyVgatewaySnat(lcuuid, snat);
	}
	
	def set_snats_for_vgateway(self, **kwargs):
        '''
        @params: name
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_snats(vgateway_lcuuid=lcuuid,
                                             snat=kwargs['snat'])
        return result


    def create_vgateway_if_not_exist(self, **kwargs):
        '''
        @params: name, product_spec, allocation_type,
                 gw_pool_lcuuid, gw_launch_server
        '''
        result = ''
        vgateway = self.get_vgateway_by_name(name=kwargs['name'])
        if vgateway.content is None:
            if kwargs['allocation_type'] == "AUTO":
                result = self._create_vgateway(
                    name=kwargs['name'],
                    product_spec=kwargs['product_spec'])
            else:
                result = self._create_vgateway_manual(
                    name=kwargs['name'],
                    product_spec=kwargs['product_spec'],
                    pool_lcuuid=kwargs['gw_pool_lcuuid'],
                    launch_server=kwargs['gw_launch_server'])
        else:
            if kwargs.get('gw_launch_server'):
                result = self.migrate_vgateway(
                    name=kwargs['name'],
                    gw_launch_server=kwargs['gw_launch_server'])
            else:
                result = vgateway
        return result

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

    

    def set_dnats_for_vgateway(self, **kwargs):
        '''
        @params: name
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_dnats(vgateway_lcuuid=lcuuid,
                                             dnat=kwargs['dnat'])
        return result

    def set_vpns_for_vgateway(self, **kwargs):
        '''
        @params: name
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_vpns(vgateway_lcuuid=lcuuid,
                                            vpn=kwargs['vpn'])
        return result

    def migrate_vgateway(self, **kwargs):
        '''
        @params: name, gw_launch_server
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_launch_server(
            vgateway_lcuuid=lcuuid,
            launch_server=kwargs['gw_launch_server'])
        return result

    def set_epc_for_vgateway(self, **kwargs):
        '''
        @params: name, epc_id
        '''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        result = self._modify_vgateway_epc_id(vgateway_lcuuid=lcuuid,
                                              epc_id=kwargs['epc_id'],
                                              info=kwargs['name'])
        return result

    def delete_vgateway_if_exist(self, **kwargs):
        '''
        @params: name
        '''
        result = ''
        lcuuid = self.get_vgateway_lcuuid_by_name(name=kwargs['name'])
        vgateway = self.get_vgateway_by_name(name=kwargs['name'])
        if vgateway.content is not None:
            result = self._delete_vgateway(vgateway_lcuuid=lcuuid,
                                           info=kwargs['name'])
        else:
            result = {
                "DESCRIPTION": "",
                "OPT_STATUS": "SUCCESS"
            }
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
	    VGWRequest rc = new VGWRequest("10.33.37.28", "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
	    System.out.println(rc.DeleteEPCIfNotExist("ddd"));
	    //System.out.println(rc.getEPCByName("ddw"));
    }
	
}
