package net.lovexq.seckill.background.core.support;

import net.lovexq.seckill.background.core.properties.AppProperties;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Map;

/**
 * @author LuPindong
 * @time 2017-07-13 09:33
 */
public class ThymeleafUtil {

    public static void generateStaticPage(Map dataMap, String templateName, AppProperties appProperties) throws Exception {
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
}
