package net.lovexq.seckill.background.estate.service;

import net.lovexq.seckill.background.domain.estate.dto.EstateItemDTO;
import net.lovexq.seckill.background.domain.estate.model.EstateImageModel;
import net.lovexq.seckill.background.domain.estate.model.EstateItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 房源业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface EstateService {

    Page<EstateItemDTO> listForSaleByPage(Pageable pageable, Map<String, Object> paramMap) throws Exception;

    List<EstateImageModel> listImageByHouseCode(String houseCode);

    List<EstateItemModel> findTop20ByHouseCodeLikeAndSaleState(String houseCode, String saleState);

    EstateItemModel save(EstateItemModel estateItem);

    EstateItemModel findItemByHouseCode(String houseCode);

    void updateItemState(String houseCode, String state);

    Long deleteImagesByHouseCode(String houseCode);

    EstateImageModel saveImage(EstateImageModel estateImageModel);

    List<EstateItemDTO> listAllByPage(Pageable pageable);
}