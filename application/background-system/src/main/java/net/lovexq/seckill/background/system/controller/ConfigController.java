package net.lovexq.seckill.background.system.controller;

import net.lovexq.seckill.background.core.controller.BasicController;
import net.lovexq.seckill.background.system.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置控制层
 *
 * @author LuPindong
 * @time 2017-05-01 09:41
 */
@RestController
@RequestMapping("/configs")
public class ConfigController extends BasicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private ConfigService configService;

}