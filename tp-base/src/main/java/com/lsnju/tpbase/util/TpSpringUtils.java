package com.lsnju.tpbase.util;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/6/21 20:12
 * @version V1.0
 */
@Slf4j
public class TpSpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;
    private static ConfigurableListableBeanFactory beanFactory;

    @SuppressWarnings("NullableProblems")
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("TpSpringUtils postProcessBeanFactory {}", beanFactory);
        TpSpringUtils.beanFactory = beanFactory;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("TpSpringUtils setApplicationContext {}", applicationContext);
        TpSpringUtils.applicationContext = applicationContext;
    }

    public static ListableBeanFactory getBeanFactory() {
        ListableBeanFactory ret = beanFactory == null ? applicationContext : beanFactory;
        Objects.requireNonNull(ret);
        return ret;
    }

    // -------------------

    public static String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    // -------------------

    public static <T> T getBean(Class<T> requiredType) {
        return getBeanFactory().getBean(requiredType);
    }

    public static <T> T getBean(Class<T> clazz, Object... args) {
        return getBeanFactory().getBean(clazz, args);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return getBeanFactory().getBean(name, requiredType);
    }

    // -------------------

    public static Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    public static Object getBean(String name, Object... args) {
        return getBeanFactory().getBean(name, args);
    }

}
