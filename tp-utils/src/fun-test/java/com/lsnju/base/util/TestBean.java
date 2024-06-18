package com.lsnju.base.util;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lsnju.base.jackson.RawJsonDeserializer;
import com.lsnju.base.model.BaseMo;
import com.lsnju.base.money.Money;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lisong
 * @since 2020/3/18 19:39
 * @version V1.0
 */
@Getter
@Setter
public class TestBean extends BaseMo {

    private int id;
    private String name;

    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private String memo;

    private Money amount;
}
