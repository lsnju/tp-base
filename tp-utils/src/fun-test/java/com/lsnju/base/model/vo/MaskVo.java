package com.lsnju.base.model.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lsnju.base.jackson.annotation.Mask;
import com.lsnju.base.jackson.mask.MaskingSerializerForDefault;
import com.lsnju.base.jackson.mask.MaskingSerializerForGid;
import com.lsnju.base.jackson.mask.MaskingSerializerForPhone;
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
public class MaskVo {

    private int id;
    private String name;
    private Money amount;

    private Date date1;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "GMT+8")
    private Date date2;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "GMT+8")
    private Date date3;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXX", timezone = "GMT+8")
    private Date date4;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT+8")
    private Date date5;

    private String sep0 = "---------------------------";

    @Mask(MaskingSerializerForDefault.class)
    private String desc1;
    @Mask(MaskingSerializerForGid.class)
    private String desc2;
    @Mask(MaskingSerializerForPhone.class)
    private String desc3;

    private String sep1 = "---------------------------";

    @Mask(serClass = MaskingSerializerForDefault.class)
    private String desc4;
    @Mask(serClass = MaskingSerializerForGid.class)
    private String desc5;
    @Mask(serClass = MaskingSerializerForPhone.class)
    private String desc6;

    private String sep2 = "---------------------------";

    @Mask(type = Mask.Type.DEFAULT)
    private String memo;
    @Mask(type = Mask.Type.PHONE)
    private String phone;
    @Mask(type = Mask.Type.GID)
    private String gid;

}
