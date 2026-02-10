package com.lsnju.base.jackson.mask;

import com.lsnju.base.jackson.annotation.Mask;

import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.Annotated;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:22
 * @version V1.0
 */
public class MaskAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findSerializer(MapperConfig<?> config, Annotated annotated) {
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
            return switch (annotation.type()) {
                case GID -> MaskingSerializerForGid.class;
                case PHONE -> MaskingSerializerForPhone.class;
                default -> MaskingSerializerForDefault.class;
            };
        }
        return super.findSerializer(config, annotated);
    }

}
