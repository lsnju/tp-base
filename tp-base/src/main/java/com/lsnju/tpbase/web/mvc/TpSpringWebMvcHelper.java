package com.lsnju.tpbase.web.mvc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/8/11 9:56
 * @version V1.0
 */
@Slf4j
public class TpSpringWebMvcHelper {

    private final List<HandlerExceptionResolver> resolvers;

    public TpSpringWebMvcHelper(List<HandlerExceptionResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public String getServerUrl() {
        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath().build().encode().toUriString();
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return StringUtils.EMPTY;
        }
    }

    public List<HandlerExceptionResolver> getResolvers() {
        return resolvers;
    }
}
