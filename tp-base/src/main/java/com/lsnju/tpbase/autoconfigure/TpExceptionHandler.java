package com.lsnju.tpbase.autoconfigure;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.lsnju.base.enums.BizErrorEnum;
import com.lsnju.base.http.NetworkExceptionUtils;
import com.lsnju.base.model.rs.BaseResp;
import com.lsnju.base.model.rs.RespEntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2022/8/24 20:49
 * @version V1.0
 */
@Slf4j
@Configuration
public class TpExceptionHandler {

    public static final String TAG = "[TP-ERROR]";

    @RestControllerAdvice
    @Order()
    public static class TpDefaultExceptionConfiguration {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<BaseResp<Void>> defaultExceptionHandler(Exception e) {
            log.warn("[异常类型] class = {}", e.getClass());
            log.error(String.format("%s [未知系统异常]", TAG), e);
            ResponseStatus rs = e.getClass().getAnnotation(ResponseStatus.class);
            if (rs != null) {
                return RespEntityUtils.fail(BizErrorEnum.INVALID_PARAM, rs.reason(), rs.code());
            } else {
                makeTmpDir(e);
                return RespEntityUtils.fail(BizErrorEnum.SYSTEM_ERROR, HttpStatus.BAD_REQUEST);
            }
        }

        private void makeTmpDir(Exception e) {
            // Failed to parse multipart servlet request; nested exception is java.lang.RuntimeException:
            // java.nio.file.NoSuchFileException: /tmp/undertow.8080.6560013878513610352/undertow8245999266619841888upload
            Throwable rootCause = NetworkExceptionUtils.getMostSpecificCause(e);
            if (rootCause instanceof NoSuchFileException) {
                String message = rootCause.getMessage();
                if (StringUtils.startsWith(message, File.separator)) {
                    try {
                        log.info("{}", message);
                        boolean mkdirs = new File(StringUtils.substringBeforeLast(message, File.separator)).mkdirs();
                        log.info("{}", mkdirs);
                    } catch (Exception ignore) {
                        // ...
                    }
                }
            }
        }
    }


    @RestControllerAdvice
    @ConditionalOnClass({HttpMediaTypeException.class,})
    @Order(0)
    public static class SpringWebExceptionConfiguration {
        @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class,
            MissingServletRequestPartException.class,
            HttpMessageNotReadableException.class,
        })
        public ResponseEntity<BaseResp<Void>> springWebReadExceptionHandler(Exception e) {
            log.warn("[异常类型] class = {}", e.getClass());
            log.error("{} errMsg = {}", TAG, e.getMessage());
            return RespEntityUtils.fail(BizErrorEnum.INVALID_PARAM, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({HttpMessageNotWritableException.class})
        public void springWebWriteExceptionHandler(HttpMessageNotWritableException e) {
            log.error(String.format("%s [HttpMessageNotWritableException]", TAG), e);
        }
    }

    @RestControllerAdvice
    @ConditionalOnClass(BindException.class)
    @Order(0)
    public static class SpringValidationExceptionConfiguration {
        @ExceptionHandler(BindException.class)
        public ResponseEntity<BaseResp<Void>> bindExceptionHandler(BindException e) {
            log.warn("[异常类型] class={}", e.getClass());
            log.error("{} [参数校验失败] errMsg = {}", TAG, e.getMessage());
            List<FieldError> fieldErrors = e.getFieldErrors();
            log.debug("----------------------");
            for (FieldError item : fieldErrors) {
                log.debug("+++++++++++++++++++++++++++++");
                log.debug("getObjectName = {}", item.getObjectName());
                log.debug("getField = {}", item.getField());
                log.debug("getCode = {}", item.getCode());
                log.debug("getDefaultMessage = {}", item.getDefaultMessage());
                log.debug("getRejectedValue = {}", item.getRejectedValue());
            }
            log.debug("----------------------");
            if (CollectionUtils.isEmpty(fieldErrors)) {
                return RespEntityUtils.fail(BizErrorEnum.INVALID_PARAM, HttpStatus.BAD_REQUEST);
            }
            FieldError firstError = fieldErrors.get(0);
            String errMsg = String.format("入参 ‘%s’ : %s", firstError.getField(), firstError.getDefaultMessage());
            return RespEntityUtils.fail(BizErrorEnum.INVALID_PARAM, errMsg, HttpStatus.BAD_REQUEST);
        }
    }

    @RestControllerAdvice
    @ConditionalOnClass(DuplicateKeyException.class)
    @Order(0)
    public static class SpringDaoExceptionConfiguration {
        @ExceptionHandler(DuplicateKeyException.class)
        public ResponseEntity<BaseResp<Void>> duplicateKeyExceptionHandler(DuplicateKeyException e) {
            log.error("{} [数据库唯一键异常] errMsg = {}", TAG, e.getMessage());
            return RespEntityUtils.fail(BizErrorEnum.DUPLICATED, HttpStatus.CONFLICT);
        }
    }

}
