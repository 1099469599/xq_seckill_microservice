package net.lovexq.seckill.background.crawler.client;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author LuPindong
 * @time 2017-07-13 06:56
 */
@Component
public class EstateFeignClientFallbackFactory implements FallbackFactory<EstateFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstateFeignClientFallbackFactory.class);

    @Override
    public EstateFeignClient create(Throwable throwable) {
        return new EstateFeignClient() {
            @Override
            public void saveItem(@RequestBody byte[] estateItemData) {
                LOGGER.error("{} Fallback, Reason Was: {}", "saveItem",  throwable);
            }

            @Override
            public byte[] findItemByHouseCode(@PathVariable("houseCode") String houseCode) {
                LOGGER.error("{} Fallback, Reason Was: {}", "findItemByHouseCode", throwable);
                return null;
            }

            @Override
            public void saveImage(String houseCode, @RequestBody byte[] estateImageData) {
                LOGGER.error("{} Fallback, Reason Was: {}", "saveImage",  throwable);
            }

            @Override
            public Long deleteImagesByHouseCode(@PathVariable("houseCode") String houseCode) {
                LOGGER.error("{} Fallback, Reason Was: {}", "deleteImagesByHouseCode",  throwable);
                return null;
            }

            @Override
            public void updateItemState(@PathVariable("houseCode") String houseCode, @PathVariable("state") String state) {
                LOGGER.error("{} Fallback, Reason Was: {}", "updateItemState",  throwable);
            }

            @Override
            public byte[] listAllByPage(@PathVariable("page") int page) {
                LOGGER.error("{} Fallback, Reason Was: {}", "listAllByPage",  throwable);
                return null;
            }
        };
    }
}