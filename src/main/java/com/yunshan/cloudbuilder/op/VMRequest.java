package com.yunshan.cloudbuilder.op;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class VMRequest extends RESTClient {

    private String domain;
    private int userid;
    private String pool_lcuuid;

    public VMRequest(String host, String poolName, String domain, int userid) {
        super(host);
        this.domain=domain;
        this.userid=userid;
        RESRequest rs = new RESRequest(host);
        this.pool_lcuuid = rs.getPoolLcuuidByName(poolName);
    }

    public ResultSet createVM(String name, String launch_server, String product_spec, String os,
            String vcpu_num, String mem_size, String sys_disk_size, String user_disk_size,
            String pool_lcuuid) {
        /*
         * @params: name, launch_server, product_spec, os, vcpu_num, mem_size
         * sys_disk_size, user_disk_size, pool_lcuuid
         * 
         * @method: POST /v1/vms/
         * 
         */
        String vmTmpl = "{" 
                + "\"allocation_type\": \"manual\"," 
                + "\"userid\": ${userid},"
                + "\"domain\": \"${domain}\"," 
                + "\"order_id\": 20000,"
                + "\"passwd\": \"yunshan3302\"," 
                + "\"name\": \"${name}\"," 
                + "\"os\": \"${os}\","
                + "\"pool_lcuuid\": \"${pool_lcuuid}\","
                + "\"launch_server\": \"${launch_server}\","
                + "\"product_specification_lcuuid\": \"${product_spec}\","
                + "\"vcpu_num\": ${vcpu_num}," 
                + "\"mem_size\": ${mem_size},"
                + "\"sys_disk_size\": ${sys_disk_size}," 
                + "\"user_disk_size\": ${user_disk_size},"
                + "\"role\": \"GENERAL_PURPOSE\"" 
                + "}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("launch_server", launch_server);
        params.put("product_spec", product_spec);
        params.put("os", os);
        params.put("vcpu_num", (vcpu_num != null) ? vcpu_num : 1);
        params.put("mem_size", (mem_size != null) ? mem_size : 1024);
        params.put("sys_disk_size", (sys_disk_size != null) ? sys_disk_size : 50);
        params.put("user_disk_size", (user_disk_size != null) ? user_disk_size : 0);
        params.put("pool_lcuuid", (pool_lcuuid != null) ? pool_lcuuid : this.pool_lcuuid);
        params.put("userid", this.userid);
        params.put("domain", this.domain);
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("post", "vms", ret, null);
    }

    public ResultSet deleteVM(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: DELETE /v1/vms/
         */
        return this.RequestAPP("delete", "vms", null, vmid);
    }

    public ResultSet stopVM(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "stop");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet startVM(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "start");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet isolateVMs(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "isolate");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet reconnectVMs(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "reconnect");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet createVmSnapshot(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "snapshot");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet revertVmSnapshot(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "recoversnapshot");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet deleteVmSnapshot(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"${action}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "delsnapshot");
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }

    public ResultSet modifyVMs(String vmid, String name, String vcpu_num, String mem_size,
            String product_spec) {
        /*
         * @params: vmid, name, vcpu_num, mem_size, product_spec
         * 
         * @method: PATCH /v1/vms/
         */
        String vmTmpl = "{" 
                + "\"action\": \"modify\"," 
                + "\"name\": \"${name}\","
                + "\"vcpu_num\": \"${vcpu_num}\"," 
                + "\"mem_size\": \"${mem_size}\","
                + "\"sys_disk_size\": \"30\"," 
                + "\"user_disk_size\": \"10\","
                + "\"product_specification_lcuuid\": \"${product_spec}\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("vcpu_num", vcpu_num);
        params.put("mem_size", mem_size);
        params.put("product_spec", product_spec);
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, vmid);
    }
    
    public ResultSet getVMs() {
        /*
         * @params:
         * 
         * @method: GET /v1/vms/
         */
        return this.RequestAPP("get", "vms", null, null);
    }
    
    public ResultSet getVM(String vmid) {
        /*
         * @params: vmid
         * 
         * @method: GET /v1/vms/
         */
        return this.RequestAPP("get", "vms", null, vmid);
    }

    public ResultSet getVmByUserEPC(String userid, String epc_id) {
        /*
         * @params: userid, epc_id
         * 
         * @method: GET /v1/vms/
         */
        String param = "vms?userid=" + userid + "&epc_id=" + epc_id;
        return this.RequestAPP("get", "vms", null, param);
    }
    
    public ResultSet addVMToEPC(int vmid, int epc_id) {
        /*
         * @params: vmid, epc_id
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmTmpl = "{" 
                + "\"action\": \"setepc\"," 
                + "\"epc_id\": ${epc_id?c}"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("epc_id", epc_id);
        params.put("vmid", vmid);
        String ret = Utils.freemarkerProcess(params, vmTmpl);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }
  
    public ResultSet setDefaultGW(int vmid, String gateway) {
        /*
         * @params: vmid, gateway
         * @method: PATCH /v1/vms
         */
        String vmWanTmpl = "{"
                + "\"action\": \"modifyinterface\","
                + "\"gateway\": \"${gateway}\","
                + "\"loopback_ips\": [],"
                + "\"interfaces\": []"
                + "}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gateway", gateway);
        String ret = Utils.freemarkerProcess(params, vmWanTmpl);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }
    
    public ResultSet attachOneLanInterface(int vmid, String index, String state, 
            String vl2_lcuuid, String vl2_net_index, String address) {
        /*
         * @params: vmid, index, state, vl2_lcuuid, vl2_net_index, address
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmLanTmpl = "{"
                + "\"state\": ${state},"
                + "\"if_type\": \"LAN\","
                + "\"lan\": {"
                + "\"vl2_lcuuid\": \"${vl2_lcuuid}\","
                + "\"ips\": [{\"vl2_net_index\": ${vl2_net_index}, \"address\": \"${address}\"}],"
                + "\"qos\": {\"min_bandwidth\": 0, \"max_bandwidth\": 0}"
                + "}"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", state);
        params.put("vl2_lcuuid", vl2_lcuuid);
        params.put("vl2_net_index", vl2_net_index);
        params.put("address", address);
        String ret = Utils.freemarkerProcess(params, vmLanTmpl);
        String param = vmid + "/interfaces/" + index;
        return this.RequestAPP("put", "vms", ret, param);
    }
    
    public ResultSet attachOneWanInterface(int vmid, int index, int state, 
            String ip_resource_lcuuid) {
        /*
         * @params: vmid, index, state, ip_resource_lcuuid
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmWanTmpl = "{"
                + "\"state\": ${state?c},"
                + "\"if_type\": \"WAN\","
                + "\"wan\": {"
                + "\"ips\": [{\"ip_resource_lcuuid\": \"$ip_resource_lcuuid\"}],"
                + "\"qos\": {\"min_bandwidth\": 10485760, \"max_bandwidth\": 10485760}"
                + "}"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", state);
        params.put("ip_resource_lcuuid", ip_resource_lcuuid);
        String ret = Utils.freemarkerProcess(params, vmWanTmpl);
        String param = vmid + "/interfaces/" + index;
        return this.RequestAPP("put", "vms", ret, param);
    }
    
    public ResultSet attachMultiInterface(String vmid, String gateway, List<Map<String, Object>> interfaces) {
        /*
         * @params: vmid, gateway, interface
         * interface = [
         *   {state, if_index, vl2_lcuuid, vl2_net_index, address},
         *   {state, if_index, ip_resource_lcuuid, bandwidth}
         * ]
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String vmLanTmpl = "{"
                + "\"state\": ${state},"
                + "\"if_type\": \"LAN\","
                + "\"lan\": {"
                + "\"vl2_lcuuid\": \"${vl2_lcuuid}\","
                + "\"ips\": [{\"vl2_net_index\": ${vl2_net_index}, \"address\": \"${address}\"}],"
                + "\"qos\": {\"min_bandwidth\": 0, \"max_bandwidth\": 0}"
                + "}"
                + "}";
        String vmWanTmpl = "{"
                + "\"state\": ${state},"
                + "\"if_type\": \"WAN\","
                + "\"wan\": {"
                + "\"ips\": [{\"ip_resource_lcuuid\": \"${ip_resource_lcuuid}\"}],"
                + "\"qos\": {\"min_bandwidth\": ${bandwidth}, \"max_bandwidth\": ${bandwidth}}"
                + "}"
                + "}";
        List<String> interf = new ArrayList<String>();
        
        for(Map<String, Object> map : interfaces) {
            if (map.containsKey("ip_resource_lcuuid")) {
                interf.add(Utils.freemarkerProcess(map, vmWanTmpl));
            } else {
                interf.add(Utils.freemarkerProcess(map, vmLanTmpl));
            }
        }
        String finalTmpl = "{"
                + "\"action\": \"modifyinterface\","
                + "\"gateway\": \"${gateway}\","
                + "\"loopback_ips\": [],"
                + "\"interfaces\": ${interface_data}"
                + "}";
        String interface_data = "[" + String.join(",", interf) + "]";
        Map<String, Object> patchData = new HashMap<String, Object>();
        patchData.put("gateway", gateway);
        patchData.put("interfaces", interface_data);
        String finalret = Utils.freemarkerProcess(patchData, finalTmpl);
        return this.RequestAPP("patch", "vms", finalret, vmid);
    }
    
    public ResultSet createBlockSnapshots(String vmid, String name, String product_spec) {
        /*
         * @params: vmid, name, product_spec
         * @method: POST /v1/vm_snapshot/<vmuuid>/snapshots
         */
        String snapTmpl = "{"
                + "\"NAME\": \"${name}\","
                + "\"DESCRIPTION\": \"${name}\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"${product_spec}\""
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        String ret = Utils.freemarkerProcess(params, snapTmpl);
        String param = vmid + "/snapshots";
        return this.RequestAPP("post", "vm_snapshot", ret, param);
    }

    public ResultSet deleteBlockSnapshots(String vmid, String snapid) {
        /*
         * @params: vmid, snapid
         * @method: DELETE /v1/vm_snapshot/<vmuuid>/snapshots/<snapuuid>
         */
        String param = vmid + "/snapshots/" + snapid;
        return this.RequestAPP("delete", "vm_snapshot", null, param);
    }
    
    public ResultSet getBlockSnapshots() {
        /*
         * @params: 
         * @method: GET /v1/vm_snapshot/snapshots
         */
        String param = "snapshots";
        return this.RequestAPP("get", "vm_snapshot", null, param);
    }
    
    public ResultSet revertBlockSnapshots(String vmid, String snapid) {
        /*
         * @params: vmid, snapid
         * @method: POST /v1/vm_snapshot/vm_id>/snapshots/<snap_id>/reversion
         */
        String param = vmid + "/snapshots/" + snapid + "/reversion";
        return this.RequestAPP("post", "vm_snapshot", null, param);
    }

    public static void main(String[] args) throws ClientProtocolException, IOException {
        VMRequest rc = new VMRequest("10.33.37.28", "KVMPool", 
                "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
        System.out.println(rc.addVMToEPC(1, 1));
        // System.out.println(rc.getEPCByName("ddw"));
    }

}
