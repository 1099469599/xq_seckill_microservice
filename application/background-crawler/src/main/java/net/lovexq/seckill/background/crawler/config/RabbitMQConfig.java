package net.lovexq.seckill.background.crawler.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LuPindong
 * @time 2017-05-24 08:06
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue initializeQueue() {
        return new Queue("LianJiaCrawler.Initialize.Queue");
    }

    @Bean
    public Queue checkQueue() {
        return new Queue("LianJiaCrawler.Check.Queue");
    }

}