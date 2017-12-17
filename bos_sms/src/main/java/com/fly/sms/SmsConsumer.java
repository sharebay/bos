
package com.fly.sms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Component;

/**
 *
 * @description 队列消息监听器
 *
 */
@Component
public class SmsConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            String phoneNumbers = ((MapMessage) message).getString("phoneNumbers");
            String code = ((MapMessage) message).getString("code");
            System.out.println(phoneNumbers + " " + code);
            //发送短信
//            SmsUtils.sendSms(phoneNumbers, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
