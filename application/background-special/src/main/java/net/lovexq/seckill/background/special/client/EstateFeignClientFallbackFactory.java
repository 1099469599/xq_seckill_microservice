package net.lovexq.seckill.background.special.client;

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
            public byte[] listByHouseCode(String houseCode) {
                LOGGER.error("{} Fallback, Reason Was: {}", "listByHouseCode",  throwable);
                return null;
            }

            @Override
            public void saveImage(@PathVariable("houseCode") String houseCode, @RequestBody byte[] estateImageData) {
                LOGGER.error("{} Fallback, Reason Was: {}", "saveImage",  throwable);
            }

            @Override
            public byte[] findTop20ByHouseCodeLikeAndSaleState(String targetCode, String value) {
                LOGGER.error("{} Fallback, Reason Was: {}", "findTop20ByHouseCodeLikeAndSaleState",  throwable);
                return null;
            }
        };
    }
}