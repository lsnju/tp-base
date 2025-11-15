package com.lsnju.tpbase.log;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.lsnju.base.util.Profiler;
import com.lsnju.tpbase.util.TpTraceUtils;
import com.lsnju.tpbase.web.filter.RequestId;

/**
 *
 * @author lis614
 * @since 2024/12/14 10:23
 * @version V1.0
 */
public class TpTraceInterceptor {

    public static final String PREFIX = "<<< ";
    public static final String FORMAT = "%s = %s";

    private final Logger logger;
    private final String prefix;

    public TpTraceInterceptor() {
        this(PREFIX);
    }

    public TpTraceInterceptor(String prefix) {
        this(prefix, DigestConstants.TP_PROFILER);
    }

    public TpTraceInterceptor(String prefix, String logName) {
        this.prefix = prefix;
        this.logger = LoggerFactory.getLogger(logName);
    }

    public <T> T get(String name, Supplier<T> supplier) {
        return this.get(name, TpTraceUtils.currentTraceId(), supplier);
    }

    public <T> T get(String name, String traceId, Supplier<T> supplier) {
        final String currentTraceId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(traceId);
        try {
            MDC.put(RequestId.MDC_REQ_ID, newId);
            Profiler.start(String.format(FORMAT, newId, name));
            return supplier.get();
        } finally {
            Profiler.release();
            if (logger.isInfoEnabled()) {
                logger.info("\n{}\n", Profiler.dump(StringUtils.defaultString(prefix)));
            }
            Profiler.reset();
            MDC.put(RequestId.MDC_REQ_ID, currentTraceId);
        }
    }

    public <T> T call(String name, Callable<T> callable) throws Exception {
        return this.call(name, TpTraceUtils.currentTraceId(), callable);
    }

    public <T> T call(String name, String traceId, Callable<T> callable) throws Exception {
        final String currentTraceId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(traceId);
        try {
            MDC.put(RequestId.MDC_REQ_ID, newId);
            Profiler.start(String.format(FORMAT, newId, name));
            return callable.call();
        } finally {
            Profiler.release();
            if (logger.isInfoEnabled()) {
                logger.info("\n{}\n", Profiler.dump(StringUtils.defaultString(prefix)));
            }
            Profiler.reset();
            MDC.put(RequestId.MDC_REQ_ID, currentTraceId);
        }
    }

    public void run(String name, Runnable runnable) {
        this.run(name, TpTraceUtils.currentTraceId(), runnable);
    }

    public void run(String name, String traceId, Runnable runnable) {
        final String currentTraceId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(traceId);
        try {
            MDC.put(RequestId.MDC_REQ_ID, newId);
            Profiler.start(String.format(FORMAT, newId, name));
            runnable.run();
        } finally {
            Profiler.release();
            if (logger.isInfoEnabled()) {
                logger.info("\n{}\n", Profiler.dump(StringUtils.defaultString(prefix)));
            }
            Profiler.reset();
            MDC.put(RequestId.MDC_REQ_ID, currentTraceId);
        }
    }

    // ---------------------------------------------
    // ---------------------------------------------

    public Runnable runnable(String name, Runnable runnable) {
        return this.runnable(name, TpTraceUtils.currentTraceId(), runnable);
    }

    public Runnable runnable(String name, String traceId, Runnable runnable) {
        final String newId = TpTraceUtils.newTraceId(traceId);
        return () -> {
            final String currentTraceId = TpTraceUtils.currentTraceId();
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                Profiler.start(String.format(FORMAT, newId, name));
                runnable.run();
            } finally {
                Profiler.release();
                if (logger.isInfoEnabled()) {
                    logger.info("\n{}\n", Profiler.dump(StringUtils.defaultString(prefix)));
                }
                Profiler.reset();
                MDC.put(RequestId.MDC_REQ_ID, currentTraceId);
            }
        };
    }

    public <T> Callable<T> callable(String name, Callable<T> callable) {
        return this.callable(name, TpTraceUtils.currentTraceId(), callable);
    }

    public <T> Callable<T> callable(String name, String traceId, Callable<T> callable) {
        final String newId = TpTraceUtils.newTraceId(traceId);
        return () -> {
            final String currentTraceId = TpTraceUtils.currentTraceId();
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                Profiler.start(String.format(FORMAT, newId, name));
                return callable.call();
            } finally {
                Profiler.release();
                if (logger.isInfoEnabled()) {
                    logger.info("\n{}\n", Profiler.dump(StringUtils.defaultString(prefix)));
                }
                Profiler.reset();
                MDC.put(RequestId.MDC_REQ_ID, currentTraceId);
            }
        };
    }

    public <T> Supplier<T> supplier(String name, Supplier<T> supplier) {
        return this.supplier(name, TpTraceUtils.currentTraceId(), supplier);
    }

    public <T> Supplier<T> supplier(String name, String traceId, Supplier<T> supplier) {
        final String newId = TpTraceUtils.newTraceId(traceId);
        return () -> {
            final String currentTraceId = TpTraceUtils.currentTraceId();
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                Profiler.start(String.format(FORMAT, newId, name));
                return supplier.get();
            } finally {
                Profiler.release();
                if (logger.isInfoEnabled()) {
                    logger.info("\n{}\n", Profiler.dump(StringUtils.defaultString(prefix)));
                }
                Profiler.reset();
                MDC.put(RequestId.MDC_REQ_ID, currentTraceId);
            }
        };
    }

    public static TpTraceInterceptor newInstance() {
        return new TpTraceInterceptor();
    }

    public static TpTraceInterceptor newInstance(String prefix) {
        return new TpTraceInterceptor(prefix);
    }

    public static final TpTraceInterceptor DEFAULT = newInstance();

    // ---------------------------------------------

    public static <T> T getByDefault(String name, Supplier<T> supplier) throws Exception {
        return getByDefault(name, TpTraceUtils.currentTraceId(), supplier);
    }

    public static <T> T getByDefault(String name, String traceId, Supplier<T> supplier) throws Exception {
        return get(name, traceId, supplier, DEFAULT);
    }

    public static <T> T get(String name, String traceId, Supplier<T> supplier, TpTraceInterceptor interceptor) throws Exception {
        Objects.requireNonNull(interceptor);
        return interceptor.get(name, traceId, supplier);
    }

    // ---------------------------------------------

    public static <T> T callByDefault(String name, Callable<T> callable) throws Exception {
        return callByDefault(name, TpTraceUtils.currentTraceId(), callable);
    }

    public static <T> T callByDefault(String name, String traceId, Callable<T> callable) throws Exception {
        return call(name, traceId, callable, DEFAULT);
    }

    public static <T> T call(String name, String traceId, Callable<T> callable, TpTraceInterceptor interceptor) throws Exception {
        Objects.requireNonNull(interceptor);
        return interceptor.call(name, traceId, callable);
    }

    // ---------------------------------------------

    public static void runByDefault(String name, Runnable runnable) {
        runByDefault(name, TpTraceUtils.currentTraceId(), runnable);
    }

    public static void runByDefault(String name, String traceId, Runnable runnable) {
        run(name, traceId, runnable, DEFAULT);
    }

    public static void run(String name, String traceId, Runnable runnable, TpTraceInterceptor interceptor) {
        Objects.requireNonNull(interceptor);
        interceptor.run(name, traceId, runnable);
    }

    // ---------------------------------------------

}
