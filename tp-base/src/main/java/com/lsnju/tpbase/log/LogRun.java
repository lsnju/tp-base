package com.lsnju.tpbase.log;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.MDC;

import com.lsnju.tpbase.util.TpTraceUtils;
import com.lsnju.tpbase.web.filter.RequestId;

/**
 * @author lisong
 * @since 2020/2/6 21:16
 * @version V1.0
 */
public class LogRun implements Runnable {

    private final Runnable run;
    private final String currentId = TpTraceUtils.currentTraceId();

    public LogRun(Runnable run) {
        this.run = run;
    }

    public static Runnable wrap(Runnable runnable) {
        return new LogRun(runnable);
    }

    public static Runnable wrapCurrent(Runnable runnable) {
        final String currentReqId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(currentReqId);
        return () -> {
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                runnable.run();
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    public static <V> Callable<V> wrapCall(Callable<V> runnable) {
        final String currentReqId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(currentReqId);
        return () -> {
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                return runnable.call();
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    public static void wrapExe(Runnable runnable) {
        final String currentReqId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(currentReqId);
        try {
            MDC.put(RequestId.MDC_REQ_ID, newId);
            runnable.run();
        } finally {
            MDC.put(RequestId.MDC_REQ_ID, currentReqId);
        }
    }

    public static <T> Supplier<T> wrapSupplier(Supplier<T> supplier) {
        final String currentReqId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(currentReqId);
        return () -> {
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                return supplier.get();
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    public static <T> Consumer<T> wrapConsumer(Consumer<T> consumer) {
        final String currentReqId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(currentReqId);
        return (T arg) -> {
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                consumer.accept(arg);
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

    @Override
    public void run() {
        final String newId = TpTraceUtils.newTraceId(currentId);
        try {
            MDC.put(RequestId.MDC_REQ_ID, newId);
            this.run.run();
        } finally {
            MDC.put(RequestId.MDC_REQ_ID, currentId);
        }
    }

}
