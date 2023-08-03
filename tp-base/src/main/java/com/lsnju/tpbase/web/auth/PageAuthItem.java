package com.lsnju.tpbase.web.auth;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2023-07-26 20:23:21
 * @version V1.0
 */
@Getter
@Setter
public class PageAuthItem extends BaseMo {

    private String url;
    private String[] permissions;
}
