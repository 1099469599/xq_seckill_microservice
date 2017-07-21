package net.lovexq.seckill.background.core.support.thymeleaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;

import java.io.File;
import java.io.FileWriter;

/**
 * @author LuPindong
 * @time 2017-07-13 23:33
 */
public class PageGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageGenerator.class);

    public static void staticPage(GenParam genParam) throws Exception {

        // 构造模板引擎
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(genParam.getResolver());

        // 渲染模板
        Assert.notNull(genParam.getTemplateName(), "templateName must not be null!");
        String filePath = genParam.getOutputPath() + genParam.getTemplateName().split("_")[0] + "/";
        //LOGGER.warn("output file path: {}", filePath);
        File file = new File(filePath);
        if (!file.exists()) file.mkdirs();
        Assert.notNull(genParam.getHouseCode(), "HouseCode must not be null!");
        // 输出文件
        FileWriter write = new FileWriter(filePath + genParam.getHouseCode() + ".shtml");
        templateEngine.process(genParam.getTemplateName(), genParam.getContext(), write);
    }
}