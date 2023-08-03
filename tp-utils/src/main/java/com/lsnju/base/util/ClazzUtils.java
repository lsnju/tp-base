package com.lsnju.base.util;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @version $Id: ClazzUtils.java, v 0.1 2019年7月24日 上午9:20:42 lisong Exp $
 */
@Slf4j
public class ClazzUtils {

    public static String getParentDirectoryFromJar(Class<?> clazz) {
        return cleanPath(clazz.getResource("").toString());
    }

    public static String getParentDirectoryFromJar() {
        return getParentDirectoryFromJar(ClazzUtils.class);
    }

    public static String getWorkingDir(Class<?> clazz) {
        return cleanPath(clazz.getResource("/").toString());
    }

    public static String getWorkingDir() {
        return getWorkingDir(ClazzUtils.class);
    }

    private static String cleanPath(final String dirtyPath) {
        try {
            String jarPath = dirtyPath.replaceAll("^.*file:/", "");
            jarPath = jarPath.replaceAll("jar!.*", "jar");
            jarPath = jarPath.replaceAll("%20", " ");
            if (!jarPath.endsWith(".jar")) {
                jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
            }
            Path parent = Paths.get(jarPath).getParent();
            if (parent == null) {
                return jarPath;
            }
            return parent.toString();
        } catch (Exception e) {
            log.error(String.format("path=%s, error=%s", dirtyPath, e.getMessage()), e);
            return dirtyPath;
        }
    }

    public static boolean exist(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * @param type
     * @return
     */
    public static String getImplementationVersion(Class<?> type) {
        if (type == null) {
            return "na";
        }
        Package aPackage = type.getPackage();
        if (aPackage != null) {
            String v = aPackage.getImplementationVersion();
            if (v == null) {
                return "na";
            } else {
                return v;
            }
        }
        return "na";
    }

    /**
     * @param type
     * @return
     */
    public static String getCodeLocation(Class<?> type) {
        try {
            if (type != null) {
                // file:/C:/java/maven-2.0.8/repo/com/icegreen/greenmail/1.3/greenmail-1.3.jar
                CodeSource codeSource = type.getProtectionDomain().getCodeSource();
                if (codeSource != null) {
                    URL resource = codeSource.getLocation();
                    if (resource != null) {
                        String locationStr = resource.toString();
                        // now lets remove all but the file name
                        String result = getCodeLocation(locationStr, '/');
                        if (result != null) {
                            return result;
                        }
                        return getCodeLocation(locationStr, '\\');
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return "na";
    }

    private static String getCodeLocation(String locationStr, char separator) {
        int idx = locationStr.lastIndexOf(separator);
        if (isFolder(idx, locationStr)) {
            idx = locationStr.lastIndexOf(separator, idx - 1);
            return locationStr.substring(idx + 1);
        } else if (idx > 0) {
            return locationStr.substring(idx + 1);
        }
        return null;
    }

    private static boolean isFolder(int idx, String text) {
        return (idx != -1 && idx + 1 == text.length());
    }
}
