package com.lsnju.tpbase.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author lis614
 * @since 2024/2/20 11:14
 * @version V1.0
 */
public class TpAopUtils {

    public static String argumentsDesc(MethodInvocation invocation) {
        Object[] arguments = invocation.getArguments();
        if (arguments.length == 0) {
            return "()";
        }
        String value = Arrays.stream(arguments).map(TpAopUtils::argDesc).collect(Collectors.joining(", "));
        return "(" + value + ")";
    }

    public static String argDesc(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg instanceof String) {
            return StringUtils.left((String) arg, 32);
        }
        if (ClassUtils.isPrimitiveOrWrapper(arg.getClass())) {
            return String.valueOf(arg);
        }
        return "na";
    }

}
