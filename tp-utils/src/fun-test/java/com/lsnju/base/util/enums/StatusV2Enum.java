package com.lsnju.base.util.enums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import com.lsnju.base.enums.BaseEnum;

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
public enum StatusV2Enum implements BaseEnum<Integer> {

    @SerializedName("ENABLE")
    E(1, "E"),
    @SerializedName("DISABLE")
    D(2, "D"),

    // ...
    ;

    @JsonValue
    private final Integer code;
    private final String desc;

    public static StatusV2Enum getByCode(Integer code) {
        return Stream.of(values()).filter(i -> i.code.equals(code)).findAny().orElse(null);
    }
}
