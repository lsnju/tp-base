package com.lsnju.tpbase.web.filter.rest;

/**
 * @author ls
 * @since 2020/11/27 10:12
 * @version V1.0
 */
public interface RestApiTokenManager {

    boolean isValid(String tokenId);

    default boolean login(String pid, String uid) {
        return true;
    }

}
