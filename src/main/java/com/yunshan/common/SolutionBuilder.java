package com.yunshan.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunshan.cloudbuilder.Utils;
import com.yunshan.cloudbuilder.op.RESRequest;
import com.yunshan.cloudbuilder.op.SolutionRequest;
import com.yunshan.config.solution.SolutionConfig;
import com.yunshan.config.solution.Interfaces;
import com.yunshan.config.solution.VGWInfo;
import com.yunshan.config.solution.VL2Info;
import com.yunshan.config.solution.VMInfo;

public class SolutionBuilder extends LivecloudBase {

	private SolutionConfig config = null;
	private SolutionRequest solutionRequest = null;
	private RESRequest resRequest = null;

	public SolutionBuilder(String filename) throws Exception {
		super(filename);
		this.config = getConfigFromYaml(filename, SolutionConfig.class);
		if (config != null) {
			uuidMapper = initMapper();
			solutionRequest = new SolutionRequest(config.getHost(), getMapValueSafely(uuidMapper, config.getDomain()),
					config.getUserid(), config.getEpc_name());
			resRequest = new RESRequest(config.getHost());
		} else {
			s_logger.error("No config in " + filename);
			System.exit(1);
		}
	}
	
	public SolutionBuilder createSimple() {
		List<Map<String, Object>> vgw_info = new ArrayList<Map<String, Object>>();
		for (VGWInfo vgwInfo : Utils.emptyIfNull(config.getVgw())) {
			Map<String, Object> newVGWMap = new HashMap<String, Object>();
			Interfaces interfaces = vgwInfo.getInterfaces();
			for (Map<String, Object> vgw : Utils.emptyIfNull(interfaces.getWan())) {
				String lcuuid = resRequest.getUuidByIp((String)vgw.get("product_spec"));
				vgw.put("product_spec", lcuuid);
				vgw.put("qos_product_spec", lcuuid);
			}
			String vgwInt = solutionRequest.generateVGWInterfaceData(interfaces.getWan(), 
					interfaces.getLan());
			newVGWMap.put("name", vgwInfo.getName());
			newVGWMap.put("product_spec", getMapValueSafely(uuidMapper, vgwInfo.getProduct_spec()));
			newVGWMap.put("interfaces", vgwInt);
			vgw_info.add(newVGWMap);
		}
		List<Map<String, Object>> vms_info = new ArrayList<Map<String, Object>>();
		for (VMInfo vmInfo : Utils.emptyIfNull(config.getVms())) {
			Map<String, Object> newVMMap = new HashMap<String, Object>();
			Interfaces interfaces = vmInfo.getInterfaces();
			for (Map<String, Object> vm : Utils.emptyIfNull(interfaces.getWan())) {
				String lcuuid = resRequest.getUuidByIp((String)vm.get("product_spec"));
				vm.put("product_spec", lcuuid);
				vm.put("qos_product_spec", lcuuid);
			}
			String vgwInt = solutionRequest.generateVMInterfaceData(vmInfo.getInterfaces().getWan(), 
					vmInfo.getInterfaces().getLan());
			newVMMap.put("name", vmInfo.getName());
			newVMMap.put("os_template", vmInfo.getTemplate());
			newVMMap.put("product_spec", getMapValueSafely(uuidMapper, vmInfo.getProduct_spec()));
			newVMMap.put("interfaces", vgwInt);
			vms_info.add(newVMMap);
		}
		List<Map<String, Object>> vl2_info = new ArrayList<Map<String, Object>>();
		for (VL2Info vl2Info : Utils.emptyIfNull(config.getVl2s())) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("name", vl2Info.getName());
			vl2_info.add(newMap);
		}
		solutionRequest.execute(vgw_info, vms_info, vl2_info);
		return this;
	}

	public void build() {
		if (config != null) {
			createSimple();
		}
	}
}
