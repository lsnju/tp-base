package com.lsnju.tpbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lsnju.tpbase.web.controller.TpHomeController;
import com.lsnju.tpbase.web.controller.monitor.TpCpInfoController;
import com.lsnju.tpbase.web.controller.monitor.TpSysInfoController;
import com.lsnju.tpbase.web.controller.monitor.TpThreadPoolController;

/**
 *
 * @author ls
 * @since 2023-08-03 12:50:47
 * @version V1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableTpTask
@Import({TpSysInfoController.class,
    TpThreadPoolController.class,
    TpCpInfoController.class,
    TpHomeController.class})
public @interface EnableTpMoConfig {
}
