package com.yunshan.cloudbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.yaml.snakeyaml.Yaml;

import com.yunshan.config.Configuration;

public class Utils {
    
    public static Level level=Level.INFO;
    
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
    
    public static void readYamlFromFile(String filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream stream = new FileInputStream(new File(filename));
        Configuration config = yaml.loadAs(stream, Configuration.class);
        System.out.println(config.getDomain());
    }
    
    public static void main(String[] args) {
        String freemarkerTemplate = "{"
                + "\"userid\": $!userid,"
                + "\"name\": \"$!name\","
                + "\"domain\": \"$!domain\""
                + "}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "david");
        params.put("userid", null);
        params.put("domain", null);
        String ret = Utils.velocityProcess(params, freemarkerTemplate);
        System.out.println(ret);
    }
}
