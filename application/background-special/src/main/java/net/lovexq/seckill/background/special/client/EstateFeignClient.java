package net.lovexq.seckill.background.special.client;

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

    @RequestMapping(value = "/estates", method = RequestMethod.POST)
    void saveItem(@RequestBody byte[] estateItemData);

    @RequestMapping(value = "/estates/{houseCode}/images", method = RequestMethod.GET)
    byte[] listByHouseCode(@PathVariable("houseCode") String houseCode);

    @RequestMapping(value = "/estates/{houseCode}/images", method = RequestMethod.POST)
    void saveImage(@PathVariable("houseCode") String houseCode, @RequestBody byte[] estateImageData);

    @RequestMapping(value = "/estates/top20/{houseCode}/{state}", method = RequestMethod.GET)
    byte[] findTop20ByHouseCodeLikeAndSaleState(@PathVariable("houseCode") String houseCode, @PathVariable("state") String state);
}