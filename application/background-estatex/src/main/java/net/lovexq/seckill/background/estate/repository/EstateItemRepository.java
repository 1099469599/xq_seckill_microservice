package net.lovexq.seckill.background.estate.repository;

import net.lovexq.seckill.background.core.repository.BasicRepository;
import net.lovexq.seckill.background.domain.estate.model.EstateItemModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateItemRepository extends BasicRepository<EstateItemModel, Long> {

    String UPDATESTATE_SQL = "UPDATE EstateItemModel i SET saleState = ?2, updateTime = ?3  WHERE i.houseCode=?1";


    EstateItemModel findByHouseCode(String houseCode);

    List<EstateItemModel> findTop20ByHouseCodeLikeAndSaleState(String houseCode, String saleState);

    @Modifying
    @Query(UPDATESTATE_SQL)
    void updateState(String houseCode, String state, LocalDateTime updateTime);
}