package com.lsnju.base.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.joor.Reflect;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.lsnju.base.model.JarInfo;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2024/8/10 16:27
 * @version V1.0
 */
@Slf4j
public class GuavaFunTest {

    // https://www.baeldung.com/jvm-list-all-classes-loaded

    @Test
    void test_002() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        do {
            final List<URL> urls = ClazzUtils.getURLs(classLoader);
            log.info("-----------------------------------------------------");
            log.info("{}, url.size={}", classLoader, urls.size());
            log.info("-----------------------------------------------------");
            showAllUrl(urls);

            classLoader = classLoader.getParent();
        } while (classLoader != null);
    }

    private void showAllUrl(List<URL> urls) {
        final Set<String> set = urls.stream().map(URL::getPath).collect(Collectors.toSet());
        log.info("total={}, unique={}", urls.size(), set.size());
        for (String url : new TreeSet<>(set)) {
            log.info("{}", url);
        }
    }

    @Test
    void test_show_diff() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final List<URL> urls = ClazzUtils.getURLs(classLoader);
            log.info("-----------------------------------------------------");
            log.info("{}, url.size={}", classLoader, urls.size());
            log.info("-----------------------------------------------------");

            final List<JarInfo> jarInfos = ClazzUtils.allJarInfo();
            log.info("{}", jarInfos.size());

            final Set<String> set1 = urls.stream().map(URL::getPath).collect(Collectors.toSet());
            final Set<String> set2 = jarInfos.stream().map(JarInfo::getPath).collect(Collectors.toSet());

            Set<String> diff1 = Sets.newHashSet(set1);
            Set<String> diff2 = Sets.newHashSet(set2);
            diff1.removeAll(set2);
            diff2.removeAll(set1);
            log.info("diff1 = {}", diff1.size());
            log.info("diff2 = {}", diff2.size());
            log.info("-------------------------");
            for (String s : new TreeSet<>(diff1)) {
                log.info("{}", s);
            }
            log.info("-------------------------");
            for (String s : new TreeSet<>(diff2)) {
                log.info("{}", s);
            }
            log.info("-------------------------");
            log.info("set1={}, set2={}", set1.size(), set2.size());
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }

    }

    @Test
    void test_show_top_level_class() {
        try {
            ClassPath classPath = ClassPath.from(GuavaFunTest.class.getClassLoader());
            Set<ClassPath.ClassInfo> allClasses = classPath.getAllClasses();

            log.info("allClasses = {}", allClasses.size());

            final ImmutableSet<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClasses();
            log.info("topLevelClasses.size = {}", topLevelClasses.size());

            final ImmutableSet<ClassPath.ResourceInfo> resources = classPath.getResources();
            log.info("resources.size = {}", resources.size());

            Map<ClassLoader, Integer> map = new LinkedHashMap<>();
            for (ClassPath.ResourceInfo item : resources) {
                ClassLoader cl = Reflect.on(item).field("loader").get();
                final Integer value = map.getOrDefault(cl, 0);
                map.put(cl, value + 1);
            }

            log.info("map.size = {}", map.size());
            map.forEach((k, v) -> {
                log.info("{} = {}", k, v);
            });

        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void show_class_list_of_cl() {
        final ClassLoader classLoader = GuavaFunTest.class.getClassLoader();
        show_classes_of_cl(classLoader);
        show_classes_of_cl(classLoader.getParent());
        show_classes_of_cl(classLoader.getParent().getParent());
    }

    private void show_classes_of_cl(ClassLoader classLoader) {
        log.info("-------------------------------------------");
        log.info("{}", classLoader);
        log.info("-------------------------------------------");
        if (classLoader == null) {
            return;
        }
        try {
            final Field field = ClassLoader.class.getDeclaredField("classes");
            log.info("{}", field);
            final Object value = field.get(classLoader);
            log.info("{}", value);
            if (value instanceof List<?> list) {
                for (Object item : list) {
                    log.info("{}", item);
                }
            }
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_show_getJarURLs() {
        final List<URL> jarURLs = ClazzUtils.getJarURLs(Thread.currentThread().getContextClassLoader());
        log.info("{}", jarURLs.size());
        final List<String> list = jarURLs.stream().map(URL::getPath).sorted().collect(Collectors.toList());
        log.info("{}", list.size());
        for (String s : list) {
            log.info("{}", s);
        }
    }

    @Test
    void test_guava_classpath() {
        try {
            ClassPath classPath = ClassPath.from(GuavaFunTest.class.getClassLoader());
            Set<ClassPath.ClassInfo> classes = classPath.getAllClasses();
            log.info("{}", classes.size());
            final Set<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClasses();
            log.info("{}", topLevelClasses.size());

            Set<ClassPath.ClassInfo> left = new LinkedHashSet<>();
            for (ClassPath.ClassInfo item : classes) {
                if (!topLevelClasses.contains(item)) {
                    left.add(item);
                }
            }

            log.info("left = {}", left.size());
//            for (ClassPath.ClassInfo classInfo : left) {
//                log.info("{}", classInfo.getName());
//            }
        } catch (IOException e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

}
