package com.fly.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.fly.bos.domain.base.Standard;
import com.fly.bos.service.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author 别点我我怕疼
 *
 */
@Namespace("/standard")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

	private static final long serialVersionUID = 1L;
	/**
	 * 模型驱动
	 * @see ModelDriven#getModel()
	 */
	private Standard standard = new Standard();
	@Override
	public Standard getModel() {
		return standard;
	}
	
	@Autowired
	private StandardService standardService;
	
	/***********************************
	 * 添加Standard
	 * @return
	 ***********************************/
	@Action(value = "addStandard", results = {
			@Result(name = "addStandard", location = "/pages/base/standard.jsp", type = "redirect")
	})
	public String addStandard() {
		standardService.save(standard);
		return "addStandard";
	}
	
	/***********************************
	 * 查询所有 json
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("queryPage")
	public String queryPage() throws IOException {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Standard> page = standardService.queryPage(pageable);
		
		Map<String, Object> map = new HashMap<>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		
		String jsonString = JSON.toJSONString(map);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(jsonString);
		return NONE;
	}
	//分页查询属性驱动
	private int page;
	private int rows;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	/***********************************
	 * 删除标准
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("deleteStandards")
	public String deleteStandards() throws IOException {
		String[] ids = idArray.trim().split(" ");
		if (ids.length > 0) {
			for (String id : ids) {
				standardService.deleteStandardById(Integer.parseInt(id));
			}
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("success");
		return NONE;
	}
	
	//属性驱动，获取删除的id数组
	private String idArray;
	public String getIdArray() {
		return idArray;
	}
	public void setIdArray(String idArray) {
		this.idArray = idArray;
	}
	
	
	/***********************************
	 * 返回所有standard的json
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("findAll")
	public String findAll() throws IOException {
		
		List<Standard> list = standardService.findAll();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(JSON.toJSONString(list));
		return NONE;
	}
	
	

}
