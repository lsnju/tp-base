package com.lsnju.base.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * @author ls
 */
class TpStringMaskUtilsTest {

    @Test
    void mask_null_returnsNull() {
        assertNull(TpStringMaskUtils.DEFAULT.mask(null));
        assertNull(TpStringMaskUtils.maskDefault(null));
    }

    @Test
    void maskDefault_exposesTwoEach_replacesMiddleWithStars() {
        assertEquals("12******90", TpStringMaskUtils.maskDefault("1234567890"));
        assertEquals("ab", TpStringMaskUtils.maskDefault("ab"));
        assertEquals("abc", TpStringMaskUtils.maskDefault("abc"));
        assertEquals("abcd", TpStringMaskUtils.maskDefault("abcd"));
    }

    @Test
    void mask_fixedMaskLength_usesRepeatChar() {
        TpStringMaskUtils fourStars = new TpStringMaskUtils(2, 2, '*', 4);
        assertEquals("12****90", fourStars.mask("1234567890"));
    }

    @Test
    void mask_inPlace_whenTooShort_returnsOriginal() {
        TpStringMaskUtils m = new TpStringMaskUtils(3, 3, '#', -1);
        assertEquals("12345", m.mask("12345"));
    }
}
