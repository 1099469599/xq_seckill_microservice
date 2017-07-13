package net.lovexq.seckill.background.estate.controller;

import net.lovexq.seckill.background.core.controller.BasicController;
import net.lovexq.seckill.background.domain.estate.dto.EstateItemDTO;
import net.lovexq.seckill.background.domain.estate.model.EstateImageModel;
import net.lovexq.seckill.background.domain.estate.model.EstateItemModel;
import net.lovexq.seckill.background.estate.service.EstateService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 房源控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@RestController
public class EstateController extends BasicController {

    @Autowired
    private EstateService estateService;

    @GetMapping("/estates")
    public JsonResult listWithGetUI(HttpServletRequest request) throws Exception {
        pageable = buildPageRequest(request);
        Map<String, Object> paramMap = buildParamMap(request);
        Page<EstateItemDTO> estateItemPage = estateService.listForSaleByPage(pageable, paramMap);
        result.setData(estateItemPage);
        return result;
    }

    @PostMapping("/estates")
    public void saveItem(@RequestBody byte[] estateItemData) {
        EstateItemModel estateItemModel = ProtoStuffUtil.deserialize(estateItemData, EstateItemModel.class);
        estateService.save(estateItemModel);
    }

    @GetMapping("/estates/{houseCode}")
    public byte[] findItemByHouseCode(@PathVariable("houseCode") String houseCode) {
        EstateItemModel estateItemModel = estateService.findItemByHouseCode(houseCode);
        if (estateItemModel != null) {
            return ProtoStuffUtil.serialize(estateItemModel);
        }
        return null;
    }

    @PostMapping("/estates/{houseCode}/images")
    public void saveImage(@PathVariable("houseCode") String houseCode, @RequestBody byte[] estateImageData) {
        EstateImageModel estateImageModel = ProtoStuffUtil.deserialize(estateImageData, EstateImageModel.class);
        estateService.saveImage(estateImageModel);
    }

    @GetMapping(value = "/estates/{houseCode}/images")
    public byte[] listByHouseCode(@PathVariable("houseCode") String houseCode) {
        List<EstateImageModel> list = estateService.listImageByHouseCode(houseCode);
        if (CollectionUtils.isNotEmpty(list)) {
            return ProtoStuffUtil.serializeList(list);
        }
        return null;
    }

    @DeleteMapping("/estates/{houseCode}/images")
    public Long deleteImagesByHouseCode(@PathVariable("houseCode") String houseCode) {
        return estateService.deleteImagesByHouseCode(houseCode);
    }

    @PostMapping("/estates/{houseCode}/{state}")
    public void updateItemState(@PathVariable("houseCode") String houseCode, @PathVariable("state") String state) {
        estateService.updateItemState(houseCode, state);
    }

    @GetMapping("/estates/all/{page}")
    public byte[] listAllByPage(@PathVariable("page") int page) {
        pageable = new PageRequest(page - 1, 100);
        List<EstateItemDTO> list = estateService.listAllByPage(pageable);
        if (CollectionUtils.isNotEmpty(list)) {
            return ProtoStuffUtil.serializeList(list);
        }
        return null;
    }

    @GetMapping(value = "/estates/top20/{houseCode}/{state}")
    public byte[] listByHouseCode(@PathVariable("houseCode") String houseCode, @PathVariable("state") String state) {
        List<EstateItemModel> list = estateService.findTop20ByHouseCodeLikeAndSaleState(houseCode, state);
        if (CollectionUtils.isNotEmpty(list)) {
            return ProtoStuffUtil.serializeList(list);
        }
        return null;
    }
}