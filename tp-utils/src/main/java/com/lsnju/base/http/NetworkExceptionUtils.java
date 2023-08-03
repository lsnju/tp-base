package com.lsnju.base.http;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.util.ClassUtils;

/**
 *
 * @author ls
 * @since 2021/10/20 21:49
 * @version V1.0
 */
public class NetworkExceptionUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger("tp-ex-class");

    private static final String[] NETWORK_EXCEPTION_CLASS_NAME = {
        "java.net.SocketException",
        "java.net.SocketTimeoutException",
        "java.net.UnknownHostException",
        "javax.ws.rs.NotFoundException",
        "org.apache.http.NoHttpResponseException",
    };
    private static final List<Class<?>> NETWORK_EXCEPTION_CLASS = new ArrayList<>();

    static {
        for (String className : NETWORK_EXCEPTION_CLASS_NAME) {
            try {
                final Class<?> clazz = Class.forName(className);
                NETWORK_EXCEPTION_CLASS.add(clazz);
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

    public static boolean isNetworkCause(Throwable rootCause) {
        if (rootCause == null) {
            return false;
        }
        LOGGER.warn("{}, {}", rootCause.getClass().getName(), rootCause.getMessage());
        for (Class<?> clazz : NETWORK_EXCEPTION_CLASS) {
            final boolean isAssign = ClassUtils.isAssignableValue(clazz, rootCause);
            if (isAssign) {
                return true;
            }
        }
        return false;
    }

    public static Throwable getMostSpecificCause(Throwable original) {
        return NestedExceptionUtils.getMostSpecificCause(original);
    }

}
