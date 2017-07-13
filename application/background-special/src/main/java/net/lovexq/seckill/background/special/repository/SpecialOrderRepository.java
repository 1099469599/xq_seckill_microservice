package net.lovexq.seckill.background.special.repository;

import net.lovexq.seckill.background.core.repository.BasicRepository;
import net.lovexq.seckill.background.domain.special.model.SpecialOrderModel;

/**
 * Created by LuPindong on 2017-4-20.
 */

public interface SpecialOrderRepository extends BasicRepository<SpecialOrderModel, Long> {

    SpecialOrderModel findByStockIdAndAccount(Long id, String account);
}