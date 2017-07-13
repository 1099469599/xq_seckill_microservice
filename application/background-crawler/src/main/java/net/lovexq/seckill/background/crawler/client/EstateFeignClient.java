package net.lovexq.seckill.background.crawler.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author LuPindong
 * @time 2017-05-24 09:25
 */
@FeignClient(name = "application-background-estate", fallbackFactory = EstateFeignClientFallbackFactory.class)
public interface EstateFeignClient {

    @RequestMapping(value = "/estates/{houseCode}", method = RequestMethod.GET)
    byte[] findItemByHouseCode(@PathVariable("houseCode") String houseCode);

    @RequestMapping(value = "/estates/{houseCode}/{state}", method = RequestMethod.POST)
    void updateItemState(@PathVariable("houseCode") String houseCode, @PathVariable("state") String state);

    @RequestMapping(value = "/estates", method = RequestMethod.POST)
    void saveItem(@RequestBody byte[] estateItemData);

    @RequestMapping(value = "/estates/{houseCode}/images", method = RequestMethod.DELETE)
    Long deleteImagesByHouseCode(@PathVariable("houseCode") String houseCode);

    @RequestMapping(value = "/estates/images", method = RequestMethod.POST)
    void saveImage(@RequestBody byte[] estateImageData);

    @RequestMapping(value = "/estates/all/{page}", method = RequestMethod.GET)
    byte[] listAllByPage(@PathVariable("page") int page);
}