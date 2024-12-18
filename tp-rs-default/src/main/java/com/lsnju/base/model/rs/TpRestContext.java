package com.lsnju.base.model.rs;

/**
 *
 * @author lis614
 * @since 2024/9/29 20:37
 * @version V1.0
 */
public class TpRestContext {

    private static final ThreadLocal<String> RS_CODE = new ThreadLocal<>();
    private static final ThreadLocal<String> RS_MSG = new ThreadLocal<>();

    public static String getRsCode() {
        return RS_CODE.get();
    }

    public static void setRsCode(String rsCode) {
        RS_CODE.set(rsCode);
    }

    public static String getRsMsg() {
        return RS_MSG.get();
    }

    public static void setRsMsg(String rsMsg) {
        RS_MSG.set(rsMsg);
    }

    public static void clear() {
        RS_CODE.remove();
        RS_MSG.remove();
    }

}
