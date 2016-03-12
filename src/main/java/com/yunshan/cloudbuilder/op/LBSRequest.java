package com.yunshan.cloudbuilder.op;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;
import com.yunshan.cloudbuilder.VMState;

public class LBSRequest extends RESTClient {
    
    protected static final Logger s_logger = Logger.getLogger(LBSRequest.class);

    private String domain;
    private int userid;
    private String pool_lcuuid;
    private EPCRequest epcRequest = null;
    
    private static final int BANDWIDTH = 10485760;

    public LBSRequest(String host, String poolName, String domain, int userid) {
        super(host);
        this.domain=domain;
        this.userid=userid;
        epcRequest = new EPCRequest(host, domain, userid);
        RESRequest rs = new RESRequest(host);
        this.pool_lcuuid = rs.getPoolLcuuidByName(poolName);
    }

    private ResultSet createLB(String name, String launch_server, String product_spec, String os,
            String vcpu_num, String mem_size, String sys_disk_size, String user_disk_size,
            String pool_lcuuid) {
        /*
         * @params: name, launch_server, product_spec, os, vcpu_num, mem_size
         * sys_disk_size, user_disk_size, pool_lcuuid
         * 
         * @method: POST /v1/lbs/
         * 
         */
        String freemarkerTemplate = "{" 
                + "\"allocation_type\": \"manual\"," 
                + "\"userid\": $userid,"
                + "\"domain\": \"$domain\"," 
                + "\"order_id\": 20000,"
                + "\"passwd\": \"yunshan3302\"," 
                + "\"name\": \"$name\"," 
                + "\"os\": \"$os\","
                + "\"pool_lcuuid\": \"$pool_lcuuid\","
                + "\"launch_server\": \"$launch_server\","
                + "\"product_specification_lcuuid\": \"$product_spec\","
                + "\"vcpu_num\": $vcpu_num," 
                + "\"mem_size\": $mem_size,"
                + "\"sys_disk_size\": $sys_disk_size," 
                + "\"user_disk_size\": $user_disk_size,"
                + "\"role\": \"LOAD_BALANCER\"" 
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", (name != null) ? name : "lb-01");
        params.put("launch_server", launch_server);
        params.put("product_spec", product_spec);
        params.put("os", (os != null) ? os : "cenos6.5-lb");
        params.put("vcpu_num", (vcpu_num != null) ? vcpu_num : 2);
        params.put("mem_size", (mem_size != null) ? mem_size : 4096);
        params.put("sys_disk_size", (sys_disk_size != null) ? sys_disk_size : 50);
        params.put("user_disk_size", (user_disk_size != null) ? user_disk_size : 0);
        params.put("pool_lcuuid", (pool_lcuuid != null) ? pool_lcuuid : this.pool_lcuuid);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("execute -> createLB function.");
        return this.RequestAPP("post", "lbs", ret, null);
    }
    
    public ResultSet deleteLB(int lbId) {
        /*
         * @params: vmid
         * 
         * @method: DELETE /v1/lbs/
         */
        s_logger.info("execute -> deleteLB function.");
        return this.RequestAPP("delete", "lbs", null, String.valueOf(lbId));
    }

    public ResultSet modifyLB(int lbId, String name, String vcpu_num, String mem_size,
            String product_spec) {
        /*
         * @params: vmid, name, vcpu_num, mem_size, product_spec
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"modify\"," 
                + "\"name\": \"$name\","
                + "\"vcpu_num\": \"$vcpu_num\"," 
                + "\"mem_size\": \"$mem_size\","
                + "\"sys_disk_size\": \"30\"," 
                + "\"user_disk_size\": \"0\","
                + "\"product_specification_lcuuid\": \"$product_spec\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("vcpu_num", vcpu_num);
        params.put("mem_size", mem_size);
        params.put("product_spec", product_spec);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("execute -> modifyLB function.");
        return this.RequestAPP("patch", "lbs", ret, String.valueOf(lbId));
    }

    private ResultSet addLBToEPC(int lbId, int epcId) {
        /*
         * @params: vmid, epc_id
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"setepc\"," 
                + "\"epc_id\": $epc_id"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("epc_id", epcId);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("execute -> addLBToEPC function.");
        return this.RequestAPP("patch", "lbs", ret, String.valueOf(lbId));
    }
    
    public ResultSet getLBById(int lbId) {
        /*
         * @params: vmid
         * 
         */
        s_logger.info("execute -> getLBById function.");
        return this.RequestAPP("get", "lbs", null, String.valueOf(lbId));
    }
    
    public ResultSet createLBListener(String name, String protocol, String ip, int port,
            String balance, String lcuuid) {
        /*
         * @params: lcuuid, name, protocol,ip,port,balance
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String freemarkerTemplate = "{" 
                + "\"NAME\": \"$name\"," 
                + "\"PROTOCOL\": \"$protocol\"," 
                + "\"IP\": \"$ip\"," 
                + "\"PORT\": $port," 
                + "\"BALANCE\": \"$balance\"," 
                + "\"SESSION\": {\"SESSION_STRICKY\": \"NONE\"},"
                + "\"HEALTH_CHECK\": 0"
                + "}";
                        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("protocol", (protocol != null) ? protocol : "http");
        params.put("ip", ip);
        params.put("port", port);
        params.put("balance", (balance != null) ? balance : "roundrobin");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        String param = lcuuid + "/lb-listeners/";
        s_logger.info("execute -> createLBListener function.");
        return this.RequestAPP("post", "lbs", ret, param);
    }
    
    public ResultSet deleteLBListener(String lb_lcuuid, String lb_listener_lcuuid) {
        /*
         * @params: lb_lcuuid, lb_listener_lcuuid
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String param = lb_lcuuid + "/lb-listeners/" + lb_listener_lcuuid;
        s_logger.info("execute -> deleteLBListener function.");
        return this.RequestAPP("delete", "lbs", null, param);
    }
    
    public ResultSet getLBListener(String lblcuuid) {
        /*
         * @params: vmid
         * @method: GET /v1/lbs/<lb_lcuuid>/lb-listeners
         */
        String param = lblcuuid + "/lb-listeners";
        s_logger.info("execute -> getLBListener function.");
        return this.RequestAPP("get", "lbs", null, param);
    }
    
    public ResultSet createLBForwardRules(String name, String type, String content) {
        /*
         * @params: name, type, content
         * @method: POST /v1/lb-forward-rules/
         */
        String freemarkerTemplate = "{" 
                + "\"NAME\": \"$name\"," 
                + "\"TYPE\": \"$type\"," 
                + "\"CONTENT\": \"$content\"," 
                + "\"USERID\": $userid" 
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("type", type);
        params.put("content", content);
        params.put("userid", userid);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("execute -> createLBForwardRules function.");
        return this.RequestAPP("post", "lb-forward-rules", ret, null);
    }

    public ResultSet setDefaultGW(int lbId, String gateway) {
        /*
         * @params: vmid, gateway
         * @method: PATCH /v1/vms
         */
        String freemarkerTemplate = "{"
                + "\"action\": \"modifyinterface\","
                + "\"gateway\": \"$gateway\","
                + "\"loopback_ips\": [],"
                + "\"interfaces\": []"
                + "}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gateway", gateway);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("execute -> createLBForwardRules function.");
        return this.RequestAPP("patch", "vms", ret, String.valueOf(lbId));
    }
    
    public ResultSet putLBListener(String name, String protocol, String ip, int port,
            String balance, String lb_lcuuid, String lb_listener_lcuuid, 
            List<Map<String, String>> bk_vms) {
        /*
         * @params: lb_lcuuid, lb_listener_lcuuid, name, protocol, ip,
         * @params: port, balance, bk_vms
         * @method: POST /v1/lbs/<lb_lcuuid>/lb-listeners/<lb_listener_lcuuid>/
         */
        String freemarkerTemplate = "{" 
                + "\"NAME\": \"$name\"," 
                + "\"PROTOCOL\": \"$protocol\"," 
                + "\"IP\": \"$ip\"," 
                + "\"PORT\": $port," 
                + "\"BALANCE\": \"$balance\"," 
                + "\"SESSION\": {\"SESSION_STRICKY\": \"NONE\"},"
                + "\"HEALTH_CHECK\": 0,"
                + "\"BK_VMS\": \"$bk_vms\""
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("protocol", (protocol != null) ? protocol : "http");
        params.put("ip", ip);
        params.put("port", port);
        params.put("balance", (balance != null) ? balance : "roundrobin");
        params.put("bk_vms", bk_vms);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        String param = lb_lcuuid + "/lb-listeners/" + lb_listener_lcuuid;
        s_logger.info("execute -> putLBListener function.");
        return this.RequestAPP("put", "lbs", ret, param);
    }
    
    public ResultSet patchLBListenerbkVms(String state,
            String vm_lcuuid, String lb_lcuuid, String lb_listener_lcuuid) {
        /*
         * @params: lb_lcuuid,
                 lb_listener_lcuuid,
                 vm_lcuuid_1,
                 state
         * @params: port, balance, bk_vms
         * @method: POST /v1/lbs/<lb_lcuuid>/lb-listeners/<lb_listener_lcuuid>/
         */
        String freemarkerTemplate = "{" 
                + "\"STATE\": \"$state\"" 
                + "}";
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", (state != null) ? state : "ENABLE");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        String param = lb_lcuuid + "/lb-listeners/" + lb_listener_lcuuid + "/lb-bk-vms/" + vm_lcuuid;
        return this.RequestAPP("patch", "lbs", ret, param);
    }
    
    private ResultSet attachOneLanInterface(int lbId, int index, int state, 
            String vl2_lcuuid, int vl2_net_index, String address) {
        /*
         * @params: lbId, index, state, vl2_lcuuid, vl2_net_index, address
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmLanTmpl = "{"
                + "\"state\": $state,"
                + "\"if_type\": \"LAN\","
                + "\"lan\": {"
                + "\"vl2_lcuuid\": \"$vl2_lcuuid\","
                + "\"ips\": [{\"vl2_net_index\": $vl2_net_index, \"address\": \"$address\"}],"
                + "\"qos\": {\"min_bandwidth\": 0, \"max_bandwidth\": 0}"
                + "}"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", state);
        params.put("vl2_lcuuid", vl2_lcuuid);
        params.put("vl2_net_index", vl2_net_index);
        params.put("address", address);
        String ret = Utils.velocityProcess(params, vmLanTmpl);
        String param = lbId + "/interfaces/" + index;
        return this.RequestAPP("put", "vms", ret, param);
    }
    
    private ResultSet attachOneWanInterface(int lbId, int index, int state, 
            String ip_resource_lcuuid, int bandwidth) {
        /*
         * @params: vmid, index, state, ip_resource_lcuuid
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmWanTmpl = "{"
                + "\"state\": $state,"
                + "\"if_type\": \"WAN\","
                + "\"wan\": {"
                + "\"ips\": [{\"ip_resource_lcuuid\": \"$ip_resource_lcuuid\"}],"
                + "\"qos\": {\"min_bandwidth\": $bandwidth, \"max_bandwidth\": $bandwidth}"
                + "}"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", state);
        params.put("ip_resource_lcuuid", ip_resource_lcuuid);
        params.put("bandwidth", (bandwidth != 0) ? bandwidth : BANDWIDTH);
        String ret = Utils.velocityProcess(params, vmWanTmpl);
        String param = lbId + "/interfaces/" + index;
        return this.RequestAPP("put", "vms", ret, param);
    }
    
    private ResultSet attachMultiInterface(int lbId, String gateway, 
            List<Map<String, Object>> interfaces) {
        /*
         * @params: vmid, gateway, interface
         * interface = [
         *   {state, if_index, vl2_lcuuid, vl2_net_index, address},
         *   {state, if_index, ip_resource_lcuuid, bandwidth}
         * ]
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmLanTmpl = "{"
                + "\"state\": $state,"
                + "\"if_type\": \"LAN\","
                + "\"lan\": {"
                + "\"vl2_lcuuid\": \"$vl2_lcuuid\","
                + "\"ips\": [{\"vl2_net_index\": $vl2_net_index, \"address\": \"$address\"}],"
                + "\"qos\": {\"min_bandwidth\": 0, \"max_bandwidth\": 0}"
                + "}"
                + "}";
        String vmWanTmpl = "{"
                + "\"state\": $state,"
                + "\"if_type\": \"WAN\","
                + "\"wan\": {"
                + "\"ips\": [{\"ip_resource_lcuuid\": \"$ip_resource_lcuuid\"}],"
                + "\"qos\": {\"min_bandwidth\": $bandwidth, \"max_bandwidth\": $bandwidth}"
                + "}"
                + "}";
        List<String> interf = new ArrayList<String>();
        
        for(Map<String, Object> map : interfaces) {
            if (map.containsKey("ip_resource_lcuuid")) {
                interf.add(Utils.velocityProcess(map, vmWanTmpl));
            } else {
                interf.add(Utils.velocityProcess(map, vmLanTmpl));
            }
        }
        String finalTmpl = "{"
                + "\"action\": \"modifyinterface\","
                + "\"gateway\": \"$gateway\","
                + "\"loopback_ips\": [],"
                + "\"interfaces\": $interface_data"
                + "}";
        String interface_data = "[" + String.join(",", interf) + "]";
        Map<String, Object> patchData = new HashMap<String, Object>();
        patchData.put("gateway", gateway);
        patchData.put("interface_data", interface_data);
        String finalret = Utils.velocityProcess(patchData, finalTmpl);
        return this.RequestAPP("patch", "vms", finalret, String.valueOf(lbId));
    }

    public ResultSet attachIPAddress(int lbId, int index, String vl2_lcuuid, int vl2_net_index, 
            String address, String ip_resource_lcuuid) {
        if (ip_resource_lcuuid==null) {
            return this.attachOneLanInterface(lbId, index, 1, vl2_lcuuid, vl2_net_index, address);
        } else {
            return this.attachOneWanInterface(lbId, index, 1, ip_resource_lcuuid, 0);
        }
    }
    
    public ResultSet detachIPAddress(int lbId, int index, String vl2_lcuuid, int vl2_net_index, 
            String address, String ip_resource_lcuuid) {
        if (ip_resource_lcuuid==null) {
            return this.attachOneLanInterface(lbId, index, 2, vl2_lcuuid, vl2_net_index, address);
        } else {
            return this.attachOneWanInterface(lbId, index, 2, ip_resource_lcuuid, 0);
        }
    }
    
    public ResultSet attachMultiIPAddress(String name, String gateway, 
            List<Map<String, Object>> interfaces) {
        int lbId = this.getLBIdByName(name);
        return this.attachMultiInterface(lbId, gateway, interfaces);
    }
    
    public ResultSet detachMultiIPAddress(int lbId, String gateway, 
            List<Map<String, Object>> interfaces) {
        //int state = 2;
        return this.attachMultiInterface(lbId, gateway, interfaces);
    }
    
    private ResultSet getLBs() {
        /*
         * @params:
         * 
         * @method: GET /v1/lbs/
         */
        return this.RequestAPP("get", "lbs", null, null);
    }

    public ResultSet getLBByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getLBs();
        return filterRecordsByKey(resultSet, "NAME", name);
    }

    public ResultSet setLBToEPC(String name, String epcName) {
        /*
         * @params: name, epc_id
         * 
         */
        int lbId = getLBIdByName(name);
        int epcId = epcRequest.getEPCIdByName(epcName);
        return this.addLBToEPC(lbId, epcId);
    }
    
    public ResultSet getLBListenerByName(String name, String lblcuuid) {
        /*
         * @params: name, epc_id
         * 
         */
        ResultSet resultSet = this.getLBListener(lblcuuid);
        return filterRecordsByKey(resultSet, "NAME", name);
    }
    
    public int getLBIdByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getLBByName(name);
        if (resultSet.content()!=null) {
            return getIntRecordsByKey(resultSet, "ID");
        } else {
            return 0;
        }
    }
    
    public String getLBUuidByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getLBByName(name);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }
    
    public VMState getVMStatusById(int lbId) {
        /*
         * @params: lbid
         * 
         */
        ResultSet resultSet = this.getLBById(lbId);
        if (resultSet.content()!=null) {
            int vmState = getIntRecordsByKey(resultSet, "STATE");
            return VMState.getVMStateByIndex(vmState);
        } else {
            return null;
        }
    }
    
    public String getLBCtrlIPById(int vmid) {
        /*
         * @params: vmid
         * 
         */
        ResultSet resultSet = this.getLBById(vmid);
        if (resultSet.content()!=null) {
            return resultSet.content().getAsJsonObject().get("INTERFACES").
                    getAsJsonArray().get(6).getAsJsonObject().get("CONTROL").
                    getAsJsonObject().get("IP").getAsString();
        } else {
            return "0.0.0.0";
        }
    }
    
    public ResultSet createLBListenerIfNotExist(String name, String lcuuid, String protocol,
            String ip, int port, String balance) {
        /*
         * @params: name, launch_server, product_spec, os, vcpu_num, mem_size
         *        sys_disk_size, user_disk_size
         * 
         */
        ResultSet resultSet = this.getLBListenerByName(name, lcuuid);
        if (resultSet.content()==null) {
            return this.createLBListener(name, protocol, ip, port, balance, lcuuid);
        } else {
            return resultSet;
        }
    }

    public ResultSet queryAsyncJobResult(int vmId, VMState vmState) {
        int count = 0;
        boolean ret = false;
        while (true) {
            try {
                Thread.sleep(10000);
                count += 1;
                VMState state = this.getVMStatusById(vmId);
                if (state.equals(vmState)) {
                    ret = true;
                    break;
                }
                if (count > 10) {
                    break;
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return (ret) ? simpleResponse("success"): simpleResponse("failed");
    }
        
    public ResultSet createLBIfNotExist(String name, String launch_server, String product_spec, 
            String os, String vcpu_num, String mem_size, String sys_disk_size, 
            String user_disk_size, String pool_lcuuid) {
        /*
         * @params: name, launch_server, product_spec, os, vcpu_num, mem_size
         *        sys_disk_size, user_disk_size
         * 
         */
        ResultSet resultSet = this.getLBByName(name);
        if (resultSet.content()==null) {
            return this.createLB(name, launch_server, product_spec, os, vcpu_num, 
                    mem_size, sys_disk_size, user_disk_size, this.pool_lcuuid);
        } else {
            return resultSet;
        }
    }
    
    public ResultSet deleteLBIfExist(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet resultSet = this.getLBByName(name);
        if (resultSet.content()!=null) {
            int vmId = getIntRecordsByKey(resultSet, "ID");
            return this.deleteLB(vmId);
        } else {
            return simpleResponse("lb is not exists");
        }
    }
    
}
