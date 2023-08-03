package com.lsnju.tpbase.web.controller.monitor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsnju.base.model.BaseMo;
import com.lsnju.base.util.TpAppInfo;
import com.lsnju.base.util.TpDateFormatUtils;
import com.lsnju.tpbase.config.prop.TpLogConfigProperties;
import com.lsnju.tpbase.util.VersionConfig;
import com.lsnju.tpbase.web.mvc.TpSpringWebMvcHelper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2021/11/3 8:59
 * @version V1.0
 */
@Slf4j
@Setter
@RestController
public class TpSysInfoController {

    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${server.port}")
    private int serverPort;
    @Autowired
    private TpLogConfigProperties tpLogConfigProperties;
    @Autowired(required = false)
    private TpSpringWebMvcHelper tpSpringWebMvcHelper;

    private static final ApplicationHome HOME = new ApplicationHome(TpSysInfoController.class);

    @GetMapping(path = "${tp.sys.mo.base-path}/sysinfo.json")
    public SysInfo show() {
        log.debug("x");
        final SysInfo ret = new SysInfo();
        ret.setHostname(VersionConfig.getHostname());
        ret.setPid(VersionConfig.getPid());
        ret.setPort(serverPort);
        ret.setJavaVersion(TpAppInfo.JAVA_VERSION);
        ret.setJavaVersionDate(TpAppInfo.JAVA_VERSION_DATE);
        ret.setJavaVendor(TpAppInfo.JAVA_VENDOR);
        ret.setVersion(TpAppInfo.BUILD_VERSION);
        ret.setBuildTime(TpAppInfo.BUILD_TIME);
        ret.setStartTime(VersionConfig.getStartDate());
        ret.setNow(TpDateFormatUtils.getNewFormatDateString(new Date()));
        ret.setProfile(profile);
        ret.setLogger(tpLogConfigProperties);
        ret.setTpVersion(TpAppInfo.TP_BASE_VERSION);
        ret.setSbVersion(SpringBootVersion.getVersion());
        ret.setHomeDir(String.valueOf(HOME.getDir()));
        ret.setHomeSrc(String.valueOf(HOME.getSource()));
        if (tpSpringWebMvcHelper != null) {
            ret.setServerUrl(tpSpringWebMvcHelper.getServerUrl());
        }
        return ret;
    }

    @Getter
    @Setter
    static class SysInfo extends BaseMo {
        private String hostname;
        private String pid;
        private int port;
        private String javaVersion;
        private String javaVersionDate;
        private String javaVendor;
        private String version;
        private String serverUrl;
        private String buildTime;
        private String startTime;
        private String now;
        private String profile;
        private String tpVersion;
        private String sbVersion;
        private String homeDir;
        private String homeSrc;
        private TpLogConfigProperties logger;
    }

}
