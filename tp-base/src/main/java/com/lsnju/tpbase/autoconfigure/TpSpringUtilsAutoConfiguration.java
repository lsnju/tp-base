package com.lsnju.tpbase.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsnju.tpbase.util.TpSpringUtils;

/**
 *
 * @author lis614
 * @since 2025/6/21 20:35
 * @version V1.0
 */
@Configuration
public class TpSpringUtilsAutoConfiguration {

    @Bean
    TpSpringUtils tpSpringUtils() {
        return new TpSpringUtils();
    }

}
