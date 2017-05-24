package net.lovexq.seckill.background.crawler.client;

import net.lovexq.seckill.domain.estate.model.EstateImageModel;
import net.lovexq.seckill.domain.estate.model.EstateItemModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author LuPindong
 * @time 2017-05-24 09:25
 */
@FeignClient(name = "application-background-estate")
public interface EstateFeignClient {

    @GetMapping("/estates/{houseCode}")
    EstateItemModel findItemByHouseCode(@PathVariable("houseCode") String houseCode);

    @PutMapping("/estates/{houseCode}/state")
    void updateItemState(@PathVariable("houseCode") String houseCode);

    @PostMapping("/estates")
    EstateItemModel saveItem(@RequestBody EstateItemModel estateItemModel);

    @DeleteMapping("/estates/{houseCode}/images")
    Long deleteImagesByHouseCode(@PathVariable("houseCode") String houseCode);

    @PostMapping("/estates/images")
    EstateItemModel saveImage(@RequestBody EstateImageModel estateImageModel);

}