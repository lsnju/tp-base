package com.lsnju.base.util.vo;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2024/11/2 12:21
 * @version V1.0
 */
@Getter
@Setter
public class DateTimeBean {

    private String name;
    private String value;
    private ZonedDateTime zonedDateTime;
    private LocalDateTime localDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "GMT+8")
    private Date date;

}
