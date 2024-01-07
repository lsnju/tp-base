package com.lsnju.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.model.JarInfo;

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


    public static List<JarInfo> allJarInfo() throws IOException {
        List<JarInfo> list = new ArrayList<>();
        Enumeration<URL> resources = ClazzUtils.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            JarInfo info = fromMF(url);
            String jarPath = jarPath(url);
            String jarFullName = jarFullName(jarPath);
            info.setPath(jarPath);
            info.setJarFullName(jarFullName);
            info.setJarName(jarName(jarFullName));
            info.setJarVersion(jarVersion(jarFullName));
            list.add(info);
        }
        return list;
    }

    private static String jarFullName(String path) {
        String rawJarName = StringUtils.substringAfterLast(path, "/");
        return StringUtils.substringBeforeLast(rawJarName, ".");
//        return StringUtils.substringBefore(StringUtils.substringBefore(rawJarName, ".jar"), ".war");
    }

    private static String jarName(String jarFullName) {
        return StringUtils.substringBeforeLast(jarFullName, "-");
    }

    private static String jarVersion(String jarFullName) {
        return StringUtils.substringAfterLast(jarFullName, "-");
    }

    private static String jarPath(URL url) {
        String manifestLocation = url.toString();
        return StringUtils.substringAfter(StringUtils.substringBefore(manifestLocation, "!/META-INF/MANIFEST.MF"), "jar:file:");
    }

    public static JarInfo fromMF(URL jarManifest) throws IOException {
        try (InputStream is = jarManifest.openStream()) {
            Manifest m = new Manifest(is);
            final JarInfo info = new JarInfo();
            info.setMfName(mfName(m));
            info.setMfVersion(mfVersion(m));
            return info;
        }
    }

    private static String mfVersion(Manifest m) {
        Attributes mainAttributes = m.getMainAttributes();
        String version = mainAttributes.getValue("Implementation-Version");
        if (version == null) {
            version = mainAttributes.getValue("Bundle-Version");
        }
        return version;
    }

    private static String mfName(Manifest m) {
        Attributes mainAttributes = m.getMainAttributes();
        String name = mainAttributes.getValue("Implementation-Title");
        if (name == null) {
            name = mainAttributes.getValue("Bundle-SymbolicName");
        }
        if (name == null) {
            name = mainAttributes.getValue("Bundle-Name");
        }
        return name;
    }

}
