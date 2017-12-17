package com.fly.bos.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.fly.bos.domain.base.Courier;
import com.fly.bos.domain.base.Standard;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.CourierService;

@Controller
@Namespace("/courier")
@ParentPackage("struts-default")
@Scope("prototype")
public class CourierAction extends BaseAction<Courier, Integer> {

	private static final long serialVersionUID = 1L;
	@Autowired
	private CourierService courierService;
	@Override
	BaseService<Courier, Integer> getService() {
		return courierService;
	}

	/**
	 * 条件查询，返回查询条件
	 */
	@Override
	protected Specification<Courier> getSpecification() {
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				//这里可以用反射抽取出来，放到BaseAction中！
				String courierNum = getModel().getCourierNum();
				if (StringUtils.isNotBlank(courierNum)) {
					predicates.add(cb.equal(root.get("courierNum").as(String.class), courierNum));
				}

				String company = getModel().getCompany();
				if (StringUtils.isNotBlank(company)) {
					predicates.add(cb.like(root.get("company").as(String.class), "%" + company + "%"));
				}

				String type = getModel().getType();
				if (StringUtils.isNotBlank(company)) {
					predicates.add(cb.like(root.get("type").as(String.class), "%" + type + "%"));
				}
				if (getModel().getStandard() != null) {
					String name = getModel().getStandard().getName();
					if (StringUtils.isNotBlank(name)) {
						Join<Courier, Standard> join = root.join(root.getModel().getSingularAttribute("standard", Standard.class));
						predicates.add(cb.like(join.get("name").as(String.class), "%" + name + "%"));
					}
				}
				Predicate[] arrayPredicates = new Predicate[predicates.size()];
				return cb.and(predicates.toArray(arrayPredicates));
			}
		};
		return specification;
	}

	/**
	 * 逻辑删除
	 */
	@Override
	protected void deleteLogically(List<Integer> ids) {
		courierService.deleteLogically(ids);
	}
	@Action("restoreAllById")
	public String restoreAllById() throws IOException {
		//将前端传入的idArray字符串转换成id字符串数组
		String[] idStringArray = getIdArray().trim().split(" ");
		//通过ConvertUtils转换为ID数组
		Integer[] ids = (Integer[]) ConvertUtils.convert(idStringArray, Integer.class);
		//调用service还原
		courierService.restore(Arrays.asList(ids));

		//返回结果
		writeText("success");
		return NONE;
	}

}
