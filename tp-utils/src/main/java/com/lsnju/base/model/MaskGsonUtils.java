package com.lsnju.base.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.gson.mask.MaskAnnotationTypeAdapterFactory;

/**
 * Gson-based masking JSON utility.
 *
 * @author ls
 */
public class MaskGsonUtils {

    private static final Gson DEFAULT_GSON = build(false);

    private static Gson build(boolean pretty) {
        GsonBuilder builder = GsonUtils.build(pretty).newBuilder();
        builder.registerTypeAdapterFactory(new MaskAnnotationTypeAdapterFactory());
        return builder.create();
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return DEFAULT_GSON.toJson(obj);
    }
}
