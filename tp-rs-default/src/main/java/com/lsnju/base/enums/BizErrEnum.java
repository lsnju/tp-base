package com.lsnju.base.enums;

/**
 * @author ls
 * @since 2020/7/31 17:20
 * @version V1.0
 */
public interface BizErrEnum {

    BizErrEnum DEFAULT_SUCCESS = BizErrorEnum.SUCCESS;

    String getCode();

    String getDesc();
}
