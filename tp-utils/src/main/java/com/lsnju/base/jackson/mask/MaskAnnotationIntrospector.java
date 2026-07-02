package com.lsnju.base.jackson.mask;

import java.io.Serial;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.lsnju.base.jackson.annotation.Mask;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:22
 * @version V1.0
 */
public class MaskAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Object findSerializer(Annotated annotated) {
        Mask annotation = annotated.getAnnotation(Mask.class);
        if (annotation != null) {
            if (annotation.serClass() != null) {
                for (Class<?> item : annotation.serClass()) {
                    return item;
                }
            }
            if (annotation.value() != null) {
                for (Class<?> item : annotation.value()) {
                    return item;
                }
            }
            if (annotation.type() == null) {
                return MaskingSerializerForDefault.class;
            }
            switch (annotation.type()) {
                case GID:
                    return MaskingSerializerForGid.class;
                case PHONE:
                    return MaskingSerializerForPhone.class;
                default:
                    return MaskingSerializerForDefault.class;
            }
        }
        return super.findSerializer(annotated);
    }
}
