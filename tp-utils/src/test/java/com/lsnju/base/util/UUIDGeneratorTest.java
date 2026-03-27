package com.lsnju.base.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * @author ls
 */
class UUIDGeneratorTest {

    @Test
    void getUUID_hasNoHyphens_andLength32() {
        String u = UUIDGenerator.getUUID();
        assertEquals(32, u.length());
        assertFalse(u.contains("-"));
        assertTrue(u.matches("[0-9a-fA-F]{32}"));
    }

    @Test
    void getSUID_isHex() {
        String s = UUIDGenerator.getSUID();
        assertTrue(s.matches("[0-9a-f]+"));
        assertFalse(s.isEmpty());
    }

    @Test
    void nextTraceIdHigh_combinesEpochLow32_andRandom() {
        long h = UUIDGenerator.nextTraceIdHigh(0x11223344);
        assertEquals(0x11223344L, h & 0xffffffffL);
    }

    @RepeatedTest(5)
    void randomString_respectsLengthAndAlphabet() {
        String s = UUIDGenerator.randomString(16);
        assertEquals(16, s.length());
        assertTrue(s.matches("[A-Za-z0-9\\-_]{16}"));
    }

    @Test
    void randomString_customSource() {
        char[] src = {'a'};
        assertEquals("aaaa", UUIDGenerator.randomString(4, src));
    }

    @Test
    void randomString_zeroLength_isEmpty() {
        assertEquals("", UUIDGenerator.randomString(0));
        assertEquals("", UUIDGenerator.randomString(0, new char[] {'x'}));
    }
}
