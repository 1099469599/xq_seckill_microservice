package net.lovexq.seckill.background;

import net.lovexq.seckill.background.core.repository.impl.BasicRepositoryImpl;
import net.lovexq.seckill.background.core.support.dynamicds.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 程序入口
 *
 * @author LuPindong
 * @time 2017-04-19 06:53
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@ServletComponentScan
@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源
@EnableJpaRepositories(repositoryBaseClass = BasicRepositoryImpl.class) // 启用Jpa基类
public class EstateApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EstateApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[]{EstateApplication.class});
    }
}