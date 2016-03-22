package com.yunshan.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yunshan.config.normal.BWInfo;
import com.yunshan.config.normal.IPInfo;
import com.yunshan.config.normal.LBInfo;
import com.yunshan.config.normal.LBListenerInfo;
import com.yunshan.config.normal.NormalConfig;
import com.yunshan.config.normal.VGWInfo;
import com.yunshan.config.normal.VL2Info;
import com.yunshan.config.normal.VMInfo;
import com.yunshan.config.normal.ValveInfo;
import com.yunshan.utils.Util;

public class EnvBuilder extends LivecloudBase {
	
	private NormalConfig config = null;
	private OrderRequest orderRequest = null;
	private EPCRequest epcRequest = null;
	private VMRequest vmRequest = null;
	private VGWRequest vgwRequest = null;
	private ValveRequest valveRequest = null;
	private LBSRequest lbRequest = null;
	private VL2Request vl2Request = null;
	private RESRequest resRequest = null;
	private BlockRequest blockRequest = null;

	public EnvBuilder(String filename) throws Exception {
		super(filename);
		this.config = getConfigFromYaml(filename, NormalConfig.class);
		if (config != null) {
			uuidMapper = initMapper();
			orderRequest = new OrderRequest(config.getHost(), getMapValueSafely(uuidMapper, config.getDomain()),
					getUserIdFromDB(config.getUser()));
			vmRequest = new VMRequest(config.getHost(), config.getPool_name(),
					getMapValueSafely(uuidMapper, config.getDomain()), getUserIdFromDB(config.getUser()));
			epcRequest = new EPCRequest(config.getHost(), getMapValueSafely(uuidMapper, config.getDomain()),
					getUserIdFromDB(config.getUser()));
			lbRequest = new LBSRequest(config.getHost(), config.getPool_name(),
					getMapValueSafely(uuidMapper, config.getDomain()), getUserIdFromDB(config.getUser()));
			vgwRequest = new VGWRequest(config.getHost(), getMapValueSafely(uuidMapper, config.getDomain()),
					getUserIdFromDB(config.getUser()));
			valveRequest = new ValveRequest(config.getHost(), getMapValueSafely(uuidMapper, config.getDomain()),
					getUserIdFromDB(config.getUser()));
			vl2Request = new VL2Request(config.getHost(), getMapValueSafely(uuidMapper, config.getDomain()),
					getUserIdFromDB(config.getUser()));
			resRequest = new RESRequest(config.getHost());
			blockRequest = new BlockRequest(config.getHost());
		} else {
			s_logger.error("No config in " + filename);
			System.exit(1);
		}
	}

	public EnvBuilder orderAll() {
		for (VMInfo vmInfo : Util.emptyIfNull(config.getVms())) {
			orderRequest.orderVM(vmInfo.getName(), getMapValueSafely(uuidMapper, vmInfo.getProduct_spec()),
					vmInfo.getTemplate());
		}
		for (VGWInfo vgwInfo : Util.emptyIfNull(config.getVgateways())) {
			orderRequest.orderVGW(vgwInfo.getName(), getMapValueSafely(uuidMapper, vgwInfo.getProduct_spec()));
		}
		for (ValveInfo valveInfo : Util.emptyIfNull(config.getValves())) {
			orderRequest.orderValve(valveInfo.getName(), getMapValueSafely(uuidMapper, valveInfo.getProduct_spec()));
		}
		for (LBInfo lbInfo : Util.emptyIfNull(config.getLbs())) {
			orderRequest.orderLB(lbInfo.getName(), getMapValueSafely(uuidMapper, lbInfo.getProduct_spec()));
		}
		for (IPInfo ipInfo : Util.emptyIfNull(config.getIps())) {
			orderRequest.orderIP(ipInfo.getIsp(), ipInfo.getNumber(),
					getMapValueSafely(uuidMapper, ipInfo.getProduct_spec()));
		}
		for (BWInfo bwInfo : Util.emptyIfNull(config.getBandws())) {
			orderRequest.orderBW(bwInfo.getIsp(), 1024*1024*bwInfo.getBandw(),
					getMapValueSafely(uuidMapper, bwInfo.getProduct_spec()));
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
		for (VMInfo vmInfo : Util.emptyIfNull(config.getVms())) {
			String vmUuid = vmRequest.getVmUuidByName(vmInfo.getName());
			for (Map<String, Object> block : Util.emptyIfNull(vmInfo.getBlocks())) {
				ResultSet resultSet = blockRequest.createBlockIfNotExist((String) block.get("name"),
						(Integer) block.get("size"), (String) block.get("product_spec"),
						getMapValueSafely(uuidMapper, (String) block.get("useruuid")));
				String block_id = blockRequest.getStringRecordsByKey(resultSet, "LCUUID");
				blockRequest.plugBlockToVm(block_id, vmUuid);
			}
		}
		return this;
	}

	public EnvBuilder unplugAndDeleteBlock() {
		for (VMInfo vmInfo : Util.emptyIfNull(config.getVms())) {
			String vmUuid = vmRequest.getVmUuidByName(vmInfo.getName());
			for (Map<String, Object> block : Util.emptyIfNull(vmInfo.getBlocks())) {
				String blockUuid = blockRequest.getBlockUuidByName((String) block.get("name"));
				blockRequest.unplugBlockToVm(blockUuid, vmUuid);
				blockRequest.deleteBlockIfExist((String) block.get("name"));
			}
		}
		return this;
	}

	public EnvBuilder network() {
		for (VL2Info vl2Info : Util.emptyIfNull(config.getVl2s())) {
			vl2Request.CreateVL2IfNotExist(vl2Info.getName(), config.getEpc_name(), vl2Info.getPrefix(),
					vl2Info.getNetmask());
		}
		for (VGWInfo vgwInfo : Util.emptyIfNull(config.getVgateways())) {
			List<Map<String, Object>> wanInfo = vgwInfo.getWan();
			String lcuuid = resRequest.getUuidByIp((String) wanInfo.get(0).get("ip"));
			wanInfo.get(0).put("ip_resource_lcuuid", lcuuid);
			List<Map<String, Object>> lanInfo = vgwInfo.getLan();
			String vl2_lcuuid = vl2Request.getVL2LcuuidByName((String) lanInfo.get(0).get("vl2_name"));
			lanInfo.get(0).put("vl2_lcuuid", vl2_lcuuid);
			vgwRequest.modifyVgatewayFinely(vgwInfo.getName(), wanInfo, lanInfo);
		}
		for (ValveInfo valveInfo : Util.emptyIfNull(config.getValves())) {
			List<Map<String, Object>> wanInfo = valveInfo.getWan();
			String lcuuid = resRequest.getUuidByIp((String) (wanInfo.get(0).get("ip")));
			wanInfo.get(0).put("ip_resource_lcuuid", lcuuid);
			List<Map<String, Object>> lanInfo = valveInfo.getLan();
			String vl2_lcuuid = vl2Request.getVL2LcuuidByName((String) lanInfo.get(0).get("vl2_name"));
			lanInfo.get(0).put("vl2_lcuuid", vl2_lcuuid);
			valveRequest.modifyValveFinely(valveInfo.getName(), wanInfo, lanInfo);
		}
		for (VMInfo vmInfo : Util.emptyIfNull(config.getVms())) {
			List<Map<String, Object>> vmIPInfo = vmInfo.getVm_ip();
			for (Map<String, Object> map : Util.emptyIfNull(vmIPInfo)) {
				map.put("state", 1);
				if (map.containsKey("wan_ip")) {
					map.put("ip_resource_lcuuid", resRequest.getUuidByIp((String) map.get("wan_ip")));
				} else {
					map.put("vl2_lcuuid", vl2Request.getVL2LcuuidByName((String) map.get("vl2_name")));
				}
			}
			vmRequest.attachMultiIPAddress(vmInfo.getName(), vmInfo.getVm_gw(), vmIPInfo);
		}
		for (LBInfo lbInfo : Util.emptyIfNull(config.getLbs())) {
			List<Map<String, Object>> lbIPInfo = lbInfo.getLb_ip();
			for (Map<String, Object> map : Util.emptyIfNull(lbIPInfo)) {
				map.put("state", 1);
				if (map.containsKey("wan_ip")) {
					map.put("ip_resource_lcuuid", resRequest.getUuidByIp((String) map.get("wan_ip")));
				} else {
					map.put("vl2_lcuuid", vl2Request.getVL2LcuuidByName((String) map.get("vl2_name")));
				}
			}
			lbRequest.attachMultiIPAddress(lbInfo.getName(), lbInfo.getLb_gw(), lbIPInfo);
		}
		return this;
	}

	public EnvBuilder configLBRule() {
		for (LBInfo lbInfo : Util.emptyIfNull(config.getLbs())) {
			ResultSet loadbalance = lbRequest.getLBByName(lbInfo.getName());
			if (loadbalance.content()==null) {
				return null;
			}
			String lbUuid = lbRequest.getStringRecordsByKey(loadbalance, "LCUUID");
			List<LBListenerInfo> lbListeners = lbInfo.getLb_listener();
			for (LBListenerInfo lbListenerInfo : Util.emptyIfNull(lbListeners)) {
				ResultSet lbListener = lbRequest.createLBListenerIfNotExist(lbListenerInfo.getName(), lbUuid,
						lbListenerInfo.getProtocol(), (String) lbInfo.getLb_ip().get(0).get("wan_ip"),
						lbListenerInfo.getPort(), lbListenerInfo.getBalance());
				List<Map<String, Object>> vmList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> map : Util.emptyIfNull(lbListenerInfo.getVms())) {
					ResultSet vmInst = vmRequest.getVmByName((String) map.get("name"));
					String vmUuid = vmRequest.getStringRecordsByKey(vmInst, "LCUUID");
					Map<String, Object> vmDict = new HashMap<String, Object>();
					vmDict.put("NAME", map.get("name"));
					if (vmInst.content() != null) {
						vmDict.put("IP",
								vmInst.content().getAsJsonObject().get("INTERFACES").getAsJsonArray().get(0)
										.getAsJsonObject().get("LAN").getAsJsonObject().get("IPS").getAsJsonArray()
										.get(0).getAsJsonObject().get("ADDRESS").getAsString());
					} else {
						vmDict.put("IP", "0.0.0.0");
					}
					vmDict.put("PORT", map.get("port"));
					vmDict.put("WEIGHT", map.get("weight"));
					vmDict.put("STATE", map.get("state"));
					vmDict.put("VM_LCUUID", vmUuid);
					vmList.add(vmDict);
				}
				String ipAddress = (String) lbInfo.getLb_ip().get(0).get("wan_ip");
				String lbListenerLcuuid = lbRequest.getStringRecordsByKey(lbListener, "LB_LISTERNER_LCUUID");
				lbRequest.putLBListener(lbListenerInfo.getName(), lbListenerInfo.getProtocol(), ipAddress,
						lbListenerInfo.getPort(), lbListenerInfo.getBalance(), lbUuid, lbListenerLcuuid, vmList);
			}
		}
		return this;
	}

	public EnvBuilder configVGWRule() {
		for (VGWInfo vgwInfo : Util.emptyIfNull(config.getVgateways())) {
			int index = 0;
			for (Map<String, Object> map : vgwInfo.getRules().getSnat_rules()) {
				index += 1;
				vgwRequest.setSnatForVgateway(vgwInfo.getName(), "snat" + index, 1, index, (String) map.get("sip1"),
						(String) map.get("sip2"), (String) map.get("dip"), true);
			}
			index = 0;
			for (Map<String, Object> map : vgwInfo.getRules().getDnat_rules()) {
				index += 1;
				vgwRequest.setDnatForVgateway(vgwInfo.getName(), "dnat" + index, 1, index, (String) map.get("sip"),
						(Integer) map.get("sport"), (String) map.get("dip"), (Integer) map.get("dport"), true);
			}
		}
		return this;
	}

	public void build() {
		if (config != null) {
			orderAll();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			plugBlockToVm();
			addResourceToEpc().network();
			configLBRule();
			configVGWRule();
		}
	}

	public void destroy() {
		unplugAndDeleteBlock();
		for (LBInfo lbInfo : Util.emptyIfNull(config.getLbs())) {
			lbRequest.deleteLBIfExist(lbInfo.getName());
		}
		for (VMInfo vmInfo : Util.emptyIfNull(config.getVms())) {
			vmRequest.deleteVMIfExist(vmInfo.getName());
		}
		for (VGWInfo vgwInfo : Util.emptyIfNull(config.getVgateways())) {
			vgwRequest.deleteVgatewayNotExist(vgwInfo.getName());
		}
		for (ValveInfo valveInfo : Util.emptyIfNull(config.getValves())) {
			valveRequest.deleteVgatewayNotExist(valveInfo.getName());
		}
		Util.sleep(2000);
		for (VL2Info vl2Info : Util.emptyIfNull(config.getVl2s())) {
			vl2Request.deleteVL2IfExist(vl2Info.getName());
		}
		epcRequest.DeleteEPCIfExist(config.getEpc_name());
	}
}
