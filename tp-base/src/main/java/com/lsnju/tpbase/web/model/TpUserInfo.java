package com.lsnju.tpbase.web.model;

import java.util.Set;

/**
 *
 * @author ls
 * @since 2023/7/26 20:04
 * @version V1.0
 */
public interface TpUserInfo {

    long getId();

    String getPid();

    String getUid();

    String getName();

    Set<String> getRoleSet();

}
