package com.fly.bos.web.action;

import com.fly.bos.domain.base.SignInfo;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.SignInfoService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
@Controller
@Scope("prototype")
@Namespace("/sign")
@ParentPackage("struts-default")
public class SignInfoAction extends BaseAction<SignInfo, Integer> {
    @Autowired
    private SignInfoService signInfoService;

    @Override
    BaseService<SignInfo, Integer> getService() {
        return signInfoService;
    }

    /**
     * 重写保存对象
     * @return
     * @throws IOException
     */
    @Override
    @Action("save")
    public String save() throws IOException {

        signInfoService.save(getModel(), transitInfoId);
        writeText("success");
        return NONE;
    }
    //属性注入
    private Integer transitInfoId;
    public Integer getTransitInfoId() {
        return transitInfoId;
    }
    public void setTransitInfoId(Integer transitInfoId) {
        this.transitInfoId = transitInfoId;
    }
}
