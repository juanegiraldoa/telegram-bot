package org.telegram.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerUtils {
    public static void executeAfterSeconds(Runnable runnable, TimeUnit timeUnit, int time) {
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        ex.schedule(runnable, time, timeUnit);
    }

    public static void executeAfterSeconds(Callable<?> callable, TimeUnit timeUnit, int time) {
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        ex.schedule(callable, time, timeUnit);
    }
}
