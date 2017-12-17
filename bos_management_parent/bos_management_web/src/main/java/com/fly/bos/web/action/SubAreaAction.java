package com.fly.bos.web.action;

import com.fly.bos.domain.base.Area;
import com.fly.bos.service.AreaService;
import com.fly.bos.util.PinYin4jUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.fly.bos.domain.base.SubArea;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.SubAreaService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
@Namespace("/subarea")
@ParentPackage("struts-default")
public class SubAreaAction extends BaseAction<SubArea, String> {

    private static final long serialVersionUID = 1L;
    @Autowired
    private SubAreaService subAreaService;
    @Autowired
    private AreaService areaService;

    @Override
    BaseService<SubArea, String> getService() {
        return subAreaService;
    }

    /**
     * 上传xml文件
     * @return
     */
    @Action("upload")
    public String upload() throws IOException {

        Workbook wb = new XSSFWorkbook(new FileInputStream(subareaFile));

        Sheet sheet = wb.getSheetAt(0);

        List<SubArea> list = new ArrayList<>();
        for (Row row : sheet) {

            //跳过第一行
            if (row.getRowNum() == 0) {
                continue;
            }

            SubArea area = new SubArea();
            String id = row.getCell(0).getStringCellValue();
            String province = row.getCell(1).getStringCellValue();
            String city = row.getCell(2).getStringCellValue();
            String district = row.getCell(3).getStringCellValue();
            String keyWords = row.getCell(4).getStringCellValue();
            String startNum = row.getCell(5).getStringCellValue();
            String endNum = row.getCell(6).getStringCellValue();
            String assistKeyWords = row.getCell(8).getStringCellValue();

            area.setId(id);
            area.setKeyWords(keyWords);
            area.setAssistKeyWords(assistKeyWords);
            area.setStartNum(startNum);
            area.setEndNum(endNum);

            province = province.substring(0, province.length());
            city = city.substring(0, city.length());
            district = district.substring(0, district.length() - 1);

            String tempStr = province+city+district;//河北石家庄开发
            String[] headByString = PinYin4jUtils.getHeadByString(tempStr);//[H,B,S,J,Z,K,F]
            String shortcode = StringUtils.join(headByString, "");
            List<Area> areaList = areaService.findByShortcode(shortcode);
            if (areaList != null && areaList.size() > 0)
                area.setArea(areaList.get(0));

            list.add(area);
        }
        subAreaService.save(list);

        wb.close();


        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("success");
        return NONE;
    }
    private File subareaFile;

    public File getSubareaFile() {
        return subareaFile;
    }

    public void setSubareaFile(File subareaFile) {
        this.subareaFile = subareaFile;
    }
}
