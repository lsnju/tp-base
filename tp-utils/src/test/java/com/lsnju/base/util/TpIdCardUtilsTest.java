package com.lsnju.base.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.lsnju.base.util.TpIdCardUtils.IDInfo;
import com.lsnju.base.util.TpIdCardUtils.SexEnum;

/**
 * 18 位样例校验位按 GB11643 加权因子计算。
 */
class TpIdCardUtilsTest {

    /** 有效 18 位（末位校验码已校验） */
    private static final String ID_18_VALID = "330324198803123048";

    /** 有效 15 位（仅格式） */
    private static final String ID_15_VALID = "330324880312304";

    @Test
    void validatePattern_18_and_15() {
        assertTrue(TpIdCardUtils.validatePattern(ID_18_VALID));
        assertTrue(TpIdCardUtils.validatePattern(ID_15_VALID));
        assertFalse(TpIdCardUtils.validatePattern(""));
        assertFalse(TpIdCardUtils.validatePattern("33032419880312304"));
        assertFalse(TpIdCardUtils.validatePattern("3303241988031230499"));
    }

    @Test
    void validate_18_checksum() {
        assertTrue(TpIdCardUtils.validate(ID_18_VALID));
        assertFalse(TpIdCardUtils.validate("330324198803123049"));
    }

    @Test
    void validate_15_afterPattern() {
        assertTrue(TpIdCardUtils.validate(ID_15_VALID));
    }

    @Test
    void parse_18() {
        IDInfo info = TpIdCardUtils.parse(ID_18_VALID);
        assertEquals("330324", info.getLoc());
        assertEquals("19880312", info.getBirth());
        assertEquals(SexEnum.FEMALE, info.getSex());
        assertNotNull(info.getBirthDate());
    }

    @Test
    void parse_15() {
        IDInfo info = TpIdCardUtils.parse(ID_15_VALID);
        assertEquals("330324", info.getLoc());
        assertEquals("19880312", info.getBirth());
        assertEquals(SexEnum.FEMALE, info.getSex());
    }

    @Test
    void parse_invalid_returnsEmptyBuilderFields() {
        IDInfo info = TpIdCardUtils.parse("bad");
        assertEquals(null, info.getLoc());
        assertEquals(null, info.getBirth());
        assertEquals(null, info.getSex());
    }
}
