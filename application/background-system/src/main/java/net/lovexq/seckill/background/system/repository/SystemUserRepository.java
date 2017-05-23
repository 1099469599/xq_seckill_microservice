package net.lovexq.seckill.background.system.repository;

import net.lovexq.seckill.background.core.repository.BasicRepository;
import net.lovexq.seckill.background.system.model.SystemUserModel;

/**
 * 系统用户
 *
 * @author LuPindong
 * @time 2017-04-29 23:53
 */
public interface SystemUserRepository extends BasicRepository<SystemUserModel, Long> {
    SystemUserModel findByAccount(String account);

    SystemUserModel findByEmail(String email);
}
