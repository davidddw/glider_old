package com.yunshan.cloudbuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ResultSet {
	
	private String status;
	private JsonElement content;
	
	public ResultSet(String status) {
	    this.status = status;
	}
	
	public ResultSet(JsonElement content) {
	    this.status = "200";
	    this.content = content;
    }
	
	public ResultSet(String status, JsonElement content) {
		this.status = status;
		this.content = content;
	}
	
	public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.add("content", content);
        return jsonObject.toString();
	}
	
	public void setResultSet(JsonElement content) {
        this.content = content;
	}
	
	public String status() {
		return this.status;
	}
	
	public JsonElement content() {
		return this.content;
	}
	
}
