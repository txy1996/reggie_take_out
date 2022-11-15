package com.txy.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 启动类
 * @date 2022/11/7 20:50
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement //开启事务
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功...");
    }
}
