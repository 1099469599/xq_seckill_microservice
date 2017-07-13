package net.lovexq.seckill.background.system.controller;

import net.lovexq.seckill.background.core.controller.BasicController;
import net.lovexq.seckill.background.system.model.SystemConfigModel;
import net.lovexq.seckill.background.system.service.ConfigService;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 配置控制层
 *
 * @author LuPindong
 * @time 2017-05-01 09:41
 */
@RestController
public class ConfigController extends BasicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private ConfigService configService;

    @PostMapping(value = "/configs")
    public void save(@RequestBody byte[] configData) {
        configService.save(ProtoStuffUtil.deserialize(configData, SystemConfigModel.class));
    }

    @GetMapping(value = "/configs/{key}")
    public byte[] findByConfigKey(@PathVariable("key") String key) {
        SystemConfigModel systemConfigModel = configService.getByConfigKey(key);
        if (systemConfigModel != null) {
            return ProtoStuffUtil.serialize(systemConfigModel);
        }
        return null;
    }
}