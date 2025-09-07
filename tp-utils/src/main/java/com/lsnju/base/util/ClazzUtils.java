package com.lsnju.base.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.SystemProperties;

import com.lsnju.base.model.JarInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @version $Id: ClazzUtils.java, v 0.1 2019年7月24日 上午9:20:42 lisong Exp $
 */
@Slf4j
public class ClazzUtils {

    public static String getParentDirectoryFromJar(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return cleanPath(Optional.ofNullable(clazz.getResource("")).map(URL::toString).orElse(""));
    }

    public static String getParentDirectoryFromJar() {
        return getParentDirectoryFromJar(ClazzUtils.class);
    }

    public static String getWorkingDir(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return cleanPath(Optional.ofNullable(clazz.getResource("/")).map(URL::toString).orElse(""));
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
     * @param type class
     * @return getImplementationVersion
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
     * @param type class
     * @return getCodeLocation
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
            String[] ss = splitJarName(jarFullName);
            Objects.requireNonNull(ss);
            info.setJarName(ss[0]);
            info.setJarVersion(ss[1]);
            list.add(info);
        }
        return list;
    }

    private static String jarFullName(String path) {
        String rawJarName = StringUtils.substringAfterLast(path, "/");
        return StringUtils.substringBeforeLast(rawJarName, ".");
//        return StringUtils.substringBefore(StringUtils.substringBefore(rawJarName, ".jar"), ".war");
    }

    public static String[] splitJarName(String fullName) {
        String[] ss = StringUtils.split(fullName, "-");
        int size = ss.length;
        if (size <= 1) {
            return new String[]{fullName, ""};
        }
        String version = ss[size - 1];
        if (Character.isDigit(version.charAt(0))) {
            String jarName = StringUtils.join(ArrayUtils.subarray(ss, 0, size - 1), "-");
            return new String[]{jarName, version};
        }
        if (size == 2) {
            return new String[]{fullName, ""};
        }
        String second = ss[size - 2];
        if (Character.isDigit(second.charAt(0))) {
            String jarName = StringUtils.join(ArrayUtils.subarray(ss, 0, size - 2), "-");
            return new String[]{jarName, second + "-" + version};
        }
        return new String[]{fullName, ""};
    }

    private static String jarPath(URL url) {
        String manifestLocation = url.toString();
        String jarPath = StringUtils.substringBefore(manifestLocation, "!/META-INF/MANIFEST.MF");
        if (Strings.CS.startsWith(jarPath, "jar:file:")) {
            return StringUtils.substringAfter(jarPath, "jar:file:");
        }
        if (Strings.CS.startsWith(jarPath, "jar:nested:")) {
            return StringUtils.substringAfter(jarPath, "jar:nested:");
        }
        if (Strings.CS.startsWith(jarPath, "jar:")) {
            return StringUtils.substringAfter(jarPath, "jar:");
        }
        return jarPath;
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
        if (StringUtils.isBlank(version)) {
            version = mainAttributes.getValue("Bundle-Version");
        }
        return version;
    }

    private static String mfName(Manifest m) {
        Attributes mainAttributes = m.getMainAttributes();
        String name = mainAttributes.getValue("Implementation-Title");
        if (StringUtils.isBlank(name)) {
            name = mainAttributes.getValue("Bundle-SymbolicName");
        }
        if (StringUtils.isBlank(name)) {
            name = mainAttributes.getValue("Bundle-Name");
        }
        if (StringUtils.isBlank(name)) {
            name = mainAttributes.getValue("Specification-Title");
        }
        return name;
    }

    public static List<URL> getJarURLs(ClassLoader cl) {
        final List<URL> all = getURLs(cl);
        return all.stream()
            .filter(Objects::nonNull)
            .filter(x -> Strings.CS.endsWith(x.getPath(), ".jar"))
            .collect(Collectors.toList());
    }

    public static List<URL> getURLs(ClassLoader cl) {
        if (cl == null) {
            return Collections.emptyList();
        }
        if (cl instanceof URLClassLoader) {
            final URL[] urls = ((URLClassLoader) cl).getURLs();
            return Stream.of(urls).collect(Collectors.toList());
        }
        if (cl.equals(ClassLoader.getSystemClassLoader())) {
            return parseJavaClassPath();
        }
        return Collections.emptyList();
    }

    private static List<URL> parseJavaClassPath() {
        List<URL> urls = new ArrayList<>();
        final String separator = SystemProperties.getPathSeparator();
        final String classpath = SystemProperties.getJavaClassPath();
        for (String entry : StringUtils.split(classpath, separator)) {
            try {
                try {
                    urls.add(new File(entry).toURI().toURL());
                } catch (SecurityException e) { // File.toURI checks to see if the file is a directory
                    urls.add(new URL("file", null, new File(entry).getAbsolutePath()));
                }
            } catch (MalformedURLException e) {
                log.warn("malformed classpath entry: " + entry, e);
            }
        }
        return urls;
    }

    public static URL getURL(Class<?> clazz) {
        return Optional.of(clazz)
            .map(Class::getProtectionDomain)
            .map(ProtectionDomain::getCodeSource)
            .map(CodeSource::getLocation)
            .orElse(null);
    }

}
