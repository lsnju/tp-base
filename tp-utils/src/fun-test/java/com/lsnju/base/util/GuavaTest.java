package com.lsnju.base.util;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class GuavaTest {

    // https://www.baeldung.com/jvm-list-all-classes-loaded

    @Test
    void test_002() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        do {
            final URL[] urls = ClazzUtils.getURLs(classLoader);
            log.info("-----------------------------------------------------");
            log.info("{}, url.size={}", classLoader, urls.length);
            log.info("-----------------------------------------------------");
            showAllUrl(urls);

            classLoader = classLoader.getParent();
        } while (classLoader != null);
    }

    private void showAllUrl(URL[] urls) {
        final Set<String> set = Stream.of(urls).map(URL::getPath).collect(Collectors.toSet());
        log.info("total={}, unique={}", urls.length, set.size());
        for (String url : new TreeSet<>(set)) {
            log.info("{}", url);
        }
    }

    @Test
    void test_show_diff() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final URL[] urls = ClazzUtils.getURLs(classLoader);
            log.info("-----------------------------------------------------");
            log.info("{}, url.size={}", classLoader, urls.length);
            log.info("-----------------------------------------------------");
            final Set<String> set1 = Stream.of(urls).map(URL::getPath).collect(Collectors.toSet());

            final List<JarInfo> jarInfos = ClazzUtils.allJarInfo();
            log.info("{}", jarInfos.size());
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
            ClassPath classPath = ClassPath.from(GuavaTest.class.getClassLoader());
            Set<ClassPath.ClassInfo> classes = classPath.getAllClasses();

            log.info("total = {}", classes.size());

            final ImmutableSet<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClasses();
            log.info("topLevelClasses.size = {}", topLevelClasses.size());

        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void show_class_list_of_cl() {
        final ClassLoader classLoader = GuavaTest.class.getClassLoader();
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
        Vector<Class<?>> classes = Reflect.on(classLoader).field("classes").get();
        log.info("{}", classes.size());
        for (Class<?> clazz : classes) {
            log.debug("{}", clazz);
        }
    }

    @Test
    void test_show_loaded_class_location() {
        final ClassLoader classLoader = GuavaTest.class.getClassLoader();
        Vector<Class<?>> classes = Reflect.on(classLoader).field("classes").get();
        Set<URL> set = classes.stream().map(ClazzUtils::getURL).filter(Objects::nonNull).collect(Collectors.toSet());
        set.stream().map(URL::getPath).sorted().forEach(log::info);
    }

    @Test
    void test_show_getJarURLs() {
        final Set<URL> jarURLs = ClazzUtils.getJarURLs(Thread.currentThread().getContextClassLoader());
        final List<String> list = jarURLs.stream().map(URL::getPath).sorted().collect(Collectors.toList());
        log.info("{}", list.size());
        for (String s : list) {
            log.info("{}", s);
        }
    }
}
