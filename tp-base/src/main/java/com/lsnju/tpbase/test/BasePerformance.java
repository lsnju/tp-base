package com.lsnju.tpbase.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.log.TpTraceInterceptor;
import com.lsnju.tpbase.test.vo.PerfResult;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2025/11/15 11:37
 * @version V1.0
 */
@Slf4j
public class BasePerformance {

    public static <T> PerfResult perfTest(List<List<T>> groupTask, Consumer<T> consumer) throws Exception {
        final int threadSize = groupTask.size();
        final ExecutorService executor = Executors.newFixedThreadPool(threadSize);
        final CountDownLatch ready = new CountDownLatch(threadSize);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threadSize);

        List<CompletableFuture<PerfResult>> allFuture = new ArrayList<>();
        for (int i = 0; i < threadSize; i++) {
            final List<T> jobList = groupTask.get(i);
            final String name = String.format("task_%02d", i);
            Supplier<PerfResult> supplier = TpTraceInterceptor.DEFAULT.supplier(name, call(name, jobList, consumer, ready, start, done));
            allFuture.add(CompletableFuture.supplyAsync(supplier, executor));
        }
        ready.await();
        start.countDown();
        final long startTime = System.nanoTime();
        done.await();
        final long emdTime = System.nanoTime();
        final long totalCost = (emdTime - startTime) / DigestConstants.MS_SCALE;

        List<PerfResult> subList = allFuture.stream().map(CompletableFuture::join).collect(Collectors.toList());
        final PerfResult ret = new PerfResult();
        ret.setSubList(subList);
        ret.setTotalCount(groupTask.stream().mapToInt(List::size).sum());
        ret.setTotalCost(totalCost);
        //
        executor.shutdown();
        return ret;
    }

    public static <T> Supplier<PerfResult> call(String name, List<T> list, Consumer<T> consumer,
                                                CountDownLatch ready, CountDownLatch start, CountDownLatch done) {
        return () -> {
            final PerfResult ret = new PerfResult();
            ret.setName(name);
            try {
                ready.countDown();
                start.await();
                final long startTime = System.nanoTime();
                for (T item : list) {
                    consumer.accept(item);
                }
                long end = System.nanoTime();
                long total = (end - startTime) / DigestConstants.MS_SCALE;
                done.countDown();

                ret.setSuccess(true);
                ret.setTotalCost(total);
                ret.setTotalCount(list.size());
                return ret;
            } catch (Exception e) {
                log.error(String.format("%s", e.getMessage()), e);
                ret.setSuccess(false);
                return ret;
            }
        };
    }

}
