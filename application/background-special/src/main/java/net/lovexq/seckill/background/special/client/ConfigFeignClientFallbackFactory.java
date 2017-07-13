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
public class ConfigFeignClientFallbackFactory implements FallbackFactory<ConfigFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFeignClientFallbackFactory.class);

    @Override
    public ConfigFeignClient create(Throwable throwable) {
        return new ConfigFeignClient() {
            @Override
            public void save(@RequestBody byte[] configData) {

                LOGGER.error("Fallback, Reason Was: {}", throwable);
            }

            @Override
            public byte[] findByConfigKey(@PathVariable("key") String key) {
                LOGGER.error("Fallback, Reason Was: {}", throwable);
                return null;
            }
        };
    }
}