package net.lovexq.seckill.background.special.service;

import io.jsonwebtoken.Claims;
import net.lovexq.seckill.background.domain.special.dto.SpecialStockDTO;
import net.lovexq.seckill.background.domain.special.model.SpecialOrderModel;
import net.lovexq.seckill.background.domain.special.model.SpecialStockModel;
import net.lovexq.seckill.common.model.JsonResult;

import java.util.List;

/**
 * 特价秒杀业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface SpecialService {

    JsonResult createData() throws Exception;

    List<SpecialStockDTO> listForSecKill() throws Exception;

    SpecialStockDTO getOne(Long id) throws Exception;

    JsonResult exposureSecKillUrl(Long id, Claims claims) throws Exception;

    JsonResult executeSecKill(Long id, String key, String captcha, Claims claims) throws Exception;

    JsonResult decreaseStock(SpecialOrderModel specialOrderModel) throws Exception;

    void saveCaptcha(Claims claims, String captcha) throws Exception;

    List<SpecialStockModel> listReadyStock(String startTime, boolean isAll);
}