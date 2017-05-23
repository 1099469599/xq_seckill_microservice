package net.lovexq.seckill;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author LuPindong
 * @time 2017-05-19 08:29
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulApplication {

    @Value("")

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}