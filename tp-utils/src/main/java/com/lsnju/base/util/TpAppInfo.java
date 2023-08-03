package com.lsnju.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2021/11/3 9:12
 * @version V1.0
 */
@Slf4j
@Getter
@Builder
public class TpAppInfo {

    private static final String TP_BASE_PROPERTIES = "/com/lsnju/base/tp-base.properties";
    private static final String BUILD_PROPERTIES = "/META-INF/build-info.properties";
    private static final TpAppInfo APP_INFO = getAppInfo();

    public static final String JAVA_VERSION = javaVersion();
    public static final String JAVA_VERSION_DATE = javaVersionDate();
    public static final String JAVA_VENDOR = javaVendor();
    public static final String TP_BASE_VERSION = getTpBaseVersion();
    public static final String BUILD_VERSION = APP_INFO.getBuildVersion();
    public static final String BUILD_TIME = APP_INFO.getBuildTime();

    private static String javaVersion() {
        return System.getProperty("java.version");
    }

    private static String javaVersionDate() {
        return StringUtils.defaultString(System.getProperty("java.version.date"), "-");
    }

    private static String javaVendor() {
        return StringUtils.defaultString(System.getProperty("java.vendor"), "-");
    }

    private static TpAppInfo getAppInfo() {
        final TpAppInfoBuilder builder = TpAppInfo.builder();
        try (InputStream inputStream = TpAppInfo.class.getResourceAsStream(BUILD_PROPERTIES)) {
            if (inputStream != null) {
                final Properties properties = new Properties();
                properties.load(inputStream);
                log.debug("build-info.properties = {}", properties);
                builder.buildTime(properties.getProperty("build.time"));
                builder.buildVersion(properties.getProperty("build.version"));
            } else {
                log.info("{} not exist", BUILD_PROPERTIES);
            }
        } catch (IOException ignore) {
            // ignore
        }
        return builder.build();
    }

    private static String getTpBaseVersion() {
        String version = "unknown-version";
        try (InputStream inputStream = TpAppInfo.class.getResourceAsStream(TP_BASE_PROPERTIES)) {
            if (inputStream != null) {
                final Properties properties = new Properties();
                properties.load(inputStream);
                log.debug("tp-base.properties = {}", properties);
                version = properties.getProperty("tp-base.version");
            }
        } catch (IOException ignore) {
            // ignore
        }
        return version;
    }

    private final String buildTime;
    private final String buildVersion;

}
