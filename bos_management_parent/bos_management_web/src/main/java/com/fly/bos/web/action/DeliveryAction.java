package com.fly.bos.web.action;

import com.fly.bos.domain.base.DeliveryInfo;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.DeliveryInfoService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Scope("prototype")
@Namespace("/delivery")
@ParentPackage("struts-default")
public class DeliveryAction extends BaseAction<DeliveryInfo, Integer> {
    @Autowired
    private DeliveryInfoService deliveryInfoService;
    @Override
    BaseService<DeliveryInfo, Integer> getService() {
        return deliveryInfoService;
    }

    /**
     * 重写保存对象
     * @return
     * @throws IOException
     */
    @Override
    @Action("save")
    public String save() throws IOException {

        deliveryInfoService.save(getModel(), transitInfoId);
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
