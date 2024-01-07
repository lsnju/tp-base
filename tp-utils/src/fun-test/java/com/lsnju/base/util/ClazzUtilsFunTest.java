package com.lsnju.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import com.fasterxml.classmate.TypeResolver;
import com.lsnju.base.model.JarInfo;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023/7/20 20:18
 * @version V1.0
 */
@Slf4j
public class ClazzUtilsFunTest {

    @Test
    public void test_001() {
        log.info("{}", ClazzUtils.getCodeLocation(Test.class));
        log.info("{}", ClazzUtils.getImplementationVersion(Test.class));
        log.info("{}", ClazzUtils.getImplementationVersion(Test.class));

        log.info("{}", ClazzUtils.getParentDirectoryFromJar(ClazzUtilsFunTest.class));
        log.info("{}", ClazzUtils.getParentDirectoryFromJar());
        log.info("{}", ClazzUtils.getParentDirectoryFromJar(Test.class));

        log.info("{}", ClazzUtils.getWorkingDir(ClazzUtilsFunTest.class));
        log.info("{}", ClazzUtils.getWorkingDir());
        log.info("{}", ClazzUtils.getWorkingDir(Test.class));
    }

    @Test
    void test_003() throws IOException {
        List<JarInfo> list = ClazzUtils.allJarInfo();
        for (JarInfo info : list) {
            log.info("{}", info);
        }
    }

    @Test
    void test_004() {
        URL resource = TypeResolver.class.getResource("/META-INF/MANIFEST.MF");
        log.info("{}", resource);

        CodeSource codeSource = TypeResolver.class.getProtectionDomain().getCodeSource();
        URL rr = codeSource.getLocation();
        log.info("{}", rr);
    }

    @Test
    void test_005() {
        try {
            Enumeration<URL> resources = ClazzUtils.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                log.info("----------------------");
                log.info("{}", url);
                try (InputStream is = url.openStream()) {
                    Manifest m = new Manifest(is);
                    log.info("{}", m);
                    Attributes mainAttributes = m.getMainAttributes();
                    log.info("mainAttributes.size = {}", mainAttributes.size());
                    log.info("getEntries.value.size = {}", m.getEntries().values().size());
                    mainAttributes.forEach((k, v) -> {
                        log.info("{} = {}", k, v);
                    });
                }
            }
        } catch (IOException e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_006() {
        try {
            Enumeration<URL> resources = ClazzUtils.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                log.info("----------------------");
                log.info("{}", url);
                log.info("{}", ClazzUtils.fromMF(url));
            }
        } catch (IOException e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_007() {
        log.info("{}", Lists.newArrayList(ClazzUtils.splitJarName("")));
        log.info("{}", Lists.newArrayList(ClazzUtils.splitJarName("jce")));
        log.info("{}", Lists.newArrayList(ClazzUtils.splitJarName("jce-1.1")));
        log.info("{}", Lists.newArrayList(ClazzUtils.splitJarName("json-lib-2.2.2-jdk15")));
    }

}
