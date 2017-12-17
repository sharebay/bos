package com.fly.bos.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FreeMarkerUtils {
    /**
     * 通过指定的名字获取相应的模板
     * @param fileName
     * @return
     */
    public static Template getTemplate(String fileName) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setOutputEncoding("UTF-8");
            cfg.setDefaultEncoding("UTF-8");// 编码设置1
            cfg.setEncoding(Locale.CHINA, "UTF-8");
            cfg.setDirectoryForTemplateLoading(new File("E:\\Workspace\\Java_space\\bos\\bos_fore\\src\\main\\webapp\\WEB-INF\\freemarker"));
            // 获取模板对象
            Template template = cfg.getTemplate(fileName);
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过指定的文件目录和文件名生成相应的文件
     */
    public static Boolean printToFile(Template template,File file,Map<String,Object> map) {
        boolean done = false;
        Writer writer = null;
        try {
            //设置生成的文件编码为UTF-8
            //服务器不支持UTF-8格式HTML时候使用ANSI格式HTML文件，即系统默认编码
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));  // 编码设置3
            //输出模板和数据模型都对应的文件
            template.process(map, writer);
            done = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return done;
    }



}
