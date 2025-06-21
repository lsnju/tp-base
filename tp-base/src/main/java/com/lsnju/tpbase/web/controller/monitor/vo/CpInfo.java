package com.lsnju.tpbase.web.controller.monitor.vo;

import com.lsnju.base.jdbc.ConnectionInfo;
import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lis614
 * @since 2025/6/21 22:28
 * @version V1.0
 */
@Getter
@Setter
public class CpInfo extends BaseMo {
    private String name;
    private Integer max;
    private Integer min;
    private Integer active;
    private Float usage;
    private ConnectionInfo detail;
}
