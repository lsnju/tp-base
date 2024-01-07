package com.lsnju.base.model.rs;

import java.util.Objects;

import org.slf4j.MDC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lsnju.base.enums.BizErrEnum;
import com.lsnju.base.model.BaseMo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ls
 * @since 2020/5/26 20:27
 * @version V1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResp<T> extends BaseMo {

    private String code;
    @JsonProperty("msg")
    @SerializedName("msg")
    private String message;
    private T data;

    private String traceId;

    public BaseResp(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = MDC.get("w-req-id");
    }


    public static <T> BaseResp<T> of(T data, BizErrEnum error) {
        Objects.requireNonNull(error);
        return new BaseResp<>(error.getCode(), error.getDesc(), data);
    }

    public static <T> BaseResp<T> of(T data, BizErrEnum error, String message) {
        Objects.requireNonNull(error);
        return new BaseResp<>(error.getCode(), message, data);
    }

    public static <T> BaseResp<T> of(T data, String code, String message) {
        return new BaseResp<>(code, message, data);
    }

    public static <T> BaseResp<T> ok(T data) {
        return BaseResp.of(data, BizErrEnum.DEFAULT_SUCCESS);
    }
}
