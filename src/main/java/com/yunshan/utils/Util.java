package com.yunshan.utils;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Util {

    public static Level level=Level.INFO;
    
    @SuppressWarnings("unchecked") 
    public static void setLevel(Level level) {
        Enumeration<Logger> currentLoggers = LogManager.getCurrentLoggers(); 
        while (currentLoggers.hasMoreElements()) { 
            Logger logger = currentLoggers.nextElement(); 
            if ("ROOT".equals(logger.getName())) { 
                LogManager.getRootLogger().setLevel(level);
            } else {
                LogManager.getLogger(logger.getName()).setLevel(level);
            }
        } 
    }
    
    public static Logger getLogger() {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[2];
        final Logger logger = Logger.getLogger(methodCaller.getClassName());
        logger.setLevel(level);
        return logger;
    }

	public static String velocityProcess(Map<String, Object> input, String templateStr) {
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		VelocityContext context = new VelocityContext(input);
		StringWriter writer = new StringWriter();
		ve.evaluate(context, writer, "", templateStr);
		return writer.toString();
	}

	public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}
	
	public static void sleep(int s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
