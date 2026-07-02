package com.lsnju.base.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.gson.reflect.TypeToken;
import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.jackson.annotation.Mask;
import com.lsnju.base.jackson.mask.MaskingSerializerForDefault;
import com.lsnju.base.jackson.mask.MaskingSerializerForGid;
import com.lsnju.base.jackson.mask.MaskingSerializerForPhone;

/**
 * Unit tests for {@link MaskGsonUtils}.
 *
 * @author ls
 */
class MaskGsonUtilsTest {

    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();

    static class MaskedBean {
        public String plain = "visible";

        @Mask
        public String secretDefault = "12345678901234567890";

        @Mask(type = Mask.Type.PHONE)
        public String phone = "1234567890123456";

        @Mask(type = Mask.Type.GID)
        public String gid = "1234567890123456";

        @Mask(MaskingSerializerForDefault.class)
        public String desc1 = "12345678901234567890";

        @Mask(MaskingSerializerForPhone.class)
        public String desc2 = "1234567890123456";

        @Mask(MaskingSerializerForGid.class)
        public String desc3 = "1234567890123456";
    }

    @Test
    void toJson_null_returnsNull() {
        assertNull(MaskGsonUtils.toJson(null));
    }

    @Test
    void toJson_masksAnnotatedStrings_andLeavesPlain() {
        MaskedBean bean = new MaskedBean();
        String json = MaskGsonUtils.toJson(bean);
        assertNotNull(json);
        assertTrue(json.contains("\"plain\":\"visible\""));
        assertFalse(json.contains("12345678901234567890"));
        assertTrue(json.contains("1234**7890"));
    }

    @Test
    void toJson_phoneAndGid_rules() {
        MaskedBean bean = new MaskedBean();
        String json = MaskGsonUtils.toJson(bean);
        assertNotNull(json);
        assertTrue(json.contains("123****3456"));
        assertTrue(json.contains("123456******3456"));
    }

    @Test
    void gsonUtils_withoutMask_showsFullSecrets() {
        MaskedBean bean = new MaskedBean();
        String plain = GsonUtils.toJson(bean);
        assertNotNull(plain);
        assertTrue(plain.contains("12345678901234567890"));
        assertTrue(plain.contains("1234567890123456"));
    }

    @Test
    void toJson_matchesMaskJacksonUtils_byFieldValue() {
        MaskedBean bean = new MaskedBean();
        String gsonJson = MaskGsonUtils.toJson(bean);
        String jacksonJson = MaskJacksonUtils.toJson(bean);
        assertNotNull(gsonJson);
        assertNotNull(jacksonJson);

        Map<String, String> gsonMap = GsonUtils.fromJson(gsonJson, MAP_TYPE);
        Map<String, String> jacksonMap = GsonUtils.fromJson(jacksonJson, MAP_TYPE);
        assertNotNull(gsonMap);
        assertNotNull(jacksonMap);

        assertEquals(jacksonMap.get("plain"), gsonMap.get("plain"));
        assertEquals(jacksonMap.get("secretDefault"), gsonMap.get("secretDefault"));
        assertEquals(jacksonMap.get("phone"), gsonMap.get("phone"));
        assertEquals(jacksonMap.get("gid"), gsonMap.get("gid"));
        assertEquals(jacksonMap.get("desc1"), gsonMap.get("desc1"));
        assertEquals(jacksonMap.get("desc2"), gsonMap.get("desc2"));
        assertEquals(jacksonMap.get("desc3"), gsonMap.get("desc3"));
    }
}
