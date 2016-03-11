package com.yunshan.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.yunshan.cloudbuilder.op.EPCRequest;
import com.yunshan.cloudbuilder.op.LBSRequest;
import com.yunshan.cloudbuilder.op.OrderRequest;
import com.yunshan.cloudbuilder.op.VGWRequest;
import com.yunshan.cloudbuilder.op.VMRequest;
import com.yunshan.cloudbuilder.op.ValveRequest;
import com.yunshan.config.BWInfo;
import com.yunshan.config.Configuration;
import com.yunshan.config.IPInfo;
import com.yunshan.config.LBInfo;
import com.yunshan.config.VGWInfo;
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

    public EnvBuilder(String filename) {
        this.config = getConfigFromYaml(filename);
        if (config!=null) {
            orderRequest = new OrderRequest(config.getHost(), config.getDomain(), config.getUserid());
            vmRequest = new VMRequest(config.getHost(), config.getPool_name(), config.getDomain(), config.getUserid());
            epcRequest = new EPCRequest(config.getHost(), config.getDomain(), config.getUserid());
            epcRequest.CreateEPCIfNotExist(config.getEpc_name());
            lbRequest = new LBSRequest(config.getHost(), config.getPool_name(), config.getDomain(), config.getUserid());
            vgwRequest = new VGWRequest(config.getHost(), config.getDomain(), config.getUserid());
            valveRequest = new ValveRequest(config.getHost(), config.getDomain(), config.getUserid());
            
        } else {
            s_logger.error("No config in " + filename);
        }
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
    
    private EnvBuilder orderAll() {
        for (VMInfo vmInfo : config.getVms()) {
            orderRequest.orderVM(vmInfo.getName(), vmInfo.getProduct_spec(), vmInfo.getTemplate());
        }
        for (VGWInfo vgwInfo : config.getVgateways()) {
            orderRequest.orderVGW(vgwInfo.getName(), vgwInfo.getProduct_spec());
        }
        for (ValveInfo valveInfo : config.getValves()) {
            orderRequest.orderValve(valveInfo.getName(), valveInfo.getProduct_spec());
        }
        for (LBInfo lbInfo : config.getLbs()) {
            orderRequest.orderLB(lbInfo.getName(), lbInfo.getProduct_spec());
        }
        for (IPInfo ipInfo : config.getIps()) {
            orderRequest.orderIP(ipInfo.getIsp(), ipInfo.getNumber(), ipInfo.getProduct_spec());
        }
        for (BWInfo bwInfo : config.getBandws()) {
            orderRequest.orderBW(bwInfo.getIsp(), bwInfo.getBandw(), bwInfo.getProduct_spec());
        }
        orderRequest.execute();
        return this;
    }
    
    private EnvBuilder addResourceToEpc() {
        for (VMInfo vmInfo : config.getVms()) {
            vmRequest.setVMToEPC(vmInfo.getName(), config.getEpc_name());
        }
        for (VGWInfo vgwInfo : config.getVgateways()) {
            vgwRequest.setEPCForVgateway(vgwInfo.getName(), config.getEpc_name());
        }
        for (ValveInfo valveInfo : config.getValves()) {
            valveRequest.setEPCForValve(valveInfo.getName(), config.getEpc_name());
        }
        for (LBInfo lbInfo : config.getLbs()) {
            lbRequest.setLBToEPC(lbInfo.getName(), config.getEpc_name());
        }
        return this;
    }
    
    public void build() {
        if (config!=null) {
            orderAll();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addResourceToEpc();
        }
    }
}
