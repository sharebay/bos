package com.fly.bos.action;

import com.alibaba.fastjson.JSON;
import com.fly.bos.domain.PageBean;
import com.fly.bos.domain.Promotion;
import com.fly.bos.util.FreeMarkerUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
@Scope("prototype")
@Namespace("/promotion")
@ParentPackage("struts-default")
public class PromotionAction  extends ActionSupport implements ModelDriven<Promotion> {

    private Promotion promotion = new Promotion();
    @Override
    public Promotion getModel() {
        return promotion;
    }


    /**
     * 展示促销详情
     * @return
     */
    @Action("showDetail")
    public String showDetail() throws IOException, TemplateException {

        //1.查缓存
        String path = ServletActionContext.getServletContext().getRealPath("/")
                + "cache/promotion"
                + getModel().getId()
                + ".html";
        File file = new File(path);
        //如果不存在，生成页面并缓存，并将结果返回
        if (!file.exists()) {
            Promotion promotion = WebClient.create("http://localhost:8080/")
                    .path("bos_management_web/services/promotionService/promotions/" + getModel().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .get(Promotion.class);
            //根据模板生成页面
            // 获取模板对象
            Template template = FreeMarkerUtils.getTemplate("promotion.ftl");
            //使用Map集合创建动态数据对象
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("promotion", promotion);
            //合并输出，在控制台显示
            FreeMarkerUtils.printToFile(template, file, parameterMap);
        }
        //如果存在，将结果返回

        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        FileUtils.copyFile(file, ServletActionContext.getResponse().getOutputStream());

        return NONE;
    }


    /**
     * 分页查询
     * @return
     */
    @Action("queryPage")
    public String queryPage() throws IOException {

        PageBean<Promotion> pageBean = WebClient.create("http://localhost:8080/")
                .path("bos_management_web/services/promotionService/promotions/"
                        + page + "/" + pageSize)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(PageBean.class);

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", pageBean.getTotal());
        map.put("pageData", pageBean.getData());

        String jsonString = JSON.toJSONString(map);
        ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().write(jsonString);

        return NONE;
    }

    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
