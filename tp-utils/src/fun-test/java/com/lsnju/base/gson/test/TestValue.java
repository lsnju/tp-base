package com.lsnju.base.gson.test;

import com.google.gson.annotations.JsonAdapter;
import com.lsnju.base.gson.Masked22TypeAdapterFactory;
import com.lsnju.base.jackson.annotation.Mask;
import com.lsnju.base.jackson.mask.MaskingSerializerForDefault;
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
public class TestValue {

    private int id;
    private String name;
    private Money amount;
    @JsonAdapter(Masked22TypeAdapterFactory.class)
    @Mask(type = Mask.Type.DEFAULT)
    private String memo;
    @Mask(serClass = MaskingSerializerForDefault.class)
    private String desc;

    @Mask(type = Mask.Type.PHONE)
    private String phone;
    @Mask(type = Mask.Type.GID)
    private String gid;

}
