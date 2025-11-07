package com.lsnju.tpbase.mq;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import com.lsnju.base.model.BaseRo;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.mq.vo.TpMqMsgVo;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2025/9/21 09:58
 * @version V1.0
 */
@Slf4j
public class TpMqListenerTemplate implements DigestConstants {

    private static final Logger DIGEST_LOG = LoggerFactory.getLogger(TP_MQ_IN_DIGEST);

    public static void execute(Message message, Function<TpMqMsgVo, ? extends BaseRo> function) {
        TpMqMsgVo msg = TpMqMsgUtils.parse(message);
        long startTime = System.nanoTime();
        String code = "S";
        try {
            BaseRo result = function.apply(msg);
            log.info("TpMqListenerTemplate.ret = {}", result);
            if (result == null || !result.isSuccess()) {
                code = "F";
            }
        } catch (Exception e) {
            code = "E";
            throw e;
        } finally {
            long endTime = System.nanoTime();
            DIGEST_LOG.info("[{}, {}, {}, {}, {}, {}ms, {}]", msg.getExchange(), msg.getRoutingKey(), msg.getQueue(),
                msg.getMsgId(), msg.getCorrelationId(), (endTime - startTime) / MS_SCALE, code);
        }
    }

}
