package com.fly.bos.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fly.bos.domain.base.WayBill;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.WayBillService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
@Namespace("/waybill")
@ParentPackage("struts-default")
public class WayBillAction extends BaseAction<WayBill, Long> {
    @Autowired
    private WayBillService wayBillService;
    @Override
    BaseService<WayBill, Long> getService() {
        return wayBillService;
    }

    @Override
    @Action("queryPage")
    public String findAllForPage() throws IOException {
        Pageable pageable = new PageRequest(getPage() - 1, getRows());

        Page<WayBill> page = null;
        if (getQueryBuilder() == null) {
            page = wayBillService.findAll(pageable);
        } else {
            page = wayBillService.findAll(getQueryBuilder(), pageable);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());

        String jsonString = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(jsonString);
        return NONE;
    }

    private QueryBuilder getQueryBuilder() {
        String wayBillNum = getModel().getWayBillNum();
        String sendAddress = getModel().getSendAddress();
        String recAddress = getModel().getRecAddress();
        String sendProNum = getModel().getSendProNum();
        Integer signStatus = getModel().getSignStatus();

        if (StringUtils.isBlank(wayBillNum)
                && StringUtils.isBlank(sendAddress)
                && StringUtils.isBlank(recAddress)
                && StringUtils.isBlank(sendProNum)
                && (signStatus == null || signStatus == 0)) {
            return null;
        } else {
            //bool查询，包括must mustnot 和 should 对应 and not or
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            //term查询：精确值查询
            if (StringUtils.isNotBlank(wayBillNum)) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("wayBillNum", wayBillNum);
                boolQueryBuilder.must(termQueryBuilder);
            }
            //WildcardQuery模糊查询（这里未使用）
            //MatchQuery全文检索（默认操作符OR，改成AND提高搜索精度）
            if (StringUtils.isNotBlank(sendAddress)) {
                MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("sendAddress", sendAddress);
                matchQueryBuilder.operator(MatchQueryBuilder.Operator.AND);
                boolQueryBuilder.must(matchQueryBuilder);
            }
            if (StringUtils.isNotBlank(recAddress)) {
                MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("recAddress", recAddress);
                matchQueryBuilder.operator(MatchQueryBuilder.Operator.AND);
                boolQueryBuilder.must(matchQueryBuilder);
            }
            if (StringUtils.isNotBlank(sendProNum)) {
                MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("sendProNum", sendProNum);
                matchQueryBuilder.operator(MatchQueryBuilder.Operator.AND);
//                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("sendProNum", sendProNum);
                boolQueryBuilder.must(matchQueryBuilder);
            }
            if (signStatus != null && signStatus != 0) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("signStatus", signStatus);
                boolQueryBuilder.must(termQueryBuilder);
            }
            return boolQueryBuilder;
        }
    }


}
