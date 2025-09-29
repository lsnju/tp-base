package com.lsnju.tpbase.mq;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.lsnju.base.util.JsonUtils;
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
public class TpMqMsgUtils implements DigestConstants {

    private static final Logger DIGEST_LOG = LoggerFactory.getLogger(TP_MQ_IN);

    public static TpMqMsgVo parse(Message message) {
        return parse(message, true);
    }

    public static TpMqMsgVo parse(Message message, boolean body) {
        MessageProperties messageProperties = message.getMessageProperties();
        Objects.requireNonNull(messageProperties);
        showMsgProperties(messageProperties);

        TpMqMsgVo msg = new TpMqMsgVo();
        msg.setExchange(messageProperties.getReceivedExchange());
        msg.setRoutingKey(messageProperties.getReceivedRoutingKey());
        msg.setQueue(messageProperties.getConsumerQueue());
        msg.setMsgId(messageProperties.getMessageId());
        msg.setCorrelationId(messageProperties.getCorrelationId());
        msg.setContentType(messageProperties.getContentType());
        msg.setContentLength(messageProperties.getContentLength());
        if (body) {
            msg.setBody(getBodyContentAsString(message));
        }
        DIGEST_LOG.info("[{}, {}, {}, {}, {}]", msg.getExchange(), msg.getRoutingKey(), msg.getQueue(), msg.getMsgId(), msg.getCorrelationId());
        log.debug("app_mq_in_header = {}, app_mq_in_body = {}", msg, msg.getBody());
        return msg;
    }

    private static void showMsgProperties(MessageProperties messageProperties) {
        try {
            log.debug("rawMqHeader = {}", JsonUtils.toJson(messageProperties));
        } catch (Exception ignore) {
        }
    }

    public static String getBodyContentAsString(Message message) {
        final String contentType = message.getMessageProperties().getContentType();
        if (contentType == null) {
            return null;
        }
        try {
            return switch (contentType) {
                case MessageProperties.CONTENT_TYPE_TEXT_PLAIN,
                     MessageProperties.CONTENT_TYPE_JSON,
                     MessageProperties.CONTENT_TYPE_JSON_ALT,
                     MessageProperties.CONTENT_TYPE_XML -> new String(message.getBody(), encoding(message));
                default -> null;
            };
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    private static String encoding(Message message) {
        String contentEncoding = message.getMessageProperties().getContentEncoding();
        if (contentEncoding != null) {
            return contentEncoding;
        }
        return DEFAULT_ENCODING;
    }

    private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
}
