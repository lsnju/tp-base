package com.lsnju.base.enums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ls
 * @since 2020/5/27 14:29
 * @version V1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BizErrorEnum implements BizErrEnum {

    SUCCESS("SUCCESS", "处理成功"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误"),
    INVALID_PARAM("INVALID_PARAM", "请求参数错误"),
    NOT_FOUND("NOT_FOUND", "数据未找到"),
    DUPLICATED("DUPLICATED", "重复请求"),
    SIGN_ERROR("SIGN_ERROR", "签名错误"),
    NO_AUTH("NO_AUTH", "接口无权访问"),

    // ...
    ;

    @JsonValue
    private final String code;
    private final String desc;

    public static BizErrorEnum getByCode(String code) {
        return Stream.of(values()).filter(i -> i.code.equals(code)).findAny().orElse(null);
    }
}
