package com.lsnju.base.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author ls
 * @since 2023-07-20 07:18:28
 * @version V1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TpStatusEnum implements TpBaseEnum {

    /** 有效 */
    E("E", "有效"),

    /** 无效 */
    D("D", "无效");

    @Getter
    private final String code;
    @Getter
    private final String desc;

    public static TpStatusEnum getByCode(String code) {
        return Stream.of(values()).filter(i -> i.code.equals(code)).findAny().orElse(null);
    }

}
