package com.fly.freemarker;

import com.fly.bos.domain.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerTest {

    @Test
    public void testOutput() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/freemarker"));

        Promotion promotion = WebClient.create("http://localhost:8080/")
                .path("bos_management_web/services/promotionService/promotions/" + 59)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(Promotion.class);
        //使用Map集合创建动态数据对象
        Template template = configuration.getTemplate("promotion.ftl");
        //使用Map集合创建动态数据对象
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("promotion", promotion);
        //合并输出，在控制台显示
        template.process(parameterMap, new PrintWriter(System.out));
        template.process(parameterMap, new PrintWriter(new File("E:\\Workspace\\Java_space\\bos\\bos_fore\\src\\main\\webapp\\cache\\test.html")));
    }
}



