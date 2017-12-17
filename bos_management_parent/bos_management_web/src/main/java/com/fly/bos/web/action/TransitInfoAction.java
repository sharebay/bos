package com.fly.bos.web.action;

import com.fly.bos.domain.base.TransitInfo;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.TransitInfoService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
@Namespace("/transit")
@ParentPackage("struts-default")
public class TransitInfoAction extends BaseAction<TransitInfo, Integer> {

    @Autowired
    private TransitInfoService transitInfoService;

    @Override
    BaseService<TransitInfo, Integer> getService() {
        return transitInfoService;
    }

    @Action("beginTransit")
    public String beginTransit() throws IOException {
        String[] idStringArray = getIdArray().trim().split(" ");
        List<Integer> ids = new ArrayList<>();
        for (String idString : idStringArray) {
            ids.add(Integer.parseInt(idString));
        }

        transitInfoService.beginTransit(ids);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("success");

        return NONE;
    }
}
