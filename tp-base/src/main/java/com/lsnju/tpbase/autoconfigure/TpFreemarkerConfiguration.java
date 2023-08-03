package com.lsnju.tpbase.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.lsnju.tpbase.util.FreemarkerStaticModels;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/8/24 8:43
 * @version V1.0
 */
@Slf4j
@Configuration
public class TpFreemarkerConfiguration {

    @Configuration
    @ConditionalOnClass(freemarker.template.Configuration.class)
    public static class FreemarkerNonWebConfig {

        @Bean
        public FreemarkerNonWebConfigBean freemarkerNonWebConfigBean(ApplicationContext context, FreemarkerStaticModels models) {
            log.debug("=========================================");
            log.debug("== setup freemarkerNonWeb staticModels ==");
            log.debug("=========================================");
            try {
                freemarker.template.Configuration configuration = context.getBean(freemarker.template.Configuration.class);
                models.forEach(configuration::setSharedVariable);
            } catch (BeansException e) {
                log.error(" freemarker.template.Configuration not found");
            }
            return new FreemarkerNonWebConfigBean();
        }

        public static class FreemarkerNonWebConfigBean {}
    }

    @Configuration
    @ConditionalOnClass(FreeMarkerViewResolver.class)
    public static class FreemarkerWebConfig {

        @Bean
        public FreemarkerWebConfigBean freemarkerWebConfigBean(ApplicationContext context, FreemarkerStaticModels models) {
            log.debug("======================================");
            log.debug("== setup freemarkerWeb staticModels ==");
            log.debug("======================================");
            try {
                FreeMarkerViewResolver freeMarkerViewResolver = context.getBean(FreeMarkerViewResolver.class);
                freeMarkerViewResolver.setAttributesMap(models);
            } catch (BeansException e) {
                log.error("FreeMarkerViewResolver not found");
            }
            return new FreemarkerWebConfigBean();
        }

        public static class FreemarkerWebConfigBean {}
    }

}
