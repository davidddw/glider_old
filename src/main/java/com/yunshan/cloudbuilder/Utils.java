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

import com.yunshan.config.Configuration;

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
    
    public static void readYamlFromFile(String filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream stream = new FileInputStream(new File(filename));
        Configuration config = yaml.loadAs(stream, Configuration.class);
        System.out.println(config.getDomain());
    }
    
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        readYamlFromFile("d:\\autotest.yml");
    }
}
