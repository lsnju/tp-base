package com.lsnju.tpbase.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.annotation.AnnotationUtils;

import com.lsnju.tpbase.cache.annotation.TpCacheKeyType;
import com.lsnju.tpbase.cache.enums.TpCacheTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2026-10-22 16:35:26
 * @version V1.0
 */
@Slf4j
public class TpKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        List<String> signature = new ArrayList<>();
        TpCacheKeyType methodAnnotation = AnnotationUtils.findAnnotation(method, TpCacheKeyType.class);

        if (methodAnnotation == null) {
            signature.add(target.getClass().getSimpleName());
            signature.add(method.getName());
        } else {
            TpCacheTypeEnum type = methodAnnotation.value();
            switch (type) {
                case NONE:
                    break;
                case CUSTOM:
                    signature.add(methodAnnotation.customKey());
                    break;
                case CLASS:
                    signature.add(target.getClass().getSimpleName());
                    break;
                case METHOD:
                    signature.add(method.getName());
                    break;
                default:
                    signature.add(target.getClass().getSimpleName());
                    signature.add(method.getName());
                    break;
            }
        }

        if (ArrayUtils.getLength(params) > 0) {
            for (Object param : params) {
                signature.add(toString(param));
            }
        }
        return String.join("_", signature);
    }

    private static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof TpCacheKey) {
            return ((TpCacheKey) obj).key();
        }
        return String.valueOf(obj);
    }

}
