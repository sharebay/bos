package com.fly.bos.web.action;

import com.alibaba.fastjson.JSON;
import com.fly.bos.domain.base.Promotion;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.PromotionService;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/promotion")
public class PromotionAction extends BaseAction<Promotion, Integer> {

    @Autowired
    private PromotionService promotionService;
    @Override
    BaseService<Promotion, Integer> getService() {
        return promotionService;
    }


    /**
     * 保存促销
     * @return
     * @throws IOException
     */
    @Override
    @Action("save")
    public String save() throws IOException {

        //获取当前绝对路径,用于保存文件
        String absolutePath = ServletActionContext.getServletContext().getRealPath("/titleImage/");
        //获取相对路径，用于回显路径
        String relativePath = ServletActionContext.getRequest().getContextPath() + "/titleImage/";
        //生成随机文件名
        String fileName = UUID.randomUUID().toString()
                + titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
        //保存文件
        FileUtils.copyFile(titleImgFile, new File(absolutePath + "/" + fileName));
        //保存相对路径
        getModel().setTitleImg(relativePath + fileName);

        return super.save();
    }

    private File titleImgFile;
    private String titleImgFileFileName;
    public File getTitleImgFile() {
        return titleImgFile;
    }
    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }
    public String getTitleImgFileFileName() {
        return titleImgFileFileName;
    }
    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    /**
     * 接收图片
     * @return
     */
    @Action("uploadImage")
    public String uploadImage() throws IOException {

        try {
            //获取当前绝对路径,用于保存文件
            String absolutePath = ServletActionContext.getServletContext().getRealPath("/attached/");
            //获取相对路径，用于回显路径
            String relativePath = ServletActionContext.getRequest().getContextPath() + "/attached/";
            //生成随机文件名
            String fileName = UUID.randomUUID().toString()
                    + imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            //保存文件
            FileUtils.copyFile(imgFile, new File(absolutePath + "/" + fileName));
            //返回json结果
            Map<String, Object> map = new HashMap<>();
            map.put("error", 0);
            map.put("url", relativePath + fileName);
            String jsonString = JSON.toJSONString(map);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/json;charset=UTF-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", 1);
            map.put("message", "出现异常");
            String jsonString = JSON.toJSONString(map);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/json;charset=UTF-8");
            response.getWriter().write(jsonString);
            e.printStackTrace();
        }

        return NONE;
    }

    private File imgFile;
    private String imgFileFileName;

    public File getImgFile() {
        return imgFile;
    }
    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
    public String getImgFileFileName() {
        return imgFileFileName;
    }
    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }


    /**
     * 管理图片
     * @return
     */
    @Action("manageImage")
    public String manageImage() throws IOException {
        //根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "attached/";
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        HttpServletRequest request = ServletActionContext.getRequest();
        PrintWriter out = ServletActionContext.getResponse().getWriter();
        String rootUrl  = request.getContextPath() + "/attached/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        //根据path参数，设置各路径和URL
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

        //不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
            out.println("Access is not allowed.");
            return NONE;
        }
        //最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            out.println("Parameter is not valid.");
            return NONE;
        }
        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if(!currentPathFile.isDirectory()){
            out.println("Directory does not exist.");
            return NONE;
        }

        //遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if(currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if(file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if(file.isFile()){
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        if ("size".equals(order)) {
            Collections.sort(fileList, new SizeComparator());
        } else if ("type".equals(order)) {
            Collections.sort(fileList, new TypeComparator());
        } else {
            Collections.sort(fileList, new NameComparator());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("moveup_dir_path", moveupDirPath);
        result.put("current_dir_path", currentDirPath);
        result.put("current_url", currentUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);

        String jsonString = JSON.toJSONString(result);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(jsonString);

        return NONE;
    }


}

class NameComparator implements Comparator {
    public int compare(Object a, Object b) {
        Hashtable hashA = (Hashtable)a;
        Hashtable hashB = (Hashtable)b;
        if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
            return -1;
        } else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
            return 1;
        } else {
            return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
        }
    }
}
class SizeComparator implements Comparator {
    public int compare(Object a, Object b) {
        Hashtable hashA = (Hashtable)a;
        Hashtable hashB = (Hashtable)b;
        if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
            return -1;
        } else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
            return 1;
        } else {
            if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
                return 1;
            } else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
class TypeComparator implements Comparator {
    public int compare(Object a, Object b) {
        Hashtable hashA = (Hashtable)a;
        Hashtable hashB = (Hashtable)b;
        if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
            return -1;
        } else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
            return 1;
        } else {
            return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
        }
    }
}