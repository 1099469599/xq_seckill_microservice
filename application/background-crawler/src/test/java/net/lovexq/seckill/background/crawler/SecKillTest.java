package net.lovexq.seckill.background.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.lovexq.seckill.background.crawler.support.lianjia.LianJiaCrawler;
import net.lovexq.seckill.common.model.JsonResult;
import org.apache.http.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author LuPindong
 * @time 2017-07-23 18:16
 */
@RunWith(SpringRunner.class)
public class SecKillTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillTest.class);

    private final Integer threadSize = 10;
    private Header[] agentHeaderArr = new Header[]{};
    private String bodyContent = "";
    private JsonResult jsonResult = new JsonResult();

    private String dataUrl = "http://www.lovexq.net/application-background-special/specials";

    @Test
    public void testExecute() throws ExecutionException, InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool();

        bodyContent = LianJiaCrawler.doGet(dataUrl, agentHeaderArr);
        jsonResult = JSON.parseObject(bodyContent, JsonResult.class);
        System.out.println(">>> dataUrl jsonResult: " + jsonResult.toString());

        if (200 == jsonResult.getStatus()) {
            JSONArray jsonArray = (JSONArray) jsonResult.getData();
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.getLongValue("id");

                for (int j = 0; j < threadSize; j++) {
                    Future<String> future = exec.submit(new SecKillCallable(id,i + j));
                    LOGGER.debug(">>> i future.get(): ", future.get());
                }
            }
        }

        exec.shutdown();

    }
}
