package com.yunshan.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.op.BlockRequest;
import com.yunshan.cloudbuilder.op.EPCRequest;
import com.yunshan.cloudbuilder.op.LBSRequest;
import com.yunshan.cloudbuilder.op.OrderRequest;
import com.yunshan.cloudbuilder.op.RESRequest;
import com.yunshan.cloudbuilder.op.VGWRequest;
import com.yunshan.cloudbuilder.op.VL2Request;
import com.yunshan.cloudbuilder.op.VMRequest;
import com.yunshan.cloudbuilder.op.ValveRequest;
import com.yunshan.config.BWInfo;
import com.yunshan.config.Configuration;
import com.yunshan.config.IPInfo;
import com.yunshan.config.LBInfo;
import com.yunshan.config.VGWInfo;
import com.yunshan.config.VL2Info;
import com.yunshan.config.VMInfo;
import com.yunshan.config.ValveInfo;

public class EnvBuilder {

    protected static final Logger s_logger = Logger.getLogger(EnvBuilder.class);
    private Configuration config = null;
    private OrderRequest orderRequest = null;
    private EPCRequest epcRequest = null;
    private VMRequest vmRequest = null;
    private VGWRequest vgwRequest = null;
    private ValveRequest valveRequest = null;
    private LBSRequest lbRequest = null;
    private VL2Request vl2Request = null;
    private RESRequest resRequest = null;
    private BlockRequest blockRequest = null;

    public EnvBuilder(String filename) {
        this.config = getConfigFromYaml(filename);
        if (config != null) {
            orderRequest = new OrderRequest(config.getHost(), config.getDomain(),
                    config.getUserid());
            vmRequest = new VMRequest(config.getHost(), config.getPool_name(), config.getDomain(),
                    config.getUserid());
            epcRequest = new EPCRequest(config.getHost(), config.getDomain(), config.getUserid());
            lbRequest = new LBSRequest(config.getHost(), config.getPool_name(), config.getDomain(),
                    config.getUserid());
            vgwRequest = new VGWRequest(config.getHost(), config.getDomain(), config.getUserid());
            valveRequest = new ValveRequest(config.getHost(), config.getDomain(),
                    config.getUserid());
            vl2Request = new VL2Request(config.getHost(), config.getDomain(), config.getUserid());
            resRequest = new RESRequest(config.getHost());
            blockRequest = new BlockRequest(config.getHost());
        } else {
            s_logger.error("No config in " + filename);
        }
    }

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T> emptyList() : iterable;
    }

    private Configuration getConfigFromYaml(String filename) {
        try {
            Yaml yaml = new Yaml();
            InputStream stream = new FileInputStream(new File(filename));
            return yaml.loadAs(stream, Configuration.class);
        } catch (FileNotFoundException e) {
            s_logger.error("Cannot find file " + filename, e);
        }
        return null;
    }

    public EnvBuilder orderAll() {
        for (VMInfo vmInfo : emptyIfNull(config.getVms())) {
            orderRequest.orderVM(vmInfo.getName(), vmInfo.getProduct_spec(), vmInfo.getTemplate());
        }
        for (VGWInfo vgwInfo : emptyIfNull(config.getVgateways())) {
            orderRequest.orderVGW(vgwInfo.getName(), vgwInfo.getProduct_spec());
        }
        for (ValveInfo valveInfo : emptyIfNull(config.getValves())) {
            orderRequest.orderValve(valveInfo.getName(), valveInfo.getProduct_spec());
        }
        for (LBInfo lbInfo : emptyIfNull(config.getLbs())) {
            orderRequest.orderLB(lbInfo.getName(), lbInfo.getProduct_spec());
        }
        for (IPInfo ipInfo : emptyIfNull(config.getIps())) {
            orderRequest.orderIP(ipInfo.getIsp(), ipInfo.getNumber(), ipInfo.getProduct_spec());
        }
        for (BWInfo bwInfo : emptyIfNull(config.getBandws())) {
            orderRequest.orderBW(bwInfo.getIsp(), bwInfo.getBandw(), bwInfo.getProduct_spec());
        }
        orderRequest.execute();
        return this;
    }

    public EnvBuilder addResourceToEpc() {
        epcRequest.CreateEPCIfNotExist(config.getEpc_name());
        if (config.getVms() != null) {
            for (VMInfo vmInfo : config.getVms()) {
                vmRequest.setVMToEPC(vmInfo.getName(), config.getEpc_name());
            }
        }
        if (config.getVgateways() != null) {
            for (VGWInfo vgwInfo : config.getVgateways()) {
                vgwRequest.setEPCForVgateway(vgwInfo.getName(), config.getEpc_name());
            }
        }
        if (config.getValves() != null) {
            for (ValveInfo valveInfo : config.getValves()) {
                valveRequest.setEPCForValve(valveInfo.getName(), config.getEpc_name());
            }
        }
        if (config.getLbs() != null) {
            for (LBInfo lbInfo : config.getLbs()) {
                lbRequest.setLBToEPC(lbInfo.getName(), config.getEpc_name());
            }
        }
        return this;
    }

    public EnvBuilder plugBlockToVm() {
        for (VMInfo vmInfo : emptyIfNull(config.getVms())) {
            String vmUuid = vmRequest.getVmUuidByName(vmInfo.getName());
            for (Map<String, Object> block : emptyIfNull(vmInfo.getBlocks())) {
                ResultSet resultSet = blockRequest.createBlockIfNotExist((String) block.get("name"),
                        (Integer) block.get("size"), (String) block.get("product_spec"),
                        (String) block.get("useruuid"));
                String block_id = blockRequest.getStringRecordsByKey(resultSet, "LCUUID");
                blockRequest.plugBlockToVm(block_id, vmUuid);
            }
        }
        return this;
    }

    public EnvBuilder network() {
        for (VL2Info vl2Info : emptyIfNull(config.getVl2s())) {
            vl2Request.CreateVL2IfNotExist(vl2Info.getName(), config.getEpc_name(),
                    vl2Info.getPrefix(), vl2Info.getNetmask());
        }
        for (VGWInfo vgwInfo : emptyIfNull(config.getVgateways())) {
            List<Map<String, Object>> wanInfo = vgwInfo.getWan();
            String lcuuid = resRequest.getUuidByIp((String) wanInfo.get(0).get("ip"));
            wanInfo.get(0).put("ip_resource_lcuuid", lcuuid);
            List<Map<String, Object>> lanInfo = vgwInfo.getLan();
            String vl2_lcuuid = vl2Request
                    .getVL2LcuuidByName((String) lanInfo.get(0).get("vl2_name"));
            lanInfo.get(0).put("vl2_lcuuid", vl2_lcuuid);
            vgwRequest.modifyVgatewayFinely(vgwInfo.getName(), wanInfo, lanInfo);
        }
        for (ValveInfo valveInfo : emptyIfNull(config.getValves())) {
            List<Map<String, Object>> wanInfo = valveInfo.getWan();
            String lcuuid = resRequest.getUuidByIp((String) (wanInfo.get(0).get("ip")));
            wanInfo.get(0).put("ip_resource_lcuuid", lcuuid);
            List<Map<String, Object>> lanInfo = valveInfo.getLan();
            String vl2_lcuuid = vl2Request
                    .getVL2LcuuidByName((String) lanInfo.get(0).get("vl2_name"));
            lanInfo.get(0).put("vl2_lcuuid", vl2_lcuuid);
            valveRequest.modifyValveFinely(valveInfo.getName(), wanInfo, lanInfo);
        }
        for (VMInfo vmInfo : emptyIfNull(config.getVms())) {
            List<Map<String, Object>> vmIPInfo = vmInfo.getVm_ip();
            for (Map<String, Object> map : emptyIfNull(vmIPInfo)) {
                map.put("state", 1);
                if (map.containsKey("wan_ip")) {
                    map.put("ip_resource_lcuuid",
                            resRequest.getUuidByIp((String) map.get("wan_ip")));
                } else {
                    map.put("vl2_lcuuid",
                            vl2Request.getVL2LcuuidByName((String) map.get("vl2_name")));
                }
            }
            vmRequest.attachMultiIPAddress(vmInfo.getName(), vmInfo.getVm_gw(), vmIPInfo);
        }
        for (LBInfo lbInfo : emptyIfNull(config.getLbs())) {
            List<Map<String, Object>> lbIPInfo = lbInfo.getLb_ip();
            for (Map<String, Object> map : emptyIfNull(lbIPInfo)) {
                map.put("state", 1);
                if (map.containsKey("wan_ip")) {
                    map.put("ip_resource_lcuuid",
                            resRequest.getUuidByIp((String) map.get("wan_ip")));
                } else {
                    map.put("vl2_lcuuid",
                            vl2Request.getVL2LcuuidByName((String) map.get("vl2_name")));
                }
            }
            lbRequest.attachMultiIPAddress(lbInfo.getName(), lbInfo.getLb_gw(), lbIPInfo);
        }
        return this;
    }

    public void build() {
        if (config != null) {
            orderAll();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            plugBlockToVm();
            addResourceToEpc().network();
        }
    }

    public void destroy() {
        for (LBInfo lbInfo : emptyIfNull(config.getLbs())) {
            lbRequest.deleteLBIfExist(lbInfo.getName());
        }
        for (VMInfo vmInfo : emptyIfNull(config.getVms())) {
            vmRequest.deleteVMIfExist(vmInfo.getName());
        }
        for (VGWInfo vgwInfo : emptyIfNull(config.getVgateways())) {
            vgwRequest.deleteVgatewayNotExist(vgwInfo.getName());
        }
        for (ValveInfo valveInfo : emptyIfNull(config.getValves())) {
            valveRequest.deleteVgatewayNotExist(valveInfo.getName());
        }
        for (VL2Info vl2Info : emptyIfNull(config.getVl2s())) {
            vl2Request.deleteVL2IfExist(vl2Info.getName());
        }
        epcRequest.DeleteEPCIfExist(config.getEpc_name());
    }
}
