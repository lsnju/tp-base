package com.lsnju.tpbase.test.cron;

import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.support.CronExpression;

import com.lsnju.base.util.TpDateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2026-03-27 14:01
 * @version V1.0
 */
@Slf4j
public class SpringCronTest {

    @Test
    void test_001() {
        // 每天中午12点执行
        testCron("0 0 12 * * ?");
        //
        testCron("0 00 20 ? * * *");
        //
        testCron("0 0 14 1-24 * ?");
        testCron("0 0 23 25-26 * ?");
    }

    private static void testCron(String cronExpressionStr) {
        try {
            // 1. 创建 CronExpression 实例，这一步会自动校验表达式合法性
            // 如果表达式非法，会抛出 ParseException
            CronExpression cronExpression = CronExpression.parse(cronExpressionStr);

            // 2. 计算下一次执行时间
            // 从当前时间之后开始计算
            ZonedDateTime nextExecutionTime = cronExpression.next(TpDateUtils.toZoneDateTime(new Date()));

            System.out.println("原始表达式: " + cronExpressionStr);
            System.out.println("下一次执行时间 (系统默认时区): " + nextExecutionTime);

        } catch (Exception e) {
            System.err.println("Cron表达式格式错误: " + e.getMessage());
        }
    }
}
