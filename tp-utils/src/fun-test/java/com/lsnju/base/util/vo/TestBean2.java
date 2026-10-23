package com.lsnju.base.util.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.lsnju.base.jackson.RawJsonDeserializer;
import com.lsnju.base.util.enums.StatusEnum;

import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * @author lisong
 * @since 2020/3/18 19:39
 * @version V1.0
 */
@Data
public class TestBean2 {

    private int id;
    private String name;

    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private String memo;

    private BigDecimal amount;

    private StatusEnum status;

}
