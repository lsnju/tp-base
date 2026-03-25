package com.lsnju.tpbase.daemon;

import java.util.function.BooleanSupplier;

/**
 *
 * @author lisong
 * @since 2026-03-25 13:31
 * @version V1.0
 */
public class TpTaskWrapper extends AbstractTask {

    private final BooleanSupplier taskChecker;
    private final Runnable task;

    public TpTaskWrapper(Runnable task) {
        this(task, null);
    }

    public TpTaskWrapper(Runnable task, BooleanSupplier taskChecker) {
        this.taskChecker = taskChecker;
        this.task = task;
    }

    @Override
    public void execute() {
        task.run();
    }

    @Override
    public boolean isTaskDisable() {
        if (taskChecker != null) {
            return taskChecker.getAsBoolean();
        }
        return super.isTaskDisable();
    }

}
