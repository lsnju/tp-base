package com.lsnju.base.util.vo;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.lsnju.base.jackson.RawJsonDeserializer;
import com.lsnju.base.money.Money;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * @author lisong
 * @since 2020/3/18 19:39
 * @version V1.0
 */
@Getter
@Setter
@XmlRootElement
public class TestBean {

    private int id;
    private String name;

    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private String memo;

    private Money amount;
}
