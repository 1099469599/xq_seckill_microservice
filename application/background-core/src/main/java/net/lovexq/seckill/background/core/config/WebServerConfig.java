package net.lovexq.seckill.background.core.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author LuPindong
 * @time 2017-07-23 16:27
 */
@Configuration
public class WebServerConfig {

    @Bean
    public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
        tomcatFactory.setUriEncoding(StandardCharsets.UTF_8);
        tomcatFactory.setSessionTimeout(3600, TimeUnit.SECONDS);
        tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return tomcatFactory;
    }
}

class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
    public void customize(Connector connector) {
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        //设置最大线程数
        protocol.setMaxThreads(1024);
        //设置最大连接数
        protocol.setMaxConnections(2048);
        //设置连接超时时间
        protocol.setConnectionTimeout(30000);
    }
}