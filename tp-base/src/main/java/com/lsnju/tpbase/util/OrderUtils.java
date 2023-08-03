package com.lsnju.tpbase.util;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 *
 * @author ls
 * @since 2023-07-25 22:37:24
 * @version V1.0
 */
public class OrderUtils {

    public static String getOrder(Object target) {
        if (target instanceof Ordered) {
            return String.valueOf(((Ordered) target).getOrder());
        }
        final Order annotation = target.getClass().getAnnotation(Order.class);
        if (annotation != null) {
            return String.valueOf(annotation.value());
        }
        return "na";
    }

}
