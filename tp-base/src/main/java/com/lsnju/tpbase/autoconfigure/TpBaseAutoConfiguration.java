package com.lsnju.tpbase.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

import com.lsnju.tpbase.TpConstants;
import com.lsnju.tpbase.config.prop.TpLogConfigProperties;
import com.lsnju.tpbase.config.prop.TpMoConfigProperties;
import com.lsnju.tpbase.config.prop.TpRestApiProfilerProperties;
import com.lsnju.tpbase.debug.env.EnvShowConfig;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/8/11 8:16
 * @version V1.0
 */
@Slf4j
@EnableConfigurationProperties({
    TpLogConfigProperties.class,
    TpMoConfigProperties.class,
    TpRestApiProfilerProperties.class,
})
@Import({
    TpBaseConfiguration.TpSpringWebMvcConfig.class,
    TpBaseConfiguration.TpSpringFoxConfig.class,
    TpBaseConfiguration.TpJacksonCustomConfig.class,
    TpBaseConfiguration.FilterShowConfiguration.class,
    TpBaseConfiguration.TpThreadPoolConfig.class,
})
@Configuration
public class TpBaseAutoConfiguration {

    @Bean
    EnvShowConfig tpEnvShowConfig(@Autowired ConfigurableEnvironment env) {
        log.debug("{} tpEnvShowConfig", TpConstants.PREFIX);
        return new EnvShowConfig(env);
    }

}
