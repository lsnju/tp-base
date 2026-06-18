package com.lsnju.base.util;

import com.lsnju.base.enums.BaseEnum;
import com.lsnju.base.enums.TpBaseEnum;

/**
 *
 * @author ls
 * @since 2026-06-13 08:50
 * @version V1.0
 */
public class EnumUtils {

    public static <E extends TpBaseEnum> E fromCode(Class<E> enumClass, String code) {
        for (E value : enumClass.getEnumConstants()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static <T, E extends BaseEnum<T>> E fromCode(Class<E> enumClass, T code) {
        for (E value : enumClass.getEnumConstants()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
