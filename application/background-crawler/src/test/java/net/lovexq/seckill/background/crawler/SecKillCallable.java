package net.lovexq.seckill.background.crawler;

import com.alibaba.fastjson.JSON;
import net.lovexq.seckill.background.crawler.support.lianjia.LianJiaCrawler;
import net.lovexq.seckill.common.model.JsonResult;
import org.apache.http.Header;

import java.util.concurrent.Callable;

/**
 * @author LuPindong
 * @time 2017-07-23 18:23
 */
public class SecKillCallable implements Callable<String> {

    private long id;
    private int userNo;

    private Header[] agentHeaderArr = new Header[]{};
    private String bodyContent = "";
    private JsonResult jsonResult = new JsonResult();

    private String exposureUrl = "http://www.lovexq.net/application-background-special/specials/{id}/exposure";
    private String executionUrl = "http://www.lovexq.net/application-background-special/specials/{id}/execution/{key}";

    public SecKillCallable(long id, int userNo) {
        this.id = id;
        this.userNo = userNo;
    }

    @Override
    public String call() throws Exception {
        System.out.println(id + " : " + userNo);
        String url = exposureUrl.replace("{id}", String.valueOf(id)) + "?account=user100" + String.valueOf(userNo);
        bodyContent = LianJiaCrawler.doGet(url, agentHeaderArr);
        jsonResult = JSON.parseObject(bodyContent, JsonResult.class);
        System.out.println(">>> exposureUrl jsonResult: " + jsonResult.toString());

        if (200 == jsonResult.getStatus()) {
            String key = (String) jsonResult.getData();
            System.out.println(">>> key: " + key);
            url = executionUrl.replace("{id}", String.valueOf(id)).replace("{key}", key) + "?captcha=xx&account=user100" + String.valueOf(userNo);
            System.out.println("doPost: " + LianJiaCrawler.doPost(url));
        }

        return jsonResult.getMessage();
    }
}
