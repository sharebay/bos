package com.fly.bos.action;

import com.aliyuncs.exceptions.ClientException;
import com.fly.bos.domain.Customer;
import com.fly.bos.util.MailUtils;
import com.fly.bos.util.MessageUtils;
import com.fly.bos.util.QueueSender;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@Namespace("/customer")
@ParentPackage("struts-default")
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private QueueSender queueSender;

    //模型驱动
    Customer customer = new Customer();
    @Override
    public Customer getModel() {
        return customer;
    }

    /**
     * 客户登录
     * @return
     */
    @Action(value = "login", results = {
            @Result(name = "success", location = "/index.html#/myhome", type = "redirect"),
            @Result(name = "fail", location = "/login.html", type = "redirect")
    })
    public String login() {

        if (StringUtils.isNotBlank(customer.getMobilePhone()) && StringUtils.isNotBlank(customer.getPassword())) {

            Customer existCustomer = WebClient.create("http://localhost:8080/")
                    .path("crm/services/customerService/customers/mobilePhone/"
                            + this.customer.getMobilePhone()
                            + "/password/"
                            + this.customer.getPassword())
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Customer.class);
            if (existCustomer != null) {
                ServletActionContext.getRequest().getSession().setAttribute("loginCustomer", existCustomer);
                return SUCCESS;
            }
        }
        return "fail";
    }



    /**
     * 发送短信获取验证码
     * @return
     */
    @Action("getCode")
    public String getCode() throws ClientException {

        //随机生成验证码，4位
        String code = RandomStringUtils.randomNumeric(4);
        //将验证码存入session
        ServletActionContext.getRequest().getSession().setAttribute("verificationCode", code);
        //调用工具类发送短信给客户
//        MessageUtils.sendSms(phoneNumbers, code);
        //调用消息队列，将“发短信”入队
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumbers", phoneNumbers);
        map.put("code", code);
        queueSender.send("sms", map);

        System.out.println(code);

        return NONE;
    }

    private String phoneNumbers;
    public String getPhoneNumbers() {
        return phoneNumbers;
    }
    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * 用户注册
     * @return
     */
    @Action(value = "signUp", results = {
            @Result(name = "success", location = "/signup-success.html", type = "redirect"),
            @Result(name = "fail", location = "/signup-fail.html", type = "redirect")
    })
    public String signUp() {

        String verificationCode = (String) ServletActionContext.getRequest().getSession().getAttribute("verificationCode");
        if (StringUtils.isNotBlank(verificationCode)
                && StringUtils.isNotBlank(this.verificationCode)
                && verificationCode.equals(this.verificationCode)) {
            //验证成功
            //清空session验证码
            ServletActionContext.getRequest().getSession().removeAttribute("verificationCode");
            //调用服务注册
            try {
                WebClient.create("http://localhost:8080/")
                        .path("crm/services/customerService/customers")
                        .type(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .post(customer);
                //发送激活邮件
                String activeCode = RandomStringUtils.randomNumeric(64);
                redisTemplate.opsForValue().set(customer.getMobilePhone(), activeCode, 24, TimeUnit.HOURS);
                MailUtils.sendMail("激活邮件",
                        "激活地址：<a href="
                                + MailUtils.activeUrl
                                + "?activeCode="
                                + activeCode
                                + "&mobilePhone="
                                + customer.getMobilePhone()
                                + ">点我激活</a>",
                        customer.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }

        } else {
            //验证码有误
            return "fail";
        }

        return SUCCESS;
    }

    //属性驱动，验证码
    private String verificationCode;
    public String getVerificationCode() {
        return verificationCode;
    }
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }


    /**
     * 激活邮件激活账号
     * @return
     */
    @Action(value = "activeMail", results = {
            @Result(name = "success", location = "/activeSuccess.html", type = "redirect"),
            @Result(name = "fail", location = "/activeFail.html", type = "redirect")
    })
    public String activeMail() {

        System.out.println(activeCode);
        System.out.println(customer.getMobilePhone());
        String activeCodeFromRedis = redisTemplate.opsForValue().get(customer.getMobilePhone());
        if (StringUtils.isNotBlank(activeCodeFromRedis) &&
                activeCodeFromRedis.equals(activeCode)) {
            //激活成功
            try {
                WebClient.create("http://localhost:8080/")
                        .path("crm/services/customerService/customers/mobilePhone/"
                                + customer.getMobilePhone())
                        .type(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .put(null);
                redisTemplate.delete(customer.getMobilePhone());
                return SUCCESS;
            } catch (Exception e) {
                return "fail";
            }
        } else {
            return "fail";
        }
    }
    private String activeCode;

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
}
