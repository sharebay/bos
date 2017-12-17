package com.fly.bos.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fly.bos.domain.base.Customer;
import com.fly.bos.domain.base.SubArea;
import com.fly.bos.service.SubAreaService;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.fly.bos.domain.base.FixedArea;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.FixedAreaService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

@Controller
@Scope("prototype")
@Namespace("/fixedarea")
@ParentPackage("struts-default")
public class FixedAreaAction extends BaseAction<FixedArea, String> {

    private static final long serialVersionUID = 1L;

    @Autowired
    private FixedAreaService fixedAreaService;
    @Autowired
    private SubAreaService subAreaService;

    @Override
    BaseService<FixedArea, String> getService() {
        return fixedAreaService;
    }


    /**
     * 重写查询条件
     * @return
     */
    @Override
    protected Specification<FixedArea> getSpecification() {
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                String id = getModel().getId();
                if (StringUtils.isNotBlank(id)) {
                    predicates.add(cb.like(root.get("province").as(String.class), "%" + id + "%"));
                }
                String company = getModel().getCompany();
                if (StringUtils.isNotBlank(company)) {
                    predicates.add(cb.like(root.get("city").as(String.class), "%" + company + "%"));
                }

                Predicate[] arrayPredicates = new Predicate[predicates.size()];
                return cb.and(predicates.toArray(arrayPredicates));
            }
        };
        return specification;
    }

    /**
     * 条件查询客户
     * @return
     */
    @Action("customers")
    public String customers() throws IOException {

        Map<String, Collection<? extends Customer>> map = new HashMap<>();
        Collection<? extends Customer> rightCustomers = WebClient.create("http://localhost:8080")
                .path("/crm/services/customerService/customers/FixedAreaId/" + getModel().getId())
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);

        Collection<? extends Customer> leftCustomers = WebClient.create("http://localhost:8080")
                .path("/crm/services/customerService/customers/FixedAreaId/isNull")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);

        map.put("left", leftCustomers);
        map.put("right", rightCustomers);

        String jsonString = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(jsonString);

        return NONE;
    }
    //属性驱动
    private boolean unassociated;
    public boolean isUnassociated() {
        return unassociated;
    }
    public void setUnassociated(boolean unassociated) {
        this.unassociated = unassociated;
    }


    /**
     * 关联用户和定区
     * @return
     */
    @Action("associateCustomersWithFixedArea")
    public String associateCustomersWithFixedArea() throws IOException {

        WebClient.create("http://localhost:8080")
                .path("/crm/services/customerService/customers/fixedAreaId/"
                        + getModel().getId()
                        +"/customerIds/"
                        + customerIds)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .put(null);


        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("success");

        return NONE;
    }

    private String customerIds;
    public String getCustomerIds() {
        return customerIds;
    }
    public void setCustomerIds(String customerIds) {
        this.customerIds = customerIds;
    }

    /**
     * 定区关联快递员
     * @return
     */
    @Action("associateCourierWithFixedArea")
    public String associateCourierWithFixedArea() {

        System.out.println(courierId);
        System.out.println(takeTimeId);

        fixedAreaService.associateCourier(getModel().getId(), courierId, takeTimeId);

        return NONE;
    }

    //属性驱动：快递员ID，早晚班
    private Integer courierId;
    private Integer takeTimeId;
    public Integer getCourierId() {
        return courierId;
    }
    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }
    public Integer getTakeTimeId() {
        return takeTimeId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    /**
     * 查找关联的分区和未关联分区
     * @return
     */
    @Action("subarea")
    public String subarea() throws IOException {

        Map<String, Set<SubArea>> map = new HashMap<>();

        FixedArea fixedArea = fixedAreaService.findOne(getModel().getId());

        Set<SubArea> rightSubarea = subAreaService.findByFixedArea(fixedArea);
        Set<SubArea> leftSubarea = subAreaService.findByFixedAreaIsNull();

        map.put("left", leftSubarea);
        map.put("right", rightSubarea);

        String jsonString = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(jsonString);
        return NONE;
    }
}
