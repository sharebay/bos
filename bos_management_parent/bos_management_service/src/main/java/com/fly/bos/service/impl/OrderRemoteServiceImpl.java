package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.AreaDao;
import com.fly.bos.dao.FixedAreaDao;
import com.fly.bos.dao.OrderDao;
import com.fly.bos.dao.WorkBillDao;
import com.fly.bos.domain.base.*;
import com.fly.bos.service.OrderRemoteService;
import com.fly.bos.util.MailUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("orderRemoteService")
@Transactional
public class OrderRemoteServiceImpl implements OrderRemoteService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private FixedAreaDao fixedAreaDao;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private WorkBillDao workBillDao;

    @Override
    public void save(Order order) {

        //瞬时态对象提取
        Area sendArea = order.getSendArea();
        Area recArea = order.getRecArea();
        order.setSendArea(null);
        order.setRecArea(null);
        orderDao.save(order);
        //自动分单,根据用户获取关联定区
        //获取用户定区ID
        String fixedAreaId = WebClient.create("http://localhost:8080/")
                .path("crm/services/customerService/customers/"
                        + order.getCustomer_id() + "/fixedAreaId")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        if (StringUtils.isNotBlank(fixedAreaId)) {
            FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);
            Set<Courier> couriers = fixedArea.getCouriers();
            if (couriers != null && couriers.size() > 0) {
                for (Courier courier : couriers) {
                    if (null != courier) {
                        order.setOrderType("自动分单");
                        order.setCourier(courier);
                        //生成工单
                        WorkBill workBill = new WorkBill();
                        workBill.setAttachbilltimes(0);
                        workBill.setBuildtime(new Date());
                        workBill.setCourier(courier);
                        workBill.setOrder(order);
                        workBill.setPickstate("未取件");
                        workBill.setRemark(order.getRemark());
                        workBill.setSmsNumber(RandomStringUtils.randomNumeric(4));
                        workBill.setType("新单");
                        workBillDao.save(workBill);
                        //发送通知，通知快递员取件
                        //MailUtils.sendMail("取件", order.getSendAddress(), courier.getEmail());
                        return;
                    }
                }
            }
        }
        //根据分区关键字获取定区,自动分单
        Area area = areaDao.findByProvinceAndCityAndDistrict(sendArea.getProvince(),
                sendArea.getCity(),
                sendArea.getDistrict());
        Set<SubArea> subareas = area.getSubareas();
        if (null != subareas && subareas.size() > 0) {
            for (SubArea subarea : subareas) {
                if (order.getSendAddress().contains(subarea.getKeyWords())
                        &&order.getSendAddress().contains(subarea.getAssistKeyWords())) {
                    //匹配成功，获取定区
                    FixedArea fixedArea = subarea.getFixedArea();
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (couriers != null && couriers.size() > 0) {
                        for (Courier courier : couriers) {
                            if (null != courier) {
                                order.setOrderType("自动分单");
                                order.setCourier(courier);
                                //生成工单
                                WorkBill workBill = new WorkBill();
                                workBill.setAttachbilltimes(0);
                                workBill.setBuildtime(new Date());
                                workBill.setCourier(courier);
                                workBill.setOrder(order);
                                workBill.setPickstate("未取件");
                                workBill.setRemark(order.getRemark());
                                workBill.setSmsNumber(RandomStringUtils.randomNumeric(4));
                                workBill.setType("新单");
                                workBillDao.save(workBill);
                                //发送通知，通知快递员取件
                                //MailUtils.sendMail("取件", order.getSendAddress(), courier.getEmail());
                                return;
                            }
                        }
                    }
                }
            }
        }
        //手动分单
        order.setOrderType("手动分单");
    }

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }
}
