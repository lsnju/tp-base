package com.lsnju.base.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lisong
 * @since 2020/1/23 9:36
 * @version V1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BasePageQueryForm extends BaseMo {
    /** 分页查询：页大小 */
    private int pageSize = 10;
    /** 分页查询：当前页 */
    private int currentPage = 1;
}
