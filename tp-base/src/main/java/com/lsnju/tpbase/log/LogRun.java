package com.lsnju.tpbase.log;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.lsnju.base.util.UUIDGenerator;
import com.lsnju.tpbase.web.filter.RequestId;

/**
 * @author lisong
 * @since 2020/2/6 21:16
 * @version V1.0
 */
public class LogRun implements Runnable {

    public static final String TAG = ":";

    private final Runnable run;
    private final String reqId = MDC.get(RequestId.MDC_REQ_ID);

    public LogRun(Runnable run) {
        this.run = run;
    }

    public static Runnable wrap(Runnable runnable) {
        return new LogRun(runnable);
    }

    public static Runnable wrapCurrent(Runnable runnable) {
        String reqId = MDC.get(RequestId.MDC_REQ_ID);
        final String newId = StringUtils.join(StringUtils.substring(reqId, -16), TAG, UUIDGenerator.getSUID());
        return () -> {
            final String currentReqId = MDC.get(RequestId.MDC_REQ_ID);
            MDC.put(RequestId.MDC_REQ_ID, newId);
            try {
                runnable.run();
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    public static <V> Callable<V> wrapCall(Callable<V> runnable) {
        String reqId = MDC.get(RequestId.MDC_REQ_ID);
        String newId = StringUtils.join(StringUtils.substring(reqId, -16), TAG, UUIDGenerator.getSUID());
        return () -> {
            final String currentReqId = MDC.get(RequestId.MDC_REQ_ID);
            MDC.put(RequestId.MDC_REQ_ID, newId);
            try {
                return runnable.call();
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    public static void wrapExe(Runnable runnable) {
        String reqId = MDC.get(RequestId.MDC_REQ_ID);
        String newId = StringUtils.join(StringUtils.substring(reqId, -16), TAG, UUIDGenerator.getSUID());
        MDC.put(RequestId.MDC_REQ_ID, newId);
        try {
            runnable.run();
        } finally {
            MDC.put(RequestId.MDC_REQ_ID, reqId);
        }
    }

    public static <T> Supplier<T> wrapSupplier(Supplier<T> supplier) {
        String reqId = MDC.get(RequestId.MDC_REQ_ID);
        String newId = StringUtils.join(StringUtils.substring(reqId, -16), TAG, UUIDGenerator.getSUID());
        return () -> {
            final String currentReqId = MDC.get(RequestId.MDC_REQ_ID);
            MDC.put(RequestId.MDC_REQ_ID, newId);
            try {
                return supplier.get();
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    public static <T> Consumer<T> wrapConsumer(Consumer<T> consumer) {
        String reqId = MDC.get(RequestId.MDC_REQ_ID);
        String newId = StringUtils.join(StringUtils.substring(reqId, -16), TAG, UUIDGenerator.getSUID());
        return (T arg) -> {
            final String currentReqId = MDC.get(RequestId.MDC_REQ_ID);
            MDC.put(RequestId.MDC_REQ_ID, newId);
            try {
                consumer.accept(arg);
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    @Override
    public void run() {
        String newId = StringUtils.join(StringUtils.substring(reqId, -16), TAG, UUIDGenerator.getSUID());
        MDC.put(RequestId.MDC_REQ_ID, newId);
        try {
            this.run.run();
        } finally {
            MDC.remove(RequestId.MDC_REQ_ID);
        }
    }
}
