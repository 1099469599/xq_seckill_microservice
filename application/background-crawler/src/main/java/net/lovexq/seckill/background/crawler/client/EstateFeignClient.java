package net.lovexq.seckill.background.crawler.client;

import net.lovexq.seckill.background.domain.estate.model.EstateImageModel;
import net.lovexq.seckill.background.domain.estate.model.EstateItemModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author LuPindong
 * @time 2017-05-24 09:25
 */
@FeignClient(name = "application-background-estate")
public interface EstateFeignClient {

    //@GetMapping("/estates/{houseCode}")
    @RequestMapping(value = "/estates/{houseCode}", method = RequestMethod.GET)
    EstateItemModel findItemByHouseCode(@PathVariable("houseCode") String houseCode);

    //@PutMapping("/estates/{houseCode}/state")
    @RequestMapping(value = "/estates/{houseCode}/state", method = RequestMethod.PUT)
    void updateItemState(@PathVariable("houseCode") String houseCode);

    //@PostMapping("/estates")
    @RequestMapping(value = "/estates", method = RequestMethod.POST)
    EstateItemModel saveItem(@RequestBody EstateItemModel estateItemModel);

    //@DeleteMapping("/estates/{houseCode}/images")
    @RequestMapping(value = "/estates/{houseCode}/images", method = RequestMethod.DELETE)
    Long deleteImagesByHouseCode(@PathVariable("houseCode") String houseCode);

    //@PostMapping("/estates/images")
    @RequestMapping(value = "/estates/images", method = RequestMethod.POST)
    EstateItemModel saveImage(@RequestBody EstateImageModel estateImageModel);

}