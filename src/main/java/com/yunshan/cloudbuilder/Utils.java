package com.yunshan.cloudbuilder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

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
}
