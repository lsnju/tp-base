package com.lsnju.tpbase.web.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 *
 * @author ls
 * @since 2021/5/18 16:04
 * @version V1.0
 */
public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;
    private final Map<String, String[]> customParams;


    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.customHeaders = new LinkedHashMap<>();
        this.customParams = new LinkedHashMap<>();
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public void putParam(String name, String value) {
        this.customParams.put(name, new String[]{value});
    }

    public void putParam(String name, String[] value) {
        this.customParams.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Set<String> headerValues = new HashSet<>();
        final String value = this.customHeaders.get(name);
        if (value != null) {
            headerValues.add(value);
        }

        Enumeration<String> underlyingHeaderValues = ((HttpServletRequest) getRequest()).getHeaders(name);
        while (underlyingHeaderValues.hasMoreElements()) {
            headerValues.add(underlyingHeaderValues.nextElement());
        }

        return Collections.enumeration(headerValues);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new HashSet<>(customHeaders.keySet());
        Enumeration<String> originNames = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (originNames.hasMoreElements()) {
            names.add(originNames.nextElement());
        }
        return Collections.enumeration(names);
    }

    @Override
    public String getParameter(String name) {
        final String[] value = getParameterValues(name);
        if (value != null && value.length > 1) {
            return value[0];
        }
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        final Map<String, String[]> ret = new LinkedHashMap<>(this.customParams);
        ret.putAll(super.getParameterMap());
        return ret;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        final Map<String, String[]> parameterMap = getParameterMap();
        return new IteratorEnumeration<>(parameterMap.keySet().iterator());
    }

    @Override
    public String[] getParameterValues(String name) {
        final Map<String, String[]> parameterMap = getParameterMap();
        return parameterMap.get(name);
    }

}
