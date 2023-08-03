package com.lsnju.tpbase.daemon.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lisong
 * @since 2020-01-20 14:48:06
 * @version V1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskCountContext {

    private static final ThreadLocal<Long> TASK_SIZE = new ThreadLocal<>();
    private static final ThreadLocal<Long> TOTAL_SIZE = new ThreadLocal<>();

    public static long getTotalSize() {
        return TOTAL_SIZE.get() == null ? 0L : TOTAL_SIZE.get();
    }

    public static void setTotalSize(long total) {
        TOTAL_SIZE.set(total);
    }

    public static long getTaskSize() {
        return TASK_SIZE.get() == null ? 0L : TASK_SIZE.get();
    }

    public static void setTaskSize(long taskSize) {
        TASK_SIZE.set(taskSize);
    }

    public static void clean() {
        TASK_SIZE.remove();
        TOTAL_SIZE.remove();
    }
}
