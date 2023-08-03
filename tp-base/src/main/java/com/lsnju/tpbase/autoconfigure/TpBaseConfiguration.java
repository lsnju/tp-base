package com.lsnju.tpbase.autoconfigure;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Lists;
import com.lsnju.base.jackson.MoneyDeserializer;
import com.lsnju.base.jackson.MoneySerializer;
import com.lsnju.base.money.Money;
import com.lsnju.tpbase.TpConstants;
import com.lsnju.tpbase.debug.filter.FilterConfigShow;
import com.lsnju.tpbase.debug.filter.WebFilterConfigShow;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.log.aop.DalDigestLogInterceptor;
import com.lsnju.tpbase.log.aop.SalDigestLogInterceptor;
import com.lsnju.tpbase.web.mvc.TpSpringWebMvcHelper;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.AlternateTypeRules;

/**
 *
 * @author lisong
 * @since 2022/8/22 16:50
 * @version V1.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TpBaseConfiguration {

    @Configuration
    @ConditionalOnClass(ServletUriComponentsBuilder.class)
    public static class TpSpringWebMvcConfig {
        @Bean
        @ConditionalOnMissingBean
        TpSpringWebMvcHelper tpSpringWebMvcHelper() {
            log.debug("{} tpSpringWebMvcHelper", TpConstants.PREFIX);
            return new TpSpringWebMvcHelper();
        }
    }

    @Configuration
    @ConditionalOnClass(AlternateTypeRuleConvention.class)
    public static class TpSpringFoxConfig {
        @Bean
        public AlternateTypeRuleConvention moneyTypeConvention() {
            log.debug("{} moneyTypeConvention", TpConstants.PREFIX);
            return new AlternateTypeRuleConvention() {
                @Override
                public int getOrder() {
                    return Ordered.HIGHEST_PRECEDENCE;
                }

                @Override
                public List<AlternateTypeRule> rules() {
                    return Lists.newArrayList(AlternateTypeRules.newRule(Money.class, String.class));
                }
            };
        }
    }

    @Configuration
    @ConditionalOnClass(Jackson2ObjectMapperBuilderCustomizer.class)
    public static class TpJacksonCustomConfig {
        @Bean
        public Jackson2ObjectMapperBuilderCustomizer tpJackson2ObjectMapperBuilderCustomizer() {
            log.debug("{} tpJackson2ObjectMapperBuilderCustomizer", TpConstants.PREFIX);
            return new TpJackson2ObjectMapperBuilderCustomizer();
        }

        static class TpJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                builder.serializers(new MoneySerializer());
                builder.deserializers(new MoneyDeserializer());
                //builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        }
    }

    @Configuration
    @ConditionalOnMissingBean(name = DigestConstants.DAL_DIGEST_PROXY_NAME)
    public static class TpDalDigestConfig {
        @Bean(name = DigestConstants.DAL_DIGEST_INTERCEPTOR_NAME)
        @ConditionalOnMissingBean(name = DigestConstants.DAL_DIGEST_INTERCEPTOR_NAME)
        public Advice dalDigestLogInterceptor() {
            log.debug("{} {}", TpConstants.PREFIX, DigestConstants.DAL_DIGEST_INTERCEPTOR_NAME);
            return new DalDigestLogInterceptor();
        }

        @Bean(DigestConstants.DAL_DIGEST_PROXY_NAME)
        @ConditionalOnMissingBean(name = DigestConstants.DAL_DIGEST_PROXY_NAME)
        public BeanNameAutoProxyCreator dalDigestLog() {
            log.debug("{} {}", TpConstants.PREFIX, DigestConstants.DAL_DIGEST_PROXY_NAME);
            BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
            beanNameAutoProxyCreator.setBeanNames("*Dao", "*DaoImpl");
            beanNameAutoProxyCreator.setInterceptorNames("dalDigestLogInterceptor");
            return beanNameAutoProxyCreator;
        }
    }

    @Configuration
    @ConditionalOnMissingBean(name = DigestConstants.SAL_DIGEST_PROXY_NAME)
    public static class TpSalDigestConfig {
        @Bean(DigestConstants.SAL_DIGEST_INTERCEPTOR_NAME)
        @ConditionalOnMissingBean(name = DigestConstants.SAL_DIGEST_INTERCEPTOR_NAME)
        public Advice salDigestLogInterceptor() {
            log.debug("{} {}", TpConstants.PREFIX, DigestConstants.SAL_DIGEST_INTERCEPTOR_NAME);
            return new SalDigestLogInterceptor();
        }

        @Bean(DigestConstants.SAL_DIGEST_PROXY_NAME)
        @ConditionalOnMissingBean(name = DigestConstants.SAL_DIGEST_PROXY_NAME)
        public BeanNameAutoProxyCreator salDigestLog() {
            log.debug("{} {}", TpConstants.PREFIX, DigestConstants.SAL_DIGEST_PROXY_NAME);
            BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
            beanNameAutoProxyCreator.setBeanNames("*ClientImpl");
            beanNameAutoProxyCreator.setInterceptorNames("salDigestLogInterceptor");
            return beanNameAutoProxyCreator;
        }
    }

    @Configuration
    public static class FilterShowConfiguration {
        @Bean
        @ConditionalOnClass(name = "javax.servlet.Filter")
        FilterConfigShow filterConfigShow() {
            log.debug("{} filterConfigShow", TpConstants.PREFIX);
            return new FilterConfigShow();
        }

        @Bean
        @ConditionalOnClass(name = "org.springframework.web.server.WebFilter")
        WebFilterConfigShow webFilterConfigShow() {
            log.debug("{} webFilterConfigShow", TpConstants.PREFIX);
            return new WebFilterConfigShow();
        }
    }

    @Configuration
    @ConditionalOnClass(ThreadPoolTaskExecutor.class)
    public static class TpThreadPoolConfig {

        public static final String TP_FOR_TASK = "tpForTask";
        public static final String TP_DEFAULT = "tpThreadPool";

        @Primary
        @Bean(name = TP_DEFAULT, destroyMethod = "shutdown")
        @ConditionalOnMissingBean(name = TP_DEFAULT)
        public ThreadPoolTaskExecutor tpThreadPool() {
            log.debug("{} tpThreadPool", TpConstants.PREFIX);
            final int coreSize = getCoreSize();
            final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(coreSize);
            taskExecutor.setMaxPoolSize(coreSize);
            taskExecutor.setQueueCapacity(256);
            taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            return taskExecutor;
        }

        @Bean(name = TP_FOR_TASK, destroyMethod = "shutdown")
        @ConditionalOnMissingBean(name = TP_FOR_TASK)
        public ThreadPoolTaskExecutor tpForTask() {
            log.debug("{} tpForTask", TpConstants.PREFIX);
            final int coreSize = getCoreSize();
            final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(coreSize);
            taskExecutor.setMaxPoolSize(coreSize);
            taskExecutor.setQueueCapacity(128);
            taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
            return taskExecutor;
        }

        private int getCoreSize() {
            return Math.max(Runtime.getRuntime().availableProcessors(), 2);
        }

    }

}
