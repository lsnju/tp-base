package com.lsnju.tpbase.debug.env;

import org.slf4j.Logger;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.ApplicationListener;

import com.lsnju.base.util.TpAppInfo;
import com.lsnju.base.util.TpDateFormatUtils;

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
        log.error("java.version     = {} ({})({})({})", JavaVersion.getJavaVersion(), TpAppInfo.JAVA_VERSION, TpAppInfo.JAVA_VERSION_DATE, TpAppInfo.JAVA_VENDOR);
        log.error("spring-boot.ver  = {}", SpringBootVersion.getVersion());
        log.error("tp.version       = {}", TpAppInfo.TP_BASE_VERSION);
        log.error("build.version    = {}", TpAppInfo.BUILD_VERSION);
        log.error("build.time       = {}", TpAppInfo.BUILD_TIME);
        log.error("build.date       = {}", TpDateFormatUtils.getNewFormatDateString(TpAppInfo.BUILD_DATE));
        log.error("app.home         = {}", new ApplicationHome().getDir());
    }
}
