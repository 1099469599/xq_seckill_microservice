package net.lovexq.seckill.background.crawler.service.impl;

import net.lovexq.seckill.background.core.properties.AppProperties;
import net.lovexq.seckill.background.crawler.client.EstateFeignClient;
import net.lovexq.seckill.background.crawler.repository.CrawlerRecordRepository;
import net.lovexq.seckill.background.crawler.service.CrawlerService;
import net.lovexq.seckill.background.crawler.support.lianjia.*;
import net.lovexq.seckill.background.crawler.support.rabbimq.MqProducer;
import net.lovexq.seckill.background.domain.crawler.model.CrawlerRecordModel;
import net.lovexq.seckill.background.domain.estate.dto.EstateItemDTO;
import net.lovexq.seckill.background.domain.estate.model.EstateImageModel;
import net.lovexq.seckill.background.domain.estate.model.EstateItemModel;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.BeanMapUtil;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.common.utils.enums.CrawlerRecordEnum;
import net.lovexq.seckill.common.utils.enums.EstateEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LuPindong
 * @time 2017-04-27 08:12
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerServiceImpl.class);
    private static final String QUERY_NOT_IMAGE_SQL = "SELECT r.id,r.batch,r.history_code,r.current_code,r.state,r.create_time,r.update_time,r.data FROM crawler_record r " +
            "LEFT JOIN estate_image i ON i.house_code = r.history_code WHERE r.batch = ? AND r.state = '更新' AND i.house_code IS NULL";
    @Autowired
    private CrawlerRecordRepository crawlerRecordRepository;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private MqProducer mqProducer;
    @Autowired
    private EstateFeignClient estateFeignClient;

    @Override
    @Transactional
    public JsonResult invokeInitialize(String baseUrl, String region, Integer curPage, Integer totalPage) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, baseUrl, region, curPage, totalPage);
            result = exec.submit(new LianJiaInitializeCallable(lianJiaParam, mqProducer, "LianJiaCrawler.Initialize.Queue")).get();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        } finally {
            exec.shutdown();
        }

        return result;
    }

    @Override
    @Transactional
    public JsonResult invokeCheck(String batch, String baseUrl, String region) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, batch, baseUrl, region);
            result = exec.submit(new LianJiaCheckCallable(lianJiaParam, mqProducer, "LianJiaCrawler.Check.Queue")).get();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        } finally {
            exec.shutdown();
        }
        return result;
    }

    @Override
    @Transactional
    public JsonResult invokeUpdate(String batch, Integer curPage) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            List<EstateItemDTO> estateItemList = new ArrayList();
            // 处理下架和更新情况
            List<CrawlerRecordModel> crawlerRecordList = crawlerRecordRepository.findByBatchAndStateIn(batch, Arrays.asList(CrawlerRecordEnum.DEFAULT.getValue(), CrawlerRecordEnum.CREATE.getValue()));
            for (CrawlerRecordModel crawlerRecord : crawlerRecordList) {

                String state = crawlerRecord.getState();
                // 下架操作
                if (CrawlerRecordEnum.DEFAULT.getValue().equals(state)) {
                    crawlerRecord.setState(CrawlerRecordEnum.DELETE.getValue());
                    crawlerRecordRepository.save(crawlerRecord);
                    estateFeignClient.updateItemState(crawlerRecord.getHistoryCode(), EstateEnum.INVALID.getValue());
                    // 新增操作
                } else {
                    EstateItemDTO estateItemDTO = ProtoStuffUtil.deserialize(crawlerRecord.getData(), EstateItemDTO.class);
                    estateItemList.add(estateItemDTO);
                }
            }

            // 处理需要更新图片情况
            List<Map> mapList = crawlerRecordRepository.queryForMapList(QUERY_NOT_IMAGE_SQL, batch);
            for (Map map : mapList) {
                CrawlerRecordModel recordModel = new CrawlerRecordModel();
                BeanMapUtil.mapToBean(map, recordModel);
                EstateItemDTO estateItemDTO = ProtoStuffUtil.deserialize(recordModel.getData(), EstateItemDTO.class);
                estateItemDTO.setCrawlerState(recordModel.getState().toString());
                estateItemList.add(estateItemDTO);
            }
            LianJiaParam lianJiaParam = new LianJiaParam(estateItemList, appProperties, batch, curPage);
            result = exec.submit(new LianJiaAddCallable(lianJiaParam, mqProducer, "LianJiaCrawler.Initialize.Queue")).get();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        } finally {
            exec.shutdown();
        }
        return result;
    }

    @Override
    @Transactional
    public void saveInitializeData(byte[] dataArray) throws Exception {
        try {
            EstateItemDTO dto = ProtoStuffUtil.deserialize(dataArray, EstateItemDTO.class);
            EstateItemModel model = new EstateItemModel();

            // 先查看数据库是否已存在该记录
            dataArray = estateFeignClient.findItemByHouseCode(dto.getHouseCode());
            if (dataArray != null) {
                model = ProtoStuffUtil.deserialize(dataArray, EstateItemModel.class);
                BeanUtils.copyProperties(dto, model, "id");
                // 先删除原有图片
                estateFeignClient.deleteImagesByHouseCode(dto.getHouseCode());
            } else {
                BeanUtils.copyProperties(dto, model);
            }
            // 保存房源条目
            estateFeignClient.saveItem(ProtoStuffUtil.serialize(model));
            // 保存房源图片
            saveImages(dto);

            // 生成静态页面
            generateStaticPage(BeanMapUtil.beanToMap(dto), "estate_detailUI");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * 保存房源图片
     *
     * @param dto
     */
    private void saveImages(EstateItemDTO dto) {
        List<EstateImageModel> imageList = dto.getEstateImageList();
        if (CollectionUtils.isNotEmpty(imageList)) {
            for (EstateImageModel image : imageList) {
                // 普通图片
                if (image.getPictureId() != null) {
                    estateFeignClient.saveImage(ProtoStuffUtil.serialize(image));
                    // 户型图
                } else {
                    image.setPictureId(System.currentTimeMillis());
                    image.setHouseCode(dto.getHouseCode());
                    image.setPictureType(99);
                    if (StringUtils.isBlank(image.getPictureSourceUrl())) {
                        image.setPictureSourceUrl(image.getUrl());
                    }
                    estateFeignClient.saveImage(ProtoStuffUtil.serialize(image));
                }
            }
        }
    }

    @Override
    @Transactional
    public void saveCheckData(byte[] dataArray) throws Exception {
        try {
            EstateItemDTO dto = ProtoStuffUtil.deserialize(dataArray, EstateItemDTO.class);
            String batch = dto.getBatch();
            String houseCode = dto.getHouseCode();
            CrawlerRecordModel crawlerRecordModel = crawlerRecordRepository.findByBatchAndHistoryCode(batch, houseCode);
            if (crawlerRecordModel != null) {
                crawlerRecordModel.setState(CrawlerRecordEnum.UPDATE.getValue());
            } else {
                crawlerRecordModel = new CrawlerRecordModel(batch, houseCode, CrawlerRecordEnum.CREATE.getValue());
                List<CrawlerRecordModel> list = crawlerRecordRepository.findByBatchAndCurrentCode(batch, houseCode);
                if (CollectionUtils.isNotEmpty(list)) {
                    crawlerRecordRepository.deleteRepeatRecord(batch, houseCode);
                }
            }
            crawlerRecordModel.setData(dataArray);
            crawlerRecordRepository.save(crawlerRecordModel);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void generateStaticPage(Map dataMap, String templateName) throws Exception {
        //构造上下文(Model)
        Context context = new Context(Locale.CHINA, dataMap);

        //构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCacheable(true);
        resolver.setCharacterEncoding(AppConstants.CHARSET_UTF8);
        resolver.setTemplateMode("LEGACYHTML5");
        resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //渲染模板
        Assert.notNull(templateName, "templateName must not be null!");
        String filePath = appProperties.getProducesPath() + templateName.split("_")[0] + "/";
        File file = new File(filePath);
        if (!file.exists()) file.mkdirs();

        String houseCode = MapUtils.getString(dataMap, "houseCode");
        Assert.notNull(houseCode, "HouseCode must not be null!");


        FileWriter write = new FileWriter(filePath + houseCode + ".shtml");
        templateEngine.process(templateName, context, write);
    }

    @Override
    public JsonResult invokeFullUpdate() {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            result = exec.submit(new LianJiaFullCallable(estateFeignClient, this)).get();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        } finally {
            exec.shutdown();
        }

        return result;
    }

}