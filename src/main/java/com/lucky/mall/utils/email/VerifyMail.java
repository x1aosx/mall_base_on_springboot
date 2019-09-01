package com.lucky.mall.utils.email;

/**
 * @Description 验证邮箱格式
 * @Author shuxian.xiao
 * @Date 2019/8/5 16:56
 */
public class VerifyMail {
    /**
     * 邮箱格式
     */
    private static final String REGEX = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";

    /**
     * 判断是否合法
     * @param email 邮箱
     * @return true 邮箱合法，false邮箱非法
     */
    public static boolean isLegal(String email){
        return email != null && email.matches(REGEX);
    }
}