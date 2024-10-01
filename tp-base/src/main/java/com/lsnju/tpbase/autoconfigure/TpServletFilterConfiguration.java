package com.lsnju.tpbase.autoconfigure;

import jakarta.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.lsnju.tpbase.TpConstants;
import com.lsnju.tpbase.config.FilterOrderConstants;
import com.lsnju.tpbase.config.prop.TpFilterConfigProperties;
import com.lsnju.tpbase.config.prop.TpRestApiProfilerProperties;
import com.lsnju.tpbase.web.filter.TpMDCInsertingServletFilter;
import com.lsnju.tpbase.web.filter.TpPagePerfFilter;
import com.lsnju.tpbase.web.filter.TpRequestFilter;
import com.lsnju.tpbase.web.filter.TpRequestHeaderFilter;
import com.lsnju.tpbase.web.filter.TpSessionFilter;
import com.lsnju.tpbase.web.filter.TpUidLogFilter;
import com.lsnju.tpbase.web.filter.profiler.RestProfilerFilter;
import com.lsnju.tpbase.web.filter.rest.TpRestApiDigestFilter;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2022/8/22 19:46
 * @version V1.0
 */
@Slf4j
public class TpServletFilterConfiguration {

    @Configuration
    @ConditionalOnClass(value = {Filter.class, FilterRegistrationBean.class, MDCInsertingServletFilter.class})
    public static class TpBaseLogbackFilterConfig implements FilterOrderConstants {
        @Bean
        public FilterRegistrationBean<MDCInsertingServletFilter> logFilter() {
            log.debug("{} logFilter", TpConstants.PREFIX);
            FilterRegistrationBean<MDCInsertingServletFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new MDCInsertingServletFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + LOG_ORDER);
            return registrationBean;
        }
    }

    @Configuration
    @ConditionalOnClass(value = {Filter.class, FilterRegistrationBean.class})
    @ConditionalOnMissingClass("ch.qos.logback.classic.helpers.MDCInsertingServletFilter")
    public static class TpBaseLog4j2FilterConfig implements FilterOrderConstants {
        @Bean
        public FilterRegistrationBean<TpMDCInsertingServletFilter> logFilter() {
            log.debug("{} logFilter", TpConstants.PREFIX);
            FilterRegistrationBean<TpMDCInsertingServletFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new TpMDCInsertingServletFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + LOG_ORDER);
            return registrationBean;
        }
    }

    @Configuration
    @EnableConfigurationProperties(TpFilterConfigProperties.class)
    @ConditionalOnClass(value = {Filter.class, FilterRegistrationBean.class})
    public static class TpBaseFilterConfig implements FilterOrderConstants {

        @Bean
        public FilterRegistrationBean<TpRequestFilter> requestFilter(TpRequestFilter newBaseRequestFilter) {
            log.debug("{} requestFilter", TpConstants.PREFIX);
            FilterRegistrationBean<TpRequestFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(newBaseRequestFilter);
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + REQ_ORDER);
            return registrationBean;
        }

        @Bean
        @ConditionalOnMissingBean(TpRequestFilter.class)
        public TpRequestFilter newBaseRequestFilter(TpFilterConfigProperties tpFilterConfigProperties) {
            log.debug("{} newBaseRequestFilter", TpConstants.PREFIX);
            return new TpRequestFilter(tpFilterConfigProperties);
        }

        @Bean
        public FilterRegistrationBean<TpPagePerfFilter> pagePerfFilter() {
            log.debug("{} pagePerfFilter", TpConstants.PREFIX);
            FilterRegistrationBean<TpPagePerfFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new TpPagePerfFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(PERF_ORDER);
            return registrationBean;
        }

        @Bean
        public FilterRegistrationBean<TpRequestHeaderFilter> requestHeaderFilter() {
            log.debug("{} requestHeaderFilter", TpConstants.PREFIX);
            FilterRegistrationBean<TpRequestHeaderFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new TpRequestHeaderFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(REQUEST_HEADER_ORDER);
            return registrationBean;
        }

        @Bean
        public FilterRegistrationBean<TpUidLogFilter> userIdLogFilter() {
            log.debug("{} userIdLogFilter", TpConstants.PREFIX);
            FilterRegistrationBean<TpUidLogFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new TpUidLogFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(UID_ORDER);
            return registrationBean;
        }
    }

    @Configuration
    @ConditionalOnClass(value = {Filter.class, FilterRegistrationBean.class})
    public static class TpFilterConfig implements FilterOrderConstants {

        @Bean
        public FilterRegistrationBean<TpSessionFilter> sessionFilter() {
            log.debug("{} sessionFilter", TpConstants.PREFIX);
            FilterRegistrationBean<TpSessionFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new TpSessionFilter());
            registrationBean.addUrlPatterns("*.htm", "*.json");
            registrationBean.setOrder(SESSION_ORDER);
            return registrationBean;
        }
    }

    @Configuration
    @EnableConfigurationProperties(TpRestApiProfilerProperties.class)
    @ConditionalOnClass(value = {Filter.class, FilterRegistrationBean.class})
    public static class ProfilerFilterConfig implements FilterOrderConstants {

        @Bean
        public FilterRegistrationBean<RestProfilerFilter> restProfilerFilter() {
            log.debug("{} restProfilerFilter", TpConstants.PREFIX);
            FilterRegistrationBean<RestProfilerFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(newRestProfilerFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(PROFILER_ORDER);
            return registrationBean;
        }

        @Bean
        public RestProfilerFilter newRestProfilerFilter() {
            log.debug("{} newRestProfilerFilter", TpConstants.PREFIX);
            return new RestProfilerFilter();
        }

        @Bean
        @ConditionalOnMissingBean(TpRestApiDigestFilter.class)
        public TpRestApiDigestFilter tpRestApiDigestFilter() {
            log.debug("{} tpRestApiDigestFilter", TpConstants.PREFIX);
            return new TpRestApiDigestFilter();
        }

    }

}
