package net.lovexq.seckill.background.estate.repository;

import net.lovexq.seckill.background.core.repository.BasicRepository;
import net.lovexq.seckill.background.domain.estate.model.EstateImageModel;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateImageRepository extends BasicRepository<EstateImageModel, String> {

    Long deleteByHouseCode(String houseCode);
}