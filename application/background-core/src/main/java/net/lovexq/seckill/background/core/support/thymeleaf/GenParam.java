package net.lovexq.seckill.background.core.support.thymeleaf;

import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author LuPindong
 * @time 2017-07-13 09:33
 */
public class GenParam {
    private ClassLoaderTemplateResolver resolver;
    private Context context;
    private String templateName;
    private String outputPath;
    private String houseCode;

    public GenParam(ClassLoaderTemplateResolver resolver, Context context, String templateName, String outputPath, String houseCode) {
        this.resolver = resolver;
        this.context = context;
        this.templateName = templateName;
        this.outputPath = outputPath;
        this.houseCode = houseCode;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ClassLoaderTemplateResolver getResolver() {
        return resolver;
    }

    public void setResolver(ClassLoaderTemplateResolver resolver) {
        this.resolver = resolver;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }
}