package com.yunshan.cloudbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Utils {
    public static String freemarkerProcess(Map<String, Object> input, String templateStr) {
        Template t;
        try {
            t = new Template(null, new StringReader(templateStr), null);
            StringWriter writer = new StringWriter();
            t.process(input, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } 
        return null;
    }
    
    protected static <T> T loadFromFile(String fileName, Class<T> clazz) throws IOException 
    { 
        Yaml yaml = new Yaml(new Constructor(clazz)); 
        InputStream input = new FileInputStream(new File(fileName)); 
        T config = (T) yaml.load(input); 
        return config; 
    } 
    
    public static void readYamlFromFile(String filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream stream = new FileInputStream(new File(filename));
        Map<String, Object> map = (Map<String, Object>) yaml.load(stream);
        System.out.println(map.get("host"));
    }
    
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        readYamlFromFile("d:\\config.yml");
    }
}
