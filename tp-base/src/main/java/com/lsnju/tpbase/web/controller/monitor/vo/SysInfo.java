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
    private String hostname;
    private String pid;
    private int port;
    private int coreSize;
    private long totalMemory;
    private long maxMemory;
    private long freeMemory;
    private String javaVersion;
    private String javaVersionDate;
    private String javaVendor;
    private String version;
    private String serverUrl;
    private String buildTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date buildDate;
    private String startTime;
    private String now;
    private String profile;
    private String tpVersion;
    private String sbVersion;
    private String homeDir;
    private String homeSrc;
    private TpLogConfigProperties logger;
}
