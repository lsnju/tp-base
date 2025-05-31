package com.lsnju.tpbase.debug.filter;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private List<WebFilter> webFilters = Collections.emptyList();

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toPrettyString("webFilters", webFilters));
        log.debug(SEP);
    }

}
