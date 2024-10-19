package com.lsnju.tpbase.debug.filter;

import java.util.Collections;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
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
public class FilterConfigShow extends AbstractFilterConfigShow {

    @Autowired(required = false)
    private List<Filter> filters = Collections.emptyList();
    @Autowired(required = false)
    private List<FilterRegistrationBean<?>> filterRegistrationBeans = Collections.emptyList();
    @Autowired(required = false)
    private List<OrderedFilter> orderedFilters = Collections.emptyList();
    @Autowired(required = false)
    private List<GenericFilterBean> genericFilterBeans = Collections.emptyList();

    @PostConstruct
    public void setup() {
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
