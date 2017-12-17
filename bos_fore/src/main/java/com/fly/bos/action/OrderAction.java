package com.fly.bos.action;

import com.fly.bos.domain.Area;
import com.fly.bos.domain.Customer;
import com.fly.bos.domain.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.UUID;

public class OrderAction extends ActionSupport implements ModelDriven<Order> {
    Order order = new Order();

    @Override
    public Order getModel() {
        return order;
    }

    /**
     * 保存订单
     * @return
     */
    @Action(value = "save", results = {
            @Result(name = "success", location = "/", type = "redirect"),
            @Result(name = "login", location = "/login.html", type = "redirect")
    })
    public String save() {

        if (StringUtils.isNotBlank(sendAreaInfo)) {
            String[] s = sendAreaInfo.split("/");
            Area sendArea = new Area();
            sendArea.setProvince(s[0]);
            sendArea.setCity(s[1]);
            if (s.length > 2)
                sendArea.setDistrict(s[2]);
            getModel().setSendArea(sendArea);
        }
        if (StringUtils.isNotBlank(recAreaInfo)) {
            String[] s = recAreaInfo.split("/");
            Area recArea = new Area();
            recArea.setProvince(s[0]);
            recArea.setCity(s[1]);
            if (s.length > 2)
                recArea.setDistrict(s[2]);
            getModel().setRecArea(recArea);
        }

        Customer loginCustomer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("loginCustomer");
        if (loginCustomer != null) {
            getModel().setCustomer_id(loginCustomer.getId());
        } else {
            return "login";
        }

        getModel().setOrderTime(new Date());
        getModel().setOrderNum(UUID.randomUUID().toString());

        WebClient.create("http://localhost:8080/")
                .path("bos_management_web/services/orderService/orders")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(getModel());

        return SUCCESS;
    }
    //属性驱动
    private String sendAreaInfo;
    private String recAreaInfo;
    public String getSendAreaInfo() {
        return sendAreaInfo;
    }
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    public String getRecAreaInfo() {
        return recAreaInfo;
    }
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
}
