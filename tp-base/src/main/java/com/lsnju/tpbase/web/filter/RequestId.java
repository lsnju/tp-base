package com.lsnju.tpbase.web.filter;

/**
 *
 * @author ls
 * @since 2021/5/8 10:57
 * @version V1.0
 */
public interface RequestId {
    /** */
    String REQ_ID = "w_req_id";

    String RX_CTX_ID = REQ_ID;

    String MDC_REQ_ID = REQ_ID;

    String HTTP_REQ_ID = REQ_ID;

}
