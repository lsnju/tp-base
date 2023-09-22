package com.lsnju.tpbase.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.util.TpDateFormatUtils;


/**
 *
 * @author ls
 * @since 2023-07-26 21:20:06
 * @version V1.0
 */
public class VersionConfig {

    private static final String WEB_VERSION = "" + System.currentTimeMillis();

    private static final String DATE_STR = TpDateFormatUtils.getNewFormatDateString(new Date());

    private static final String _hostname = getHostname0();

    private static final String _pid = getPid0();

    private static String getPid0() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            return jvmName.split("@")[0];
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    private static String getHostname0() {
        Process p = null;
        try {
            p = new ProcessBuilder("uname", "-n").start();
            return StringUtils.trim(IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8));
        } catch (Throwable e) {
            return getHostname1();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    private static String getHostname1() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return getHostname2();
        }
    }

    private static String getHostname2() {
        return StringUtils.EMPTY;
    }

    public static String getVer() {
        return WEB_VERSION;
    }

    public static String getStartDate() {
        return DATE_STR;
    }

    public static String getHostname() {
        return _hostname;
    }

    public static String getPid() {
        return _pid;
    }

    public static String getEnv(String key) {
        return System.getenv(key);
    }
}
