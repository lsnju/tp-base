package com.lsnju.tpbase.autoconfigure;

import java.io.IOException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.lsnju.tpbase.util.FreemarkerStaticModels;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/8/24 8:42
 * @version V1.0
 */
@Slf4j
@Configuration
@Import({
    TpFreemarkerConfiguration.FreemarkerNonWebConfig.class,
    TpFreemarkerConfiguration.FreemarkerWebConfig.class,
})
@ConditionalOnClass(freemarker.template.Template.class)
public class TpFreemarkerAutoConfiguration {

    /** FreemarkerStaticModels location */
    private static final String LOCATION = "classpath:cc/fm/staticClass.properties";

    @Bean
    public FreemarkerStaticModels freemarkerStaticModels(ResourceLoader resourceLoader) throws IOException {
        Resource location = resourceLoader.getResource(LOCATION);
        log.debug("location = {}", location);
        log.debug("location = {}", location.getURL().getPath());
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        pfb.setLocation(location);
        pfb.afterPropertiesSet();
        FreemarkerStaticModels fsm = FreemarkerStaticModels.getInstance();
        fsm.setStaticModels(pfb.getObject());
        return fsm;
    }

}
