package net.lovexq.seckill.background.system.service;

import net.lovexq.seckill.background.system.model.SystemConfigModel;

/**
 * @author LuPindong
 * @time 2017-05-01 09:42
 */
public interface ConfigService {

    void save(SystemConfigModel deserialize);

    SystemConfigModel getByConfigKey(String key);
}