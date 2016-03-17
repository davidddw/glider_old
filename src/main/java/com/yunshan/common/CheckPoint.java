package com.yunshan.common;

import java.util.Map;

import com.yunshan.cloudbuilder.SSHManager;

public class CheckPoint {
	
	private String user;
	private String pass;
	private String host;
	private int port;
	
	public CheckPoint(String user, String pass, String host, int port) {
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.port = port;
		
	}
	
	public Map<String, Object> executeCmdFromSSH(String cmd) {
		SSHManager instance = new SSHManager(user, pass, host, port);
		String errorMessage = instance.connect();
		if(errorMessage != null) {
	        System.out.println(errorMessage);
	    }
		Map<String, Object> result = instance.sendCommand(cmd);
		instance.close();
		return result;
	}
	
	public boolean checkPingAlive(String ip) {
		String command = "ping -c 1 -W 1 " + ip;
		Map<String, Object> ret = this.executeCmdFromSSH(command);
		return (ret!=null) ? ret.get("status").equals(0) : false;
	}
	
	public Map<String, Object> executeRemoteCmd(String ip, String cmd) {
		String fullcmd = "sshpass -p yunshan3302 ssh "
				+ "-o 'StrictHostKeyChecking no' " 
				+ ip + " " + cmd;
		return this.executeCmdFromSSH(fullcmd);
	}
		
	public static void main(String args[]) {
		String userName = "root";
		String password = "yunshan3302";
		String connectionIP = "192.168.182.191";
		CheckPoint ck = new CheckPoint(userName, password, connectionIP, 60022);
		System.out.println(ck.checkPingAlive("10.25.1.11"));
		System.out.println(ck.checkPingAlive("10.25.1.10"));
		System.out.println(ck.checkPingAlive("10.25.1.1"));
		System.out.println(ck.checkPingAlive("10.33.2.1"));
		System.out.println(ck.checkPingAlive("10.25.2.10"));
	}
}
