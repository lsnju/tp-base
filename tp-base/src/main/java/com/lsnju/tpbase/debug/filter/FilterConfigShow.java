package com.lsnju.tpbase.debug.filter;

import java.util.List;

import jakarta.servlet.Filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.servlet.filter.OrderedFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.GenericFilterBean;

import com.lsnju.tpbase.util.OrderUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2021/5/28 13:14
 * @version V1.0
 */
@Slf4j
@Setter
public class FilterConfigShow extends AbstractFilterConfigShow implements InitializingBean {

    private final List<Filter> filters;
    private final List<FilterRegistrationBean<?>> filterRegistrationBeans;
    private final List<OrderedFilter> orderedFilters;
    private final List<GenericFilterBean> genericFilterBeans;

    public FilterConfigShow(List<Filter> filters,
                            List<FilterRegistrationBean<?>> filterRegistrationBeans,
                            List<OrderedFilter> orderedFilters,
                            List<GenericFilterBean> genericFilterBeans) {
        this.filters = filters;
        this.filterRegistrationBeans = filterRegistrationBeans;
        this.orderedFilters = orderedFilters;
        this.genericFilterBeans = genericFilterBeans;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toPrettyString2(filterRegistrationBeans));
        log.debug(toPrettyString("filters", filters));
        log.debug(toPrettyString("orderedFilters", orderedFilters));
        log.debug(toPrettyString("genericFilterBeans", genericFilterBeans));
        log.debug(SEP);
    }

    private static String toPrettyString2(List<FilterRegistrationBean<?>> list) {
        StringBuilder sb = new StringBuilder();
        String tag = "filterRegistrationBeans";
        sb.append(SEP).append(System.lineSeparator());
        sb.append(TAG_1).append(tag).append(TAG_2).append(list.size()).append(System.lineSeparator());
        for (FilterRegistrationBean<?> item : list) {
            sb.append(String.format(TEMP + ", %s", OrderUtils.getOrder(item), item, item.getFilter())).append(System.lineSeparator());
        }
        return sb.toString();
    }


}
