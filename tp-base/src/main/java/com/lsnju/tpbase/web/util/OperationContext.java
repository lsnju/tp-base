package com.lsnju.tpbase.web.util;

import java.util.HashMap;
import java.util.Map;

import com.lsnju.tpbase.web.model.TpUserInfo;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 20:08:31
 * @version V1.0
 */
@Slf4j
public class OperationContext {

    /** */
    private static final ThreadLocal<TpUserInfo> USER = new ThreadLocal<>();
    /** */
    private static final ThreadLocal<String> UID = new ThreadLocal<>();

    private static final ThreadLocal<String> PID = new ThreadLocal<>();
    /** */
    private static final ThreadLocal<Map<String, Object>> CTX = new ThreadLocal<>();

    public static TpUserInfo getUserInfo() {
        return USER.get();
    }

    public static void setUserInfo(TpUserInfo userInfo) {
        USER.set(userInfo);
    }

    public static String getUid() {
        return UID.get();
    }

    public static void setUid(String uid) {
        UID.set(uid);
    }

    public static String getPid() {
        return PID.get();
    }

    public static void setPid(String pid) {
        PID.set(pid);
    }

    public static Object getContext(String key) {
        Map<String, Object> map = CTX.get();
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    public static void setContext(String key, Object value) {
        log.debug("[OperationContext] key={}, value={}", key, value);
        Map<String, Object> map = CTX.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
        CTX.set(map);
    }

    public static void clear() {
        log.debug("OperationContext.clear");
        USER.remove();
        UID.remove();
        PID.remove();
        if (CTX.get() != null) {
            CTX.get().clear();
            CTX.remove();
        }
    }

}
