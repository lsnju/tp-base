package com.lsnju.tpbase.daemon;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

import com.lsnju.tpbase.daemon.utils.TaskCountContext;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.web.filter.RequestId;
import com.lsnju.base.util.UUIDGenerator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lisong
 * @since 2020-01-20 14:48:31
 * @version V1.0
 */
@Setter
public abstract class AbstractTask implements Runnable, DigestConstants {

    /** */
    private static final String W_REQ_ID = RequestId.MDC_REQ_ID;

    private static final Logger DIGEST_LOG = LoggerFactory.getLogger(DigestConstants.TP_BIZ_DAEMON_DIGEST);
    private static final String FORMAT_STR = "[%s,%sms,%s,%s] %s";
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${quartz.task}")
    @Getter(AccessLevel.PROTECTED)
    private String taskStatus;

    @Override
    public void run() {
        long startTime = System.nanoTime();
        String success = "S";
        try {
            MDC.put(W_REQ_ID, UUIDGenerator.getSUID());
            if (!StringUtils.equals("on", taskStatus)) {
                log.info("taskStatus = {}", taskStatus);
                return;
            }
            execute();
        } catch (Throwable e) {
            log.error(String.format("【定时任务异常】error=%s", e.getMessage()), e);
            success = "E";
        } finally {
            long endTime = System.nanoTime();
            String clazzName = this.getClass().getSimpleName();

            long taskSize = TaskCountContext.getTaskSize();
            long total = TaskCountContext.getTotalSize();
            String tag = taskSize > 0 ? String.format("====%s", total) : "";

            DIGEST_LOG.info(String.format(FORMAT_STR, clazzName, (endTime - startTime) / MS_SCALE, success, taskSize, tag));
            TaskCountContext.clean();
            MDC.remove(W_REQ_ID);
        }
    }

    /** 定时任务处理 */
    protected abstract void execute();
}
