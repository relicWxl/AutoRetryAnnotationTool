package com.relic.retry.pojo.dto;

import com.relic.retry.service.IRetryService;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 初始化RetryJob的参数封装对象.
 *
 * @author wxl
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InitRetryJobParam {

    /**
     * 参数实例数组.
     * <p>每个实例都需要实现{@link java.io.Serializable}接口</p>
     */
    private Object[] paramObjects;

    /**
     * 最后一次任务持续时间.
     * <p>单位为毫秒，记录此时间是为了动态变更数据锁的持有时间，避免出现同一任务并行执行的问题</p>
     * <p>上一次任务执行时间，这是因为retry有锁失效时间设定，但还没有增加锁续约设定，所以锁的持有时间是通过上一次任务执行时间来计算的，第一次初始化时需要自行评估你{@link IRetryService}
     * 实现类的执行时间，在此基础上建议乘以5作为上一次任务执行时间</p>
     */
    private Long lastRunDurationMilliSecond;

    /**
     * 任务类型标识，用以区分不同的业务，全局唯一.
     * <p>手动模式下，必须要有type值，且需要保证type值与{@link IRetryService#getType()}方法返回的值一致</p>
     */
    private String type;
    /**
     * 任务开始时间。
     * <p>
     * 对于特别的业务，可能需要指定任务的开始时间范围，如发送短信的业务，仅允许在早上八点到下午五点发送。
     */
    @Builder.Default
    private LocalTime jobStartTime = LocalTime.MIN;
    /**
     * 任务结束时间。
     * <p>
     * 对于特别的业务，可能需要指定任务的结束时间范围，如发送短信的业务，仅允许在早上八点到下午五点发送。
     */
    @Builder.Default
    private LocalTime jobEndTime = LocalTime.MAX;
    /**
     * 任务的排序。 默认情况下先按此字段大小顺序进行排序，再按照提交的时间顺序进行排序。
     */
    @Builder.Default
    private Integer order = Integer.MAX_VALUE;
    /**
     * 最大重试次数 <br/>
     * 默认值10次，可根据不同的业务场景自行定义
     */
    @Builder.Default
    private Integer maxRetries = 10;
    /**
     * 间隔重试时间（单位：分钟）.
     * <p>
     * 默认为5分钟
     * </p>
     */
    @Builder.Default
    private Integer intervalInMinutes = 5;
    /**
     * 间隔倍数.
     * <p>
     * 默认间隔重试时间为5分钟，间隔倍数为2<br/>
     * 设当前时间为x，则第一次重试间隔为 5*2=10 ，即x+10分钟之后;<br/>
     * 第二次重试时间为 5*2*2=20 ，即x+10+20分钟之后；<br/>
     * 那么第三次重试时间为 10*2*2*2=40 ，即x+10+20+40分钟之后，以此类推。<br/>
     * 那么到默认重试次数10时，为 x+5*2^10*2 分钟之后，换算下来，整个重试周期约为7.2天（这是假定jobStartTime与jobEndTime为默认值的情况下），可适用于大多数场景.
     * </p>
     */
    @Builder.Default
    private Double intervalMultiple = 2d;

}
