package com.lsnju.base.util.enums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lisong
 * @since 2025/12/3 14:16
 * @version V1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusEnum {


    @SerializedName("ENABLE")
    E("ENABLE", "E"),
    @SerializedName("DISABLE")
    D("DISABLE", "D"),

    // ...
    ;

    @JsonValue
    private final String code;
    private final String desc;

    public static StatusEnum getByCode(String code) {
        return Stream.of(values()).filter(i -> i.code.equals(code)).findAny().orElse(null);
    }
}
