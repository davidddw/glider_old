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

public class VMRequest extends RESTClient {
    
    protected static final Logger s_logger = Logger.getLogger(VMRequest.class);

    private String domain;
    private int userid;
    private String pool_lcuuid;
    
    private EPCRequest epcRequest = null;
    
    private static final int BANDWIDTH = 10485760;

    public VMRequest(String host, String poolName, String domain, int userid) {
        super(host);
        this.domain=domain;
        this.userid=userid;
        RESRequest rs = new RESRequest(host);
        epcRequest = new EPCRequest(host, domain, userid);
        this.pool_lcuuid = rs.getPoolLcuuidByName(poolName);
    }

    private ResultSet createVM(String name, String launch_server, String product_spec, String os,
            String vcpu_num, String mem_size, String sys_disk_size, String user_disk_size,
            String pool_lcuuid) {
        /*
         * @params: name, launch_server, product_spec, os, vcpu_num, mem_size
         * sys_disk_size, user_disk_size, pool_lcuuid
         * 
         * @method: POST /v1/vms/
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
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("Execute -> createEPC function.");
        return this.RequestAPP("post", "vms", ret, null);
    }

    private ResultSet deleteVM(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: DELETE /v1/vms/
         */
        s_logger.info("Execute -> createEPC function.");
        return this.RequestAPP("delete", "vms", null, String.valueOf(vmid));
    }

    private ResultSet stopVM(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "stop");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("Execute -> createEPC function.");
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    private ResultSet startVM(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "start");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("Execute -> createEPC function.");
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    public ResultSet isolateVMs(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "isolate");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        s_logger.info("Execute -> createEPC function.");
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    public ResultSet reconnectVMs(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "reconnect");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    public ResultSet createVmSnapshot(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "snapshot");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    public ResultSet revertVmSnapshot(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "recoversnapshot");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    public ResultSet deleteVmSnapshot(int vmid) {
        /*
         * @params: vmid
         * 
         * @method: PATCH /v1/vms/
         */
        String freemarkerTemplate = "{" 
                + "\"action\": \"$action\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "delsnapshot");
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }

    public ResultSet modifyVMs(int vmid, String name, String vcpu_num, String mem_size,
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
                + "\"user_disk_size\": \"10\","
                + "\"product_specification_lcuuid\": \"$product_spec\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("vcpu_num", vcpu_num);
        params.put("mem_size", mem_size);
        params.put("product_spec", product_spec);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmid));
    }
    
    private ResultSet getVMs() {
        /*
         * @params:
         * 
         * @method: GET /v1/vms/
         */
        return this.RequestAPP("get", "vms", null, null);
    }
    
    public ResultSet getVmByUserEPC(int userid, int epc_id) {
        /*
         * @params: userid, epc_id
         * 
         * @method: GET /v1/vms/
         */
        String param = "vms?userid=" + userid + "&epc_id=" + epc_id;
        return this.RequestAPP("get", "vms", null, param);
    }
    
    private ResultSet addVMToEPC(int vmId, int epcId) {
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
        params.put("vmid", vmId);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmId));
    }
  
    public ResultSet setDefaultGW(int vmId, String gateway) {
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
        return this.RequestAPP("patch", "vms", ret, String.valueOf(vmId));
    }
    
    private ResultSet attachOneLanInterface(int vmId, int index, int state, 
            String vl2_lcuuid, int vl2_net_index, String address) {
        /*
         * @params: vmid, index, state, vl2_lcuuid, vl2_net_index, address
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String freemarkerTemplate = "{"
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
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        String param = vmId + "/interfaces/" + index;
        return this.RequestAPP("put", "vms", ret, param);
    }
    
    private ResultSet attachOneWanInterface(int vmId, int index, int state, 
            String ip_resource_lcuuid) {
        /*
         * @params: vmid, index, state, ip_resource_lcuuid
         * @method: PATCH /v1/vms/<fdb_vmid>
         */
        String freemarkerTemplate = "{"
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
        params.put("bandwidth", BANDWIDTH);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        String param = vmId + "/interfaces/" + index;
        return this.RequestAPP("put", "vms", ret, param);
    }
    
    private ResultSet attachMultiInterface(int vmid, String gateway, 
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
        String freemarkerTemplate = "{"
                + "\"action\": \"modifyinterface\","
                + "\"gateway\": \"$gateway\","
                + "\"loopback_ips\": [],"
                + "\"interfaces\": $interface_data"
                + "}";
        String interface_data = "[" + String.join(",", interf) + "]";
        Map<String, Object> patchData = new HashMap<String, Object>();
        patchData.put("gateway", gateway);
        patchData.put("interface_data", interface_data);
        String finalret = Utils.velocityProcess(patchData, freemarkerTemplate);
        return this.RequestAPP("patch", "vms", finalret, String.valueOf(vmid));
    }
    
    private ResultSet createBlockSnapshots(String vmuuid, String name, String product_spec) {
        /*
         * @params: vmid, name, product_spec
         * @method: POST /v1/vm_snapshot/<vmuuid>/snapshots
         */
        String freemarkerTemplate = "{"
                + "\"NAME\": \"$name\","
                + "\"DESCRIPTION\": \"$name\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$product_spec\""
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("product_spec", product_spec);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        String param = vmuuid + "/snapshots";
        return this.RequestAPP("post", "vm_snapshot", ret, param);
    }
    
    public ResultSet getVmSnapshots(String vmuuid) {
        /*
         * @params: vmuuid
         * @method: GET /v1/vm_snapshot/snapshots
         */
        String param = vmuuid + "/snapshots";
        return this.RequestTalker("get", "vms", null, param);
    }

    private ResultSet deleteBlockSnapshots(String vmuuid, String snapuuid) {
        /*
         * @params: vmid, snapid
         * @method: DELETE /v1/vm_snapshot/<vmuuid>/snapshots/<snapuuid>
         */
        String param = vmuuid + "/snapshots/" + snapuuid;
        return this.RequestAPP("delete", "vm_snapshot", null, param);
    }
    
    private ResultSet getBlockSnapshots() {
        /*
         * @params: 
         * @method: GET /v1/vm_snapshot/snapshots
         */
        String param = "snapshots";
        return this.RequestAPP("get", "vm_snapshot", null, param);
    }
    
    private ResultSet revertBlockSnapshots(String vmuuid, String snapuuid) {
        /*
         * @params: vmid, snapid
         * @method: POST /v1/vm_snapshot/<vm_id>/snapshots/<snap_id>/reversion
         */
        String param = vmuuid + "/snapshots/" + snapuuid + "/reversion";
        return this.RequestAPP("post", "vm_snapshot", null, param);
    }
    
    public ResultSet getVmByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getVMs();
        return filterRecordsByKey(resultSet, "NAME", name);
    }
    
    public int getVmIdByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getVmByName(name);
        if (resultSet.content()!=null) {
            return getIntRecordsByKey(resultSet, "ID");
        } else {
            return 0;
        }
    }
    
    public String getVmUuidByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getVmByName(name);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }
    
    public ResultSet getVMById(int vmid) {
        /*
         * @params: vmid
         * 
         */
        return this.RequestAPP("get", "vms", null, String.valueOf(vmid));
    }
    
    public ResultSet setVMToEPC(String name, String epcName) {
        /*
         * @params: name, epc_id
         * 
         */
        int vmId = getVmIdByName(name);
        int epcId = epcRequest.getEPCIdByName(epcName);
        return this.addVMToEPC(vmId, epcId);
    }
    
    public VMState getVMStatusById(int vmid) {
        /*
         * @params: vmid
         * 
         */
        ResultSet resultSet = this.getVMById(vmid);
        if (resultSet.content()!=null) {
            int vmState = getIntRecordsByKey(resultSet, "STATE");
            return VMState.getVMStateByIndex(vmState);
        } else {
            return null;
        }
    }
    
    public String getVMLaunchServerById(int vmid) {
        /*
         * @params: vmid
         * 
         */
        ResultSet resultSet = this.getVMById(vmid);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LAUNCH_SERVER");
        } else {
            return "0.0.0.0";
        }
    }
    
    public String getVMCtrlIPById(int vmid) {
        /*
         * @params: vmid
         * 
         */
        ResultSet resultSet = this.getVMById(vmid);
        if (resultSet.content()!=null) {
            return resultSet.content().getAsJsonObject().get("INTERFACES").
                    getAsJsonArray().get(6).getAsJsonObject().get("CONTROL").
                    getAsJsonObject().get("IP").getAsString();
        } else {
            return "0.0.0.0";
        }
    }

    public ResultSet getVMSnapByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getBlockSnapshots();
        return filterRecordsByKey(resultSet, "NAME", name);
    }
    
    public String getVMSnapUuidByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getVMSnapByName(name);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }
    
    public ResultSet attachIPAddress(int vmId, int index, String vl2_lcuuid, int vl2_net_index, 
            String address, String ip_resource_lcuuid) {
        if (ip_resource_lcuuid==null) {
            return this.attachOneLanInterface(vmId, index, 1, vl2_lcuuid, vl2_net_index, address);
        } else {
            return this.attachOneWanInterface(vmId, index, 1, ip_resource_lcuuid);
        }
    }
    
    public ResultSet detachIPAddress(int vmId, int index, String vl2_lcuuid, int vl2_net_index, 
            String address, String ip_resource_lcuuid) {
        if (ip_resource_lcuuid==null) {
            return this.attachOneLanInterface(vmId, index, 2, vl2_lcuuid, vl2_net_index, address);
        } else {
            return this.attachOneWanInterface(vmId, index, 2, ip_resource_lcuuid);
        }
    }
    
    public ResultSet createVmSnapIfNotExist(String name, String vm_name,
            String product_spec) {
        /*
         * @params: name, vm_name, description, product_spec
         * @method: 
         */
        String vmUuid = this.getVmUuidByName(vm_name);
        if (vmUuid!=null) {
            ResultSet vmSnap = this.getVMSnapByName(name);
            if (vmSnap.content()==null) {
                return this.createBlockSnapshots(vmUuid, name, product_spec);
            } else {
                return vmSnap;
            }
        } else {
            return simpleResponse("vm is not exists");
        }
    }
    
    public ResultSet revertBlockSnapshotsIfExist(String vmName, String snapName) {
        /*
         * @params: vmid, snapid
         * @method: POST /v1/vm_snapshot/<vm_id>/snapshots/<snap_id>/reversion
         */
        String vmUuid = this.getVmUuidByName(vmName);
        if (vmUuid!=null) {
            String vmSnapUuid = this.getVMSnapUuidByName(snapName);
            if (vmSnapUuid!=null) {
                return this.revertBlockSnapshots(vmUuid, vmSnapUuid);
            } else {
                return simpleResponse("snapshot is not exists");
            }
        } else {
            return simpleResponse("vm is not exists");
        }
    }
    
    public ResultSet deleteVmSnapIfExist(String name, String vm_name,
            String product_spec) {
        /*
         * @params: name, vm_name
         * @method: 
         */
        String vmUuid = this.getVmUuidByName(vm_name);
        if (vmUuid!=null) {
            ResultSet vmSnap = this.getVMSnapByName(name);
            if (vmSnap.content()!=null) {
                return this.deleteBlockSnapshots(vmUuid, 
                        getStringRecordsByKey(vmSnap, "LCUUID"));
            } else {
                return simpleResponse("snapshot is not exists");
            }
        } else {
            return simpleResponse("vm is not exists");
        }
    }
    
    public ResultSet attachMultiIPAddress(String name, String gateway, 
            List<Map<String, Object>> interfaces) {
        int vmid = this.getVmIdByName(name);
        return this.attachMultiInterface(vmid, gateway, interfaces);
    }
    
    public ResultSet detachMultiIPAddress(int vmid, String gateway, 
            List<Map<String, Object>> interfaces) {
        //int state = 2;
        return this.attachMultiInterface(vmid, gateway, interfaces);
    }
    
    public ResultSet modifyVmFinely(String name, String gateway, 
            List<Map<String, Object>> interfaces) {
        /*
         * @params: name, gateway, interface
         */
        int vmId = this.getVmIdByName(name);
        return this.attachMultiInterface(vmId, gateway, interfaces);
    }
    
    public ResultSet startVmIfHalt(String name) {
        /*
         * @params: params: name
         */
        ResultSet resultSet = this.getVmByName(name);
        if (resultSet.content()!=null) {
            int vmId = getIntRecordsByKey(resultSet, "ID");
            VMState state = this.getVMStatusById(vmId);
            if (state.equals(VMState.getVMStateByDisplayName("Halt"))) {
                this.startVM(vmId);
                return queryAsyncJobResult(vmId, VMState.getVMStateByDisplayName("Running"));
            } else {
                return simpleResponse("could not start vm");
            }
        } else {
            return simpleResponse("vm is not exists");
        }
    }
    
    public ResultSet stopVmIfRunning(String name) {
        /*
         * @params: params: name
         */
        ResultSet resultSet = this.getVmByName(name);
        if (resultSet.content()!=null) {
            int vmId = getIntRecordsByKey(resultSet, "ID");
            VMState state = this.getVMStatusById(vmId);
            if (state.equals(VMState.getVMStateByDisplayName("Running"))) {
                this.stopVM(vmId);
                return queryAsyncJobResult(vmId, VMState.getVMStateByDisplayName("Halt"));
            } else {
                return simpleResponse("could not shutdown vm");
            }
        } else {
            return simpleResponse("vm is not exists");
        }
    }
    
    private ResultSet queryAsyncJobResult(int vmId, VMState vmState) {
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
        
    public ResultSet createVMIfNotExist(String name, String launch_server, String product_spec, 
            String os, String vcpu_num, String mem_size, String sys_disk_size, 
            String user_disk_size, String pool_lcuuid) {
        /*
         * @params: name, launch_server, product_spec, os, vcpu_num, mem_size
         *        sys_disk_size, user_disk_size
         * 
         */
        ResultSet resultSet = this.getVmByName(name);
        if (resultSet.content()==null) {
            return this.createVM(name, launch_server, product_spec, os, vcpu_num, 
                    mem_size, sys_disk_size, user_disk_size, this.pool_lcuuid);
        } else {
            return resultSet;
        }
    }
    
    public ResultSet deleteVMIfExist(String name) {
        /*
         * @params: name
         * @method: 
         */
        ResultSet resultSet = this.getVmByName(name);
        if (resultSet.content()!=null) {
            int vmId = getIntRecordsByKey(resultSet, "ID");
            VMState state = this.getVMStatusById(vmId);
            if (!state.equals(VMState.getVMStateByDisplayName("Halt"))) {
                this.stopVmIfRunning(name);
            }
            return this.deleteVM(vmId);
        } else {
            return simpleResponse("vm is not exists");
        }
    }
    
}
