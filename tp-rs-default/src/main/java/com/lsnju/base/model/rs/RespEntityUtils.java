package com.lsnju.base.model.rs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lsnju.base.enums.BizErrEnum;

/**
 * @author ls
 * @since 2020/5/27 14:12
 * @version V1.0
 */
public class RespEntityUtils {

    public static <T> ResponseEntity<BaseResp<T>> success(T data) {
        return success(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<BaseResp<T>> success(T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(BaseResp.of(data, BizErrEnum.DEFAULT_SUCCESS), httpStatus);
    }

    public static <T> ResponseEntity<BaseResp<T>> fail(BizErrEnum code, HttpStatus httpStatus) {
        return of(null, code, httpStatus);
    }

    public static <T> ResponseEntity<BaseResp<T>> fail(BizErrEnum code, String msg, HttpStatus httpStatus) {
        return of(null, code, msg, httpStatus);
    }

    public static <T> ResponseEntity<BaseResp<T>> fail(String code, String msg, HttpStatus httpStatus) {
        return of(null, code, msg, httpStatus);
    }

    public static <T> ResponseEntity<BaseResp<T>> of(T data, BizErrEnum code, HttpStatus httpStatus) {
        return new ResponseEntity<>(BaseResp.of(data, code), httpStatus);
    }

    public static <T> ResponseEntity<BaseResp<T>> of(T data, BizErrEnum code, String msg, HttpStatus httpStatus) {
        return new ResponseEntity<>(BaseResp.of(data, code, msg), httpStatus);
    }

    public static <T> ResponseEntity<BaseResp<T>> of(T data, String code, String msg, HttpStatus httpStatus) {
        return new ResponseEntity<>(BaseResp.of(data, code, msg), httpStatus);
    }
}
