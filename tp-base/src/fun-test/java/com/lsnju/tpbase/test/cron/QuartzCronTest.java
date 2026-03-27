package com.lsnju.tpbase.test.cron;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;

import com.lsnju.base.util.TpDateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2026-03-27 13:51
 * @version V1.0
 */
@Slf4j
public class QuartzCronTest {

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
            CronExpression cronExpression = new CronExpression(cronExpressionStr);

            // 2. 计算下一次执行时间
            // 从当前时间之后开始计算
            Date nextExecutionTime = cronExpression.getNextValidTimeAfter(new Date());

            // 3. (推荐) 指定时区进行计算，避免生产环境问题
            cronExpression.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Date nextExecutionTimeInShanghai = cronExpression.getNextValidTimeAfter(new Date());

            System.out.println("原始表达式: " + cronExpressionStr);
            System.out.println("下一次执行时间 (系统默认时区)   : " + TpDateUtils.toZoneDateTime(nextExecutionTime));
            System.out.println("下一次执行时间 (Asia/Shanghai): " + TpDateUtils.toZoneDateTime(nextExecutionTimeInShanghai));

        } catch (ParseException e) {
            System.err.println("Cron表达式格式错误: " + e.getMessage());
        }
    }


}
