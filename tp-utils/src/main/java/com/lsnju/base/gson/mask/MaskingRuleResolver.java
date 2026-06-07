package com.lsnju.base.gson.mask;

import com.lsnju.base.jackson.annotation.Mask;
import com.lsnju.base.jackson.mask.MaskingSerializerForDefault;
import com.lsnju.base.jackson.mask.MaskingSerializerForGid;
import com.lsnju.base.jackson.mask.MaskingSerializerForPhone;
import com.lsnju.base.util.TpStringMaskUtils;

/**
 * Resolve masking rules from {@link Mask} annotation.
 *
 * @author ls
 */
public final class MaskingRuleResolver {

    private static final TpStringMaskUtils DEFAULT_MASK = new TpStringMaskUtils(4, 4, '*', 2);
    private static final TpStringMaskUtils PHONE_MASK = new TpStringMaskUtils(3, 4, '*', 4);
    private static final TpStringMaskUtils GID_MASK = new TpStringMaskUtils(6, 4, '*', -1);

    private MaskingRuleResolver() {}

    public static String mask(Mask annotation, String value) {
        if (annotation == null || value == null) {
            return value;
        }
        TpStringMaskUtils bySerializer = resolveBySerializer(annotation);
        if (bySerializer != null) {
            return bySerializer.mask(value);
        }
        switch (annotation.type()) {
            case PHONE:
                return PHONE_MASK.mask(value);
            case GID:
                return GID_MASK.mask(value);
            case DEFAULT:
            default:
                return DEFAULT_MASK.mask(value);
        }
    }

    private static TpStringMaskUtils resolveBySerializer(Mask annotation) {
        TpStringMaskUtils mask = resolveFromClasses(annotation.serClass());
        if (mask != null) {
            return mask;
        }
        return resolveFromClasses(annotation.value());
    }

    private static TpStringMaskUtils resolveFromClasses(Class<?>[] classes) {
        if (classes == null) {
            return null;
        }
        for (Class<?> cls : classes) {
            if (cls == null) {
                continue;
            }
            if (MaskingSerializerForPhone.class.isAssignableFrom(cls)) {
                return PHONE_MASK;
            }
            if (MaskingSerializerForGid.class.isAssignableFrom(cls)) {
                return GID_MASK;
            }
            if (MaskingSerializerForDefault.class.isAssignableFrom(cls)) {
                return DEFAULT_MASK;
            }
        }
        return null;
    }
}
