package com.lsnju.base.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.lsnju.base.jackson.JacksonUtils;
import com.lsnju.base.jackson.annotation.Mask;

/**
 * Unit tests for {@link MaskJacksonUtils}.
 *
 * @author ls
 */
class MaskJacksonUtilsTest {

    static class MaskedBean {
        public String plain = "visible";

        @Mask
        public String secretDefault = "12345678901234567890";

        @Mask(type = Mask.Type.PHONE)
        public String phone = "1234567890123456";

        @Mask(type = Mask.Type.GID)
        public String gid = "1234567890123456";
    }

    @Test
    void toJson_null_returnsNull() {
        assertNull(MaskJacksonUtils.toJson(null));
    }

    @Test
    void toJson_masksAnnotatedStrings_andLeavesPlain() {
        MaskedBean bean = new MaskedBean();
        String json = MaskJacksonUtils.toJson(bean);
        assertNotNull(json);
        assertTrue(json.contains("\"plain\":\"visible\""));
        assertFalse(json.contains("12345678901234567890"));
        assertTrue(json.contains("1234**7890"));
    }

    @Test
    void toJson_defaultMask_matchesTpStringMaskUtils() {
        MaskedBean bean = new MaskedBean();
        String json = MaskJacksonUtils.toJson(bean);
        assertNotNull(json);
        assertTrue(json.contains("1234**7890"));
    }

    @Test
    void toJson_phoneAndGid_rules() {
        MaskedBean bean = new MaskedBean();
        String json = MaskJacksonUtils.toJson(bean);
        assertNotNull(json);
        assertTrue(json.contains("123****3456"));
        assertTrue(json.contains("123456******3456"));
    }

    @Test
    void jacksonUtils_withoutMask_showsFullSecrets() {
        MaskedBean bean = new MaskedBean();
        String plain = JacksonUtils.toJson(bean);
        assertNotNull(plain);
        assertTrue(plain.contains("12345678901234567890"));
        assertTrue(plain.contains("1234567890123456"));
    }
}
