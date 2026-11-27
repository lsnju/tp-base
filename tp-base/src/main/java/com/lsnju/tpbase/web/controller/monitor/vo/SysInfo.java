package com.lsnju.tpbase.web.controller.monitor.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lsnju.base.model.BaseMo;
import com.lsnju.tpbase.config.prop.TpLogConfigProperties;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lis614
 * @since 2025/6/21 22:26
 * @version V1.0
 */
@Getter
@Setter
public class SysInfo extends BaseMo {

    private MemInfo jvmInfo;
    private JdkInfo jdkInfo;
    private AppInfo appInfo;
    private AppRuntime appRuntime;
    private TpLogConfigProperties logger;

    @Getter
    @Setter
    public static class MemInfo extends BaseMo {

        private long totalMemory;
        private long maxMemory;
        private long freeMemory;

    }

    @Getter
    @Setter
    public static class JdkInfo extends BaseMo {

        private String javaVersion;
        private String javaVersionDate;
        private String javaVendor;

    }

    @Getter
    @Setter
    public static class AppInfo extends BaseMo {

        private String version;
        private String buildTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date buildDate;
        private String tpVersion;
        private String sbVersion;

    }

    @Getter
    @Setter
    public static class AppRuntime extends BaseMo {

        private String hostname;
        private String pid;
        private int port;
        private int coreSize;
        private String serverUrl;
        private String startTime;
        private String now;
        private String profile;
        private String homeDir;
        private String homeSrc;

    }

}
