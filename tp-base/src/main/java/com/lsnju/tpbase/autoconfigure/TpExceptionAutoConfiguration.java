package com.lsnju.tpbase.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author ls
 * @since 2022/8/24 21:19
 * @version V1.0
 */
@Configuration
@Import({
    TpExceptionHandler.SpringWebExceptionConfiguration.class,
    TpExceptionHandler.SpringDaoExceptionConfiguration.class,
    TpExceptionHandler.SpringValidationExceptionConfiguration.class,
    TpExceptionHandler.TpDefaultExceptionConfiguration.class,
})
public class TpExceptionAutoConfiguration {
}
