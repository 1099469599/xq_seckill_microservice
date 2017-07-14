package net.lovexq.seckill.background.crawler.support.lianjia;

import net.lovexq.seckill.background.core.properties.AppProperties;
import net.lovexq.seckill.background.core.support.thymeleaf.GenParam;
import net.lovexq.seckill.background.core.support.thymeleaf.PageGenerator;
import net.lovexq.seckill.background.crawler.client.EstateFeignClient;
import net.lovexq.seckill.background.domain.estate.dto.EstateItemDTO;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.BeanMapUtil;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * 链家网爬虫多线程执行程序【适用于全量更新操作】
 *
 * @author LuPindong
 * @time 2017-04-22 02:02
 */
public class LianJiaFullCallable implements Callable<JsonResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LianJiaFullCallable.class);

    private EstateFeignClient estateFeignClient;

    private AppProperties appProperties;

    public LianJiaFullCallable(EstateFeignClient estateFeignClient, AppProperties appProperties) {
        this.estateFeignClient = estateFeignClient;
        this.appProperties = appProperties;
    }

    @Override
    public JsonResult call() throws Exception {
        JsonResult result = new JsonResult();
        long beginTime = System.currentTimeMillis();
        int index = 1;
        try {
            byte[] dataArray = estateFeignClient.listAllByPage(index);

            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setCacheable(true);
            resolver.setCharacterEncoding(AppConstants.CHARSET_UTF8);
            resolver.setTemplateMode(StandardTemplateModeHandlers.LEGACYHTML5.getTemplateModeName());
            resolver.setPrefix("templates/");//模板所在目录
            resolver.setSuffix(".html");//模板文件后缀

            while (dataArray != null) {
                List<EstateItemDTO> estateItemList = ProtoStuffUtil.deserializeList(dataArray, EstateItemDTO.class);
                for (EstateItemDTO dto : estateItemList) {
                    //构造上下文(Model)
                    Context context = new Context(Locale.CHINA, BeanMapUtil.beanToMap(dto));
                    GenParam genParam = new GenParam(resolver, context, "estate_detailUI", appProperties.getProducesPath(), dto.getHouseCode());
                    // 生成静态页面
                    PageGenerator.staticPage(genParam);
                }
                dataArray = estateFeignClient.listAllByPage(++index);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("全量更新执行完毕，累计耗时{}分", (endTime - beginTime) / 1000 / 60);
        return result;
    }
}