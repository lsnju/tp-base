package com.lsnju.base.model;

import com.lsnju.base.jackson.JacksonUtils;

/**
 * @author lisong
 * @since 2020年1月23日 上午8:58:21
 * @version V1.0
 */
public class BaseMo {

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }

}
