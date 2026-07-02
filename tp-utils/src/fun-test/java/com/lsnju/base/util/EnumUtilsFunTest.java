package com.lsnju.base.util;

import org.junit.jupiter.api.Test;

import com.lsnju.base.util.enums.StatusEnum;
import com.lsnju.base.util.enums.StatusV2Enum;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2026-06-13 08:51
 * @version V1.0
 */
@Slf4j
public class EnumUtilsFunTest {

    @Test
    void test_001() {
        StatusEnum[] enumConstants = StatusEnum.class.getEnumConstants();
        for (StatusEnum enumConstant : enumConstants) {
            log.info("test_001 -> " + enumConstant);
        }
    }


    @Test
    void test_002() {
        for (StatusEnum item : StatusEnum.values()) {
            log.info("{}", EnumUtils.fromCode(StatusEnum.class, item.getCode()));
        }
        for (StatusV2Enum item : StatusV2Enum.values()) {
            log.info("{}", EnumUtils.fromCode(StatusV2Enum.class, item.getCode()));
        }
    }

}
