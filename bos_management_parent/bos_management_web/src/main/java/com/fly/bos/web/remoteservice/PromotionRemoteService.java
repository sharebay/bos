package com.fly.bos.web.remoteservice;

import com.fly.bos.domain.base.Promotion;
import com.fly.bos.service.PromotionService;
import com.fly.bos.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/promotions")
@Service("promotionRemoteService")
public class PromotionRemoteService {

    @Autowired
    private PromotionService promotionService;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{page}/{pageSize}")
    public PageBean<Promotion> findAll(
            @PathParam("page") Integer page,
            @PathParam("pageSize") Integer pageSize
    ) {
        Pageable pageable = new PageRequest(page - 1, pageSize);
        Page<Promotion> result = promotionService.findAll(new Specification<Promotion>() {
            @Override
            public Predicate toPredicate(Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        }, pageable);
        PageBean<Promotion> pageBean = new PageBean<>();
        pageBean.setTotal(result.getTotalElements());
        pageBean.setData(result.getContent());
        return pageBean;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id}")
    public Promotion findOne(@PathParam("id") Integer id) {
        Promotion promotion = promotionService.findOne(id);
        return promotion;
    }
}
