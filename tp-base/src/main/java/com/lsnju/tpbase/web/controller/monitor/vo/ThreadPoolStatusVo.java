package com.lsnju.tpbase.web.controller.monitor.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lsnju.base.model.BaseRo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2023-08-03 12:56:42
 * @version V1.0
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"resultCode", "resultMsg"})
public class ThreadPoolStatusVo extends BaseRo {
    private String hostname;
    private List<TpInfoVo> tpInfos;
    private List<TpInfoVo> scheduler;
}
