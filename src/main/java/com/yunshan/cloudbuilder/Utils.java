package com.yunshan.cloudbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.yaml.snakeyaml.Yaml;

import com.yunshan.config.Configuration;

public class Utils {

	public static void setLevel(Logger log, Level level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(log.getName());
		loggerConfig.setLevel(level);
		ctx.updateLoggers(config); 
	}

	public static Logger getLogger() {
		final Throwable t = new Throwable();
		final StackTraceElement methodCaller = t.getStackTrace()[2];
		final Logger logger = LogManager.getLogger(methodCaller.getClassName());
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

	public static void readYamlFromFile(String filename) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		InputStream stream = new FileInputStream(new File(filename));
		Configuration config = yaml.loadAs(stream, Configuration.class);
		System.out.println(config.getDomain());
	}

	public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}
}
