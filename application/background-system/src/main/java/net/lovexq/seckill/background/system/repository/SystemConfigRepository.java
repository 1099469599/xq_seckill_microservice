package net.lovexq.seckill.background.system.repository;

import net.lovexq.seckill.background.core.repository.BasicRepository;
import net.lovexq.seckill.background.system.model.SystemConfigModel;

/**
 * 系统配置
 *
 * @author LuPindong
 * @time 2017-04-29 23:53
 */
public interface SystemConfigRepository extends BasicRepository<SystemConfigModel, Integer> {

    SystemConfigModel findByConfigKey(String key);
}