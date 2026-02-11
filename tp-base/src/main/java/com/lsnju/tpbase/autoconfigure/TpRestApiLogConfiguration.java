package com.lsnju.tpbase.autoconfigure;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.lsnju.tpbase.config.prop.TpAopConfigProperties;
import com.lsnju.tpbase.log.rest.TpRestApiLogInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lsnju
 * @since 2025-09-29 18:05:17
 * @version V1.0
 * @since 3.1.19
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(name = "tp.aop.enable-rest-log", matchIfMissing = true)
@Import(TpRestApiLogConfiguration.TpRestApiLogInterceptorConfig.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TpRestApiLogConfiguration {

    private TpRestApiLogInterceptor tpRestApiLogInterceptor;

    @Autowired
    public void setTpRestApiLogInterceptor(TpRestApiLogInterceptor tpRestApiLogInterceptor) {
        this.tpRestApiLogInterceptor = tpRestApiLogInterceptor;
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && execution(public * *(..))")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return tpRestApiLogInterceptor.proceed(joinPoint);
    }

    @Configuration
    public static class TpRestApiLogInterceptorConfig {

        @Bean
        @ConditionalOnMissingBean
        static TpRestApiLogInterceptor tpRestApiLogInterceptor(ObjectProvider<TpAopConfigProperties> config) {
            return new TpRestApiLogInterceptor(config.getIfAvailable(() -> {
                TpAopConfigProperties ret = new TpAopConfigProperties();
                ret.setUseSpring(false);
                ret.setEnableRestLog(true);
                ret.setEnableRestLogReq(true);
                ret.setEnableRestLogResp(true);
                return ret;
            }));
        }

    }

}
