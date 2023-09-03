package com.lsnju.tpbase.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.lsnju.tpbase.TpConstants;
import com.lsnju.tpbase.config.FilterOrderConstants;
import com.lsnju.tpbase.web.filter.TpPagePerfFilter;
import com.lsnju.tpbase.web.filter.TpRequestFilter;
import com.lsnju.tpbase.web.filter.TpRequestHeaderFilter;
import com.lsnju.tpbase.web.filter.TpSessionFilter;
import com.lsnju.tpbase.web.filter.TpUidLogFilter;
import com.lsnju.tpbase.web.filter.profiler.RestProfilerFilter;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2022/8/22 19:46
 * @version V1.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TpServletFilterConfiguration {


    @Configuration
    @ConditionalOnClass(value = {Filter.class, FilterRegistrationBean.class})
    public static class TpBaseFilterConfig implements FilterOrderConstants {

        @Bean
        public FilterRegistrationBean<MDCInsertingServletFilter> logFilter() {
            log.debug("{} logFilter", TpConstants.PREFIX);
            FilterRegistrationBean<MDCInsertingServletFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new MDCInsertingServletFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + LOG_ORDER);
            return registrationBean;
        }

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
        protected TpRequestFilter newBaseRequestFilter() {
            log.debug("{} newBaseRequestFilter", TpConstants.PREFIX);
            return new TpRequestFilter();
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
    }

}
