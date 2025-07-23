package com.lsnju.base.jackson.mask;

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
    @Override
    public Object findSerializer(Annotated annotated) {
        Mask annotation = annotated.getAnnotation(Mask.class);
        if (annotation != null) {
            if (annotation.serClass() != null && annotation.serClass().length > 0) {
                return annotation.serClass()[0];
            }
            if (annotation.value() != null && annotation.value().length > 0) {
                return annotation.value()[0];
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
