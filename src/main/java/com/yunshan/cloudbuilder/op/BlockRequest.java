package com.yunshan.cloudbuilder.op;

import java.util.HashMap;
import java.util.Map;

import com.yunshan.cloudbuilder.HttpMethod;
import com.yunshan.cloudbuilder.RESTClient;
import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.Utils;

public class BlockRequest extends RESTClient {
    
    public BlockRequest(String host) {
        super(host);
    }

    private ResultSet createBlock(String name, int size, String product_spec, String useruuid) {
        /*
         * @params: name, size, product_spec, useruuid
         * @method: POST /v1/blocks
         * 
         */
        String velocityTemplate = "{" 
                + "\"NAME\": \"$!name\"," 
                + "\"SIZE\": \"$!size\","
                + "\"FROM_VOLUME\": false," 
                + "\"USER_LCUUID\": \"$!useruuid\","
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\"," 
                + "\"ORDER_ID\": 100003"
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("size", size);
        params.put("product_spec", product_spec);
        params.put("useruuid", useruuid);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        return this.RequestAPP(HttpMethod.POST, "blocks", ret, null);
    }
    
    private ResultSet getBlocks() {
        /*
         * @method: GET /v1/blocks
         */
        return this.RequestAPP(HttpMethod.GET, "blocks", null, null);
    }
    
    public ResultSet getBlockById(String blockId) {
        /*
         * @method: GET v1/blocks/<block_id>/
         */
        return this.RequestAPP(HttpMethod.GET, "blocks", null, blockId);
    }

    public ResultSet modifyBlocks(String blockUuid, String size) {
        /*
         * @params: params: block_id, size
         * @method: PATCH /v1/blocks/<block_id>/
         */
        String velocityTemplate = "{" 
                + "\"SIZE\": \"$!size\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("size", size);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        return this.RequestAPP(HttpMethod.PATCH, "blocks", ret, blockUuid);
    }
    
    private ResultSet deleteBlock(String blockUuid) {
        /*
         * @params: block_id
         * @method: DELETE /v1/block/<block_id>/
         */
        return this.RequestAPP(HttpMethod.DELETE, "blocks", null, blockUuid);
    }

    private ResultSet createBlockSnapshot(String name, String description, String product_spec, String blockUuid) {
        /*
         * @params: name, description, product_spec, block_id
         * @method: PATCH /v1/blocks/<block_id>/snapshots
         */
        String velocityTemplate = "{" 
                + "\"NAME\": \"$!name\"," 
                + "\"DESCRIPTION\": \"$!description\"," 
                + "\"PRODUCT_SPECIFICATION_LCUUID\": \"$!product_spec\"" 
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("description", description);
        params.put("product_spec", product_spec);
        String ret = Utils.velocityProcess(params, velocityTemplate);
        String param = blockUuid + "/snapshots";
        return this.RequestAPP(HttpMethod.POST, "blocks", ret, param);
    }
    
    private ResultSet deleteBlockSnapshot(String blockUuid, String snapUuid) {
        /*
         * @params: block_id, snap_id
         * @method: DELETE /v1/blocks/<block_id>/snapshots/<snapshot_lcuuid>
         */
        String param = blockUuid + "/snapshots/" + snapUuid;
        return this.RequestAPP(HttpMethod.DELETE, "blocks", null, param);
    }

    public ResultSet revertBlockSnapshot(String blockUuid, String snap_id) {
        /*
         * @params: block_id, snap_id
         * POST /v1/blocks/<block_id>/snapshots/<snap_id>/reversion
         */
        String param = blockUuid + "/snapshots/" + snap_id + "/reversion";
        return this.RequestAPP(HttpMethod.POST, "blocks", null, param);
    }
    
    private ResultSet getBlockSnapshot(String blockUuid) {
        /*
         * @params: block_id
         * @method: GET /v1/blocks/<block_id>/snapshots
         */
        String param = blockUuid + "/snapshots";
        return this.RequestAPP(HttpMethod.GET, "blocks", null, param);
    }

    public ResultSet plugBlockToVm(String blockUuid, String vmUuid) {
        /*
         * @params: block_id, vm_id
         * @method: POST /v1/vm_block/<vm_lcuuid>/blocks/<block_lcuuid>
         */
        String param = vmUuid + "/blocks/" + blockUuid;
        return this.RequestAPP(HttpMethod.POST, "vm_block", null, param);
    }
    
    public ResultSet unplugBlockToVm(String blockUuid, String vmUuid) {
        /*
         * @params: block_id, vm_id
         * @method: DELETE /v1/vm_block/<vm_lcuuid>/blocks/<block_lcuuid>
         */
        String param = vmUuid + "/blocks/" + blockUuid;
        return this.RequestAPP(HttpMethod.DELETE, "vm_block", null, param);
    }
    
    public ResultSet getBlockByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getBlocks();
        return filterRecordsByKey(resultSet, "ALIAS", name);
    }
    
    public ResultSet getBlockSnapByName(String name, String blockUuid) {
        /*
         * @params: name, block_id
         * 
         */
        ResultSet resultSet = this.getBlockSnapshot(blockUuid);
        return filterRecordsByKey(resultSet, "NAME", name);
    }
    
    public String getBlockUuidByName(String name) {
        /*
         * @params: name
         * 
         */
        ResultSet resultSet = this.getBlockByName(name);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }
    
    public String getBlockSnapUuidByName(String name, String blockUuid) {
        /*
         * @params: name, block_id
         * 
         */
        ResultSet resultSet = this.getBlockSnapByName(name, blockUuid);
        if (resultSet.content()!=null) {
            return getStringRecordsByKey(resultSet, "LCUUID");
        } else {
            return null;
        }
    }

    public ResultSet createBlockIfNotExist(String name, int size, String product_spec, String useruuid) {
        /*
         * @params: name, size, product_spec, useruuid
         */
        ResultSet resultSet = this.getBlockByName(name);
        if (resultSet.content()==null) {
            return this.createBlock(name, size, product_spec, useruuid);
        } else {
            return resultSet;
        }
    }
    
    public ResultSet deleteBlockIfExist(String name) {
        /*
         * @params: vmid, name, product_spec
         */
        ResultSet resultSet = this.getBlockByName(name);
        if (resultSet.content()!=null) {
            return this.deleteBlock(getStringRecordsByKey(resultSet, "LCUUID"));
        } else {
            return simpleResponse("block is not exists");
        }
    }

    public ResultSet createBlockSnapIfNotExist(String block_name, String snap_name, String product_spec, String useruuid) {
        /*
         * @params: block_name, snap_name, description, product_spec
         */
        String blockId = this.getBlockUuidByName(block_name);
        if (blockId!=null) {
            ResultSet blockSnap = this.getBlockSnapByName(snap_name, blockId);
            if (blockSnap.content()==null){
                return this.createBlockSnapshot(snap_name, snap_name, product_spec, blockId);
            } else {
                return simpleResponse("snapshot is exists");
            }
        } else {
            return simpleResponse("block is not exists");
        }
    }
    
    public ResultSet deleteBlockSnapIfExist(String block_name, String snap_name) {
        /*
         * @params: block_name, snap_name
         */
        String blockId = this.getBlockUuidByName(block_name);
        if (blockId!=null) {
            ResultSet blockSnap = this.getBlockSnapByName(snap_name, blockId);
            if (blockSnap.content()!=null){
                return this.deleteBlockSnapshot(blockId, getStringRecordsByKey(blockSnap, "LCUUID"));
            } else {
                return simpleResponse("snapshot is not exists");
            }
        } else {
            return simpleResponse("block is not exists");
        }
    }

}
