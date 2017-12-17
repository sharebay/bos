package com.fly.bos.web.action;

import com.fly.bos.domain.base.InOutStorageInfo;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.InOutStorageInfoService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Scope("prototype")
@Namespace("/inoutstore")
@ParentPackage("struts-default")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo, Integer> {
    @Autowired
    private InOutStorageInfoService inOutStorageInfoService;
    @Override
    BaseService<InOutStorageInfo, Integer> getService() {
        return inOutStorageInfoService;
    }

    /**
     * 保存对象
     * @return
     * @throws IOException
     */
    @Override
    @Action("save")
    public String save() throws IOException {

        inOutStorageInfoService.save(getModel(), transitInfoId);
        writeText("success");
        return NONE;
    }
    //属性驱动 transitInfoId
    private Integer transitInfoId;
    public Integer getTransitInfoId() {
        return transitInfoId;
    }
    public void setTransitInfoId(Integer transitInfoId) {
        this.transitInfoId = transitInfoId;
    }
}
