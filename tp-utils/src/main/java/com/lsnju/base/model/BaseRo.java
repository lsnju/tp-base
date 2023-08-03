package com.lsnju.base.model;

import com.lsnju.base.enums.BizErrorEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lisong
 * @since 2020年1月23日 上午8:59:59
 * @version V1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BaseRo extends BaseMo {

    public static final BaseRo SUCCESS =
        new BaseRo() {
            {
                setSuccess(true);
                setResultCode(BizErrorEnum.SUCCESS.getCode());
                setResultMsg(BizErrorEnum.SUCCESS.getDesc());
            }

            @Override
            public boolean isSuccess() {
                return true;
            }
        };
    public static final BaseRo FAIL =
        new BaseRo() {
            {
                setSuccess(false);
                setResultCode(BizErrorEnum.SYSTEM_ERROR.getCode());
                setResultMsg(BizErrorEnum.SYSTEM_ERROR.getDesc());
            }

            @Override
            public boolean isSuccess() {
                return false;
            }
        };
    private String resultCode;
    private String resultMsg;
    private boolean success = false;
}
