package net.lovexq.seckill.background.crawler.support.rabbimq;

import net.lovexq.seckill.background.crawler.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by LuPindong on 2017-2-15.
 */
@Component
public class MqCustomer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqCustomer.class);

    @Autowired
    private CrawlerService crawlerService;

    /**
     * 处理创建记录消息
     */
    @RabbitListener(queues = "LianJiaCrawler.Initialize.Queue")
    public void receiveInitializeQueueMessage(Message message) {
        try {
            byte[] dataArray = message.getBody();
            LOGGER.info("【接收消息】>>>队列目的地：{}", message.getMessageProperties().getConsumerQueue());
            crawlerService.saveInitializeData(dataArray);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 处理更新记录消息
     */
    @RabbitHandler
    @RabbitListener(queues = "LianJiaCrawler.Check.Queue")
    public void receiveCheckQueueMessage(Message message) {
        try {
            byte[] dataArray = message.getBody();
            LOGGER.info("【接收消息】>>>队列目的地：{}", message.getMessageProperties().getConsumerQueue());
            crawlerService.saveCheckData(dataArray);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
