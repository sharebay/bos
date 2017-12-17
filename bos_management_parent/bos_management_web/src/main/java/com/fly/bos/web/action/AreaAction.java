package com.fly.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.fly.bos.domain.base.Area;
import com.fly.bos.service.AreaService;
import com.fly.bos.service.BaseService;
import com.fly.bos.util.PinYin4jUtils;

@Controller
@Scope("prototype")
@Namespace("/area")
@ParentPackage("struts-default")
public class AreaAction extends BaseAction<Area, String>  {

	private static final long serialVersionUID = 1L;
	@Autowired
	private AreaService areaService;

	@Override
	BaseService<Area, String> getService() {
		return areaService;
	}

	
	/***********************************
	 * 接收上传文件
	 * @return
	 * @throws IOException 
	 ***********************************/
	@Action("upload")
	public String upload() throws IOException {
		
		Workbook wb = new HSSFWorkbook(new FileInputStream(areaFile));
		
		Sheet sheet = wb.getSheetAt(0);
		
		List<Area> list = new ArrayList<>();
		for (Row row : sheet) {
			
			//跳过第一行
			if (row.getRowNum() == 0) {
				continue;
			}
			
			Area area = new Area();
			String id = row.getCell(0).getStringCellValue();
			String province = row.getCell(1).getStringCellValue();
			String city = row.getCell(2).getStringCellValue();
			String district = row.getCell(3).getStringCellValue();
			String postcode = row.getCell(4).getStringCellValue();
			
			area.setId(id);
			area.setPostcode(postcode);
			area.setProvince(province);
			area.setCity(city);
			area.setDistrict(district);
			
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			
			String tempStr = province+city+district;//河北石家庄开发
			String[] headByString = PinYin4jUtils.getHeadByString(tempStr);//[H,B,S,J,Z,K,F]
			String shortcode = StringUtils.join(headByString, "");
			area.setShortcode(shortcode);
			//2.citycode
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);
			
			list.add(area);
		}
		areaService.save(list);
		
		wb.close();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("success");
		return NONE;
	}
	private File areaFile;
	public File getAreaFile() {
		return areaFile;
	}
	public void setAreaFile(File areaFile) {
		this.areaFile = areaFile;
	}
	
	@Override
	protected Specification<Area> getSpecification() {
		
		Specification<Area> specification = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<>();
				
				String province = getModel().getProvince();
				if (StringUtils.isNotBlank(province)) {
					predicates.add(cb.like(root.get("province").as(String.class), "%" + province + "%"));
				}
				String city = getModel().getCity();
				if (StringUtils.isNotBlank(city)) {
					predicates.add(cb.like(root.get("city").as(String.class), "%" + city + "%"));
				}
				String district = getModel().getDistrict();
				if (StringUtils.isNotBlank(district)) {
					predicates.add(cb.like(root.get("district").as(String.class), "%" + district + "%"));
				}
				
				Predicate[] arrayPredicates = new Predicate[predicates.size()];
				return cb.and(predicates.toArray(arrayPredicates));
			}
		};
		
		return specification;
	}

}
