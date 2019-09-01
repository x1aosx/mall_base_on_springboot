package com.lucky.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * @Description 启动类
 * @Author shuxian.xiao
 * @Date 2019/8/21 17:24
 */

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.lucky.mall.pojo")
@MapperScan("com.lucky.mall.mapper")
public class MallApplication extends SpringBootServletInitializer {
    /**
     * 启动
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

    /**
     * 为了打包springboot项目
     * @param builder builder
     * @return 略
     */
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
