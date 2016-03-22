package com.yunshan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHManager {
	public static final Logger s_logger = Util.getLogger();
	private JSch jschSSHChannel;
	private String strUserName;
	private String strConnectionIP;
	private int intConnectionPort;
	private String strPassword;
	private Session sesConnection;
	private int intTimeOut;

	private void doCommonConstructorActions(String userName, String password, String connectionIP) {
		jschSSHChannel = new JSch();
		strUserName = userName;
		strPassword = password;
		strConnectionIP = connectionIP;
	}
	
	public SSHManager(String userName, String password, String connectionIP) {
		doCommonConstructorActions(userName, password, connectionIP);
		intConnectionPort = 22;
		intTimeOut = 60000;
	}

	public SSHManager(String userName, String password, String connectionIP, int connectionPort) {
		doCommonConstructorActions(userName, password, connectionIP);
		intConnectionPort = connectionPort;
		intTimeOut = 60000;
	}

	public SSHManager(String userName, String password, String connectionIP, 
			int connectionPort, int timeOutMilliseconds) {
		doCommonConstructorActions(userName, password, connectionIP);
		intConnectionPort = connectionPort;
		intTimeOut = timeOutMilliseconds;
	}

	public String connect() {
		String errorMessage = null;
		try {
			sesConnection = jschSSHChannel.getSession(strUserName, strConnectionIP, intConnectionPort);
			sesConnection.setPassword(strPassword);
			sesConnection.setConfig("StrictHostKeyChecking", "no");
			sesConnection.connect(intTimeOut);
		} catch (JSchException jschX) {
			errorMessage = jschX.getMessage();
		}
		return errorMessage;
	}

	private String logWarning(String warnMessage) {
		if (warnMessage != null) {
		    s_logger.log(Level.WARN, strConnectionIP + ":" + intConnectionPort + " " + warnMessage);
		}

		return warnMessage;
	}

	public Map<String, Object> sendCommand(String command) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder outputBuffer = new StringBuilder();
		try {
			Channel channel = sesConnection.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			InputStream commandOutput = channel.getInputStream();
			channel.connect();
			int readByte = commandOutput.read();
			while (readByte != 0xffffffff) {
				outputBuffer.append((char) readByte);
				readByte = commandOutput.read();
			}
			map.put("status", channel.getExitStatus());
			channel.disconnect();
		} catch (IOException ioX) {
			logWarning(ioX.getMessage());
			return null;
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
			return null;
		}
		map.put("result", outputBuffer.toString());
		return map;
	}

	public void close() {
		sesConnection.disconnect();
	}

	public static void main(String args[]) {
		String command = "ping -c 1 -W 1 10.33.2.254";
		String userName = "root";
		String password = "security421";
		String connectionIP = "10.33.37.28";
		SSHManager instance = new SSHManager(userName, password, connectionIP);
		String errorMessage = instance.connect();
		if(errorMessage != null)
	     {
	        System.out.println(errorMessage);
	     }
		Map<String, Object> result = instance.sendCommand(command);
		System.out.println(result);
		// close only after all commands are sent
		instance.close();
	}

}
