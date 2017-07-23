package net.lovexq.seckill.background.crawler;

import net.lovexq.seckill.background.crawler.support.rabbimq.MqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author LuPindong
 * @time 2017-07-11 21:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMQ {

    @Autowired
    private MqProducer mqProducer;

    @Test
    public void sendMessage() {
        mqProducer.sendQueueMessage(null,"LianJiaCrawler.Check.Queue");
    }
}