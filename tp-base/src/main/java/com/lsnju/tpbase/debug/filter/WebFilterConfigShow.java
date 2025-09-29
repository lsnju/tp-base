package com.lsnju.tpbase.debug.filter;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.server.WebFilter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2022/12/1 21:53
 * @version V1.0
 */
@Slf4j
@Setter
public class WebFilterConfigShow extends AbstractFilterConfigShow implements InitializingBean {

    private final List<WebFilter> webFilters;

    public WebFilterConfigShow(List<WebFilter> webFilters) {
        this.webFilters = webFilters;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toPrettyString("webFilters", webFilters));
        log.debug(SEP);
    }

}
