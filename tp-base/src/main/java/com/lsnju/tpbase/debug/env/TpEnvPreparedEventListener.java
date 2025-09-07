package com.lsnju.tpbase.debug.env;

import org.slf4j.Logger;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.ApplicationListener;

import com.lsnju.base.util.TpAppInfo;

/**
 *
 * @author lisong
 * @since 2021/11/17 13:47
 * @version V1.0
 */
public class TpEnvPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private final Logger log;

    public TpEnvPreparedEventListener(Logger logger) {
        this.log = logger;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        log();
    }

    private void log() {
        log.warn("java.version     = {} ({})({})({})", JavaVersion.getJavaVersion(), TpAppInfo.JAVA_VERSION, TpAppInfo.JAVA_VERSION_DATE, TpAppInfo.JAVA_VENDOR);
        log.warn("spring-boot.ver  = {}", SpringBootVersion.getVersion());
        log.warn("tp.version       = {}", TpAppInfo.TP_BASE_VERSION);
        log.warn("build.version    = {}", TpAppInfo.BUILD_VERSION);
        log.warn("build.time       = {}", TpAppInfo.BUILD_TIME);
        log.warn("app.home         = {}", new ApplicationHome().getDir());
    }
}
