package com.lsnju.tpbase.mq.vo;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2025/9/21 09:58
 * @version V1.0
 */
@Getter
@Setter
public class TpMqMsgVo extends BaseMo {

    private String exchange;
    private String routingKey;
    private String queue;

    private String msgId;
    private String correlationId;
    private String contentType;
    private long contentLength;

    private transient String body;

}
