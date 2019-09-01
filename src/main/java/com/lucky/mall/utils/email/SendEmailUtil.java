package com.lucky.mall.utils.email;

import lombok.Data;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
/**
 * @Description 邮件发送工具类
 * @Author shuxian.xiao
 * @Date 2019/8/22 9:27
 */

@Data
public class SendEmailUtil {
    /**
     * 收件人
     */
    private String emailAddress;

    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String message;
    /**
     * 注册验证链接
     */
    private String verifyLink;
    /**
     * 随机生成的最大值
     */
    private static final Integer RAND_MAX = 1000000;

    /**
     * 有参构造方法
     * @param emailAddress 发送地址
     */
    public SendEmailUtil(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * 随机生成6位验证码
     * @return 6位验证码
     */
    public String randomCode(){
        return String.valueOf((int)((Math.random()* RAND_MAX)));
    }

    /**
     * 发送邮箱验证码
     * @param validity 有效期/单位自行填写
     * @param verifyCode 验证码
     * @return 邮件样式
     */
    private static String prettyQrCodeLayout(String validity,String verifyCode){
        return "<div>邮箱验证码的有效期为:"+validity+"\n邮箱验证码如下:</div>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td colspan=\"2\" style=\"font-size:\n" +
                "12px; line-height: 20px; padding-top: 14px;\n" +
                "padding-bottom: 25px; color: #909090;\">\n" +
                "                    <div>"+verifyCode+"</div>\n" +
                "                </td>\n" +
                "            </tr>\n";
    }


    /**
     * 发送邮件
     * @return 验证码
     */
    public String send() {
        message = randomCode();
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.163.com");
//        email.setHostName("smtp.qq.com");
//        email.setHostName("smtp.gmail.com");
        email.setCharset("UTF-8");
        try {
            email.addTo(emailAddress);

            email.setFrom("xsx1075312923@163.com", "饰品商城");
//            email.setFrom("1075312923@qq.com", "xiaosx");
//            email.setFrom("shawsxian@gmail.com","xiaosx");
            email.setAuthentication("xsx1075312923@163.com", "xsx123456");
//            email.setAuthentication("1075312923@qq.com", "dpdqsiokovwljbda");
//            email.setAuthentication("shawsxian@gmail.com","xian1183089686*");
            email.setMsg(prettyQrCodeLayout("5分钟", message));
            email.setSubject(subject);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return message;
    }




}