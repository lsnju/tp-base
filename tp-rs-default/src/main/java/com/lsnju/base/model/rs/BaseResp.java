package com.lsnju.base.model.rs;

import java.util.Objects;

import com.lsnju.base.enums.BizErrEnum;
import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ls
 * @since 2020/5/26 20:27
 * @version V1.0
 */
@Getter
public class BaseResp<T> extends BaseMo {

    private String code;
    private String message;
    @Setter
    private T data;

    public BaseResp() {
    }

    public BaseResp(String code, String message, T data) {
        TpRestContext.setRsCode(code);
        TpRestContext.setRsMsg(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void setCode(String code) {
        TpRestContext.setRsCode(code);
        this.code = code;
    }

    public void setMessage(String message) {
        TpRestContext.setRsMsg(message);
        this.message = message;
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
