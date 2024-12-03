package com.relic.retry.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

/**
 * 计算工具.
 *
 * @author wxl
 */
@UtilityClass
public class CalculateUtil {

    /**
     * 根据间隔时间以及间隔倍数计算下一次可执行的日期与时间.
     *
     * @param localDateTime 当前的nextRunDateTime
     * @param intervalInMinutes 间隔分钟数
     * @param intervalMultiple 间隔倍数
     * @return 下一次可执行的日期与时间
     */
    public LocalDateTime calculateNextRunDateTime(LocalDateTime localDateTime, Integer intervalInMinutes,
                                                  Double intervalMultiple, Integer retriesNumber) {
        // 为空时，默认为1
        retriesNumber = null == retriesNumber ? 1 : retriesNumber;
        return localDateTime.plusMinutes(new BigDecimal(intervalInMinutes)
                .multiply(new BigDecimal(intervalMultiple).multiply(new BigDecimal(retriesNumber))).longValue());
    }

    /**
     * 计算每次数据锁的持有时间.
     * <p>
     *     <ol>
     *         <li>当任务执行时间为空时（历史数据），赋予最小值</li>
     *         <li>讲任务执行时间乘以3，如果小于最小值，那么返回最小值；如果大于最小值，那么返回任务执行时间乘以3的结果</li>
     *     </ol>
     * </p>
     *
     * @param lastRunDurationMilliSecond 任务执行时间
     * @param minIntervalMilliSecond 锁的持有时间最小值
     * @return
     */
    public long calculateLockInterval(Long lastRunDurationMilliSecond, Long minIntervalMilliSecond) {
        if (null == lastRunDurationMilliSecond) {
            return minIntervalMilliSecond;
        }
        return Math.max(lastRunDurationMilliSecond * 3, minIntervalMilliSecond);
    }

}
