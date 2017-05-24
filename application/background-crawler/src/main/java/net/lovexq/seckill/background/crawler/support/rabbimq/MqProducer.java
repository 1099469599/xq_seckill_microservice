package net.lovexq.seckill.background.crawler.support.rabbimq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by LuPindong on 2017-2-15.
 */
@Component
public class MqProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqProducer.class);

    private final AmqpAdmin amqpAdmin;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public MqProducer(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.amqpTemplate = amqpTemplate;
    }

    public void sendQueueMessage(byte[] dataArray, String queueName) {
        try {
            LOGGER.info("【发送消息】>>>队列目的地：{}", queueName);
            amqpTemplate.convertAndSend(queueName, dataArray);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}