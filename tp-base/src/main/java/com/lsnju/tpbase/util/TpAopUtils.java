package com.lsnju.tpbase.util;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ClassUtils;

/**
 *
 * @author lis614
 * @since 2024/2/20 11:14
 * @version V1.0
 */
public class TpAopUtils {

    public static String argumentsDesc(MethodInvocation invocation) {
        Object[] arguments = invocation.getArguments();
        if (arguments.length != 1) {
            return null;
        }
        Object arg = arguments[0];
        if (arg == null) {
            return null;
        }
        if (arg instanceof String) {
            return (String) arg;
        }
        if (ClassUtils.isPrimitiveOrWrapper(arg.getClass())) {
            return String.valueOf(arg);
        }
        return null;
    }

}
