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
@FeignClient(name = "application-background-system", fallbackFactory = EstateFeignClientFallbackFactory.class)
public interface ConfigFeignClient {

    @RequestMapping(value = "/configs", method = RequestMethod.POST)
    void save(@RequestBody byte[] configData);

    @RequestMapping(value = "/configs/{key}", method = RequestMethod.GET)
    byte[] findByConfigKey(@PathVariable("key") String key);
}