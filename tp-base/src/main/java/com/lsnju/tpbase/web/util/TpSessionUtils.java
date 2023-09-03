package com.lsnju.tpbase.web.util;

import java.util.Objects;

import com.lsnju.tpbase.web.model.TpUserInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ls
 * @since 2023-07-26 21:32:20
 * @version V1.0
 */
public class TpSessionUtils {

    /** 用户ID */
    public static final String SESSION_USER_ID = "@_@__$$_@@@_user_id";
    /** 新用户信息 */
    public static final String SESSION_USER_INFO = "@_@__$$_@@@_new_user_info";


    public static void invalidate(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }

    public static String getUid(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s == null) {
            return null;
        }
        TpUserInfo newUserInfo = (TpUserInfo) s.getAttribute(SESSION_USER_INFO);
        if (newUserInfo != null) {
            return newUserInfo.getUid();
        }
        return (String) s.getAttribute(SESSION_USER_ID);
    }

    public static TpUserInfo getUserInfo(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s == null) {
            return null;
        }
        return (TpUserInfo) s.getAttribute(SESSION_USER_INFO);
    }

    public static void setUserInfo(HttpServletRequest request, TpUserInfo newUserInfo) {
        Objects.requireNonNull(newUserInfo);
        removeNewUserInfo(request);
        HttpSession s = request.getSession(true);
        String uid = newUserInfo.getUid();
        s.setAttribute(SESSION_USER_ID, uid);
        s.setAttribute(SESSION_USER_INFO, newUserInfo);
    }

    public static void removeNewUserInfo(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s == null) {
            return;
        }
        s.removeAttribute(SESSION_USER_ID);
        s.removeAttribute(SESSION_USER_INFO);
        OperationContext.clear();
        s.invalidate();
    }

    public static void add(HttpServletRequest request, String key, Object value) {
        HttpSession s = request.getSession(true);
        s.setAttribute(key, value);
    }

    public static Object get(HttpServletRequest request, String key) {
        HttpSession s = request.getSession(false);
        if (s == null) {
            return null;
        }
        return s.getAttribute(key);
    }

    public static Object getAndRemove(HttpServletRequest request, String key) {
        HttpSession s = request.getSession(false);
        if (s == null) {
            return null;
        }
        Object retValue = s.getAttribute(key);
        s.removeAttribute(key);
        return retValue;
    }

}
