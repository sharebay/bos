package com.fly.bos.web.action;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fly.bos.service.BaseService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T, ID extends Serializable> extends ActionSupport implements ModelDriven<T> {


	//抽象方法，获取BaseService
	abstract BaseService<T, ID> getService();

	private Class<T> modelClass;
	private Class<ID> idClass;

	//模型驱动
	private T model;
	public BaseAction() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		modelClass = (Class<T>) pt.getActualTypeArguments()[0];
		idClass = (Class<ID>) pt.getActualTypeArguments()[1];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	@Override
	public T getModel() {
		return model;
	}



	/***********************************
	 * 分页查询
	 * @param
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("queryPage")
	public String findAllForPage() throws IOException {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<T> page = getService().findAll(getSpecification(), pageable);

		Map<String, Object> map = new HashMap<>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());

		String jsonString = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);

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
	//子类重写此方法，实现条件查询
	protected Specification<T> getSpecification() {
		return null;
	}
	/***********************************
	 * 保存对象/更新对象
	 * @param
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("save")
	public String save() throws IOException {
		getService().save(model);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("success");

		return NONE;
	}

	/***********************************
	 * 查询所有
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("findAll")
	public String findAll() throws IOException {
		Iterable<T> all = getService().findAll();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(JSON.toJSONString(all, SerializerFeature.DisableCircularReferenceDetect));
		return NONE;
	}

	/***********************************
	 * 获取总数
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("count")
	public String count() throws IOException {
		long count = getService().count();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write((int) count);
		return NONE;
	}

	/***********************************
	 * 通过Id删除一个对象
	 * @return
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 ***********************************/
	@Action("deleteById")
	public String delete() throws IOException, IllegalArgumentException, IllegalAccessException {

		//从值栈获取id
		Object id = ActionContext.getContext().getValueStack().findValue("id");

		if (id != null) {
			//如果存在
			getService().delete((ID)id);
		} else {
			//如果不存在，从model中提取

			return NONE;
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("success");
		return NONE;
	}

	/***********************************
	 * 删除一组对象
	 * @return
	 * @throws IOException 
	 ***********************************/

	@Action("deleteAllById")
	public String deleteAll() throws IOException {

		//将前端传入的idArray字符串转换成id字符串数组
		String[] idStringArray = idArray.trim().split(" ");
		//通过ConvertUtils转换为ID数组
		ID[] ids = (ID[]) ConvertUtils.convert(idStringArray, idClass);
		//调用service删除
		if (deleteLogically) {
			//逻辑删除
			deleteLogically(Arrays.asList(ids));
		} else {
			//真正删除
			getService().delete(Arrays.asList(ids));
		}

		//返回结果
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("success");
		return NONE;
	}

	//逻辑删除方法，让子类实现
	protected void deleteLogically(List<ID> ids) {
	}
	//属性驱动，获取删除的id数组字符串
	private String idArray;
	//是否逻辑删除，true：逻辑删除，false：真正删除.默认逻辑删除
	private boolean deleteLogically = true;
	public String getIdArray() {
		return idArray;
	}
	public void setIdArray(String idArray) {
		this.idArray = idArray;
	}
	public boolean isDeleteLogically() {
		return deleteLogically;
	}
	public void setDeleteLogically(boolean deleteLogically) {
		this.deleteLogically = deleteLogically;
	}


	protected void writeText(String text) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(text);
    }

    protected void writeJson(String json) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
