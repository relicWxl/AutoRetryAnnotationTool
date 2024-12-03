package com.relic.retry.pojo.model;

import com.relic.retry.pojo.consts.StateEnum;
import com.relic.retry.service.IRetryService;
import com.relic.retry.util.MD5Util;
import com.relic.retry.util.SerializationUtil;
import com.relic.retry.aop.annotation.RetryJobAnnotation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * THttpRetryJob
 * <p>
 * Http重试任务表对应的实体
 *
 * @author wxl
 * @version v1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RetryJobDO {

    /**
     * 主键 编号
     */
    private String bh;

    /**
     * 任务类型标识，用以区分不同的业务，全局唯一.
     * <p>包含两种情况：</p>
     * <ol>
     *     <li>经过AOP拦截自动生成的type</li>
     *     <li>手动维护的type，与{@link IRetryService#getType()}保持一致</li>
     * </ol>
     */
    private String type;

    /**
     * 任务类型的hex值，方便对任务去重.
     * <p>
     * 详见{@link MD5Util#encrypt(String)}}
     * </p>
     */
    private String typeHex;

    /**
     * 参数，经{@link SerializationUtil#serialize(Serializable)} 序列化为base64编码后的字符串.
     */
    private String param;

    /**
     * 参数的hex值，方便对任务去重.
     * <p>
     * 详见{@link MD5Util#encrypt(String)}}
     * </p>
     */
    private String paramHex;

    /**
     * 顺序
     */
    private Integer order;

    /**
     * 已经重试的次数
     */
    @Builder.Default
    private Integer retriesNumber = 0;

    /**
     * 限制的最大重试次数
     */
    private Integer maxRetries;

    /**
     * 间隔重试时间（单位：分钟）.
     */
    private Integer intervalInMinutes;

    /**
     * 间隔重试倍数.
     */
    private Double intervalMultiple;

    /**
     * 任务的开始时间
     */
    private LocalTime jobStartTime;

    /**
     * 任务的结束时间
     */
    private LocalTime jobEndTime;

    /**
     * 下一次可运行的时间.
     * <p>
     * 通过{@link RetryJobAnnotation#intervalInMinutes()}以及{@link RetryJobAnnotation#intervalMultiple()}
     * 计算而来，重试任务的日期与时间需要晚于{@link #nextRunDateTime}且时间保持在
     * {@link #jobStartTime} 和{@link #jobEndTime}的区间内
     * </p>
     */
    private LocalDateTime nextRunDateTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime zhgxsj;

    /**
     * 创建时间
     */
    private LocalDateTime cjsj;

    /**
     * 最后一次任务持续时间.
     * <p>单位为毫秒，记录此时间是为了动态变更数据锁的持有时间，避免出现同一任务并行执行的问题</p>
     */
    private Long lastRunDurationMilliSecond;

    /**
     * 锁的时间戳.
     * <p>当前加锁的时间，年月日十分秒</p>
     */
    private LocalDateTime lockTime;

    /**
     * 锁的持有ID.
     * <p>每一个retry实例都会有一个随机的uuid.</p>
     * <p>如果实例部署在k8s中，那么每一次漂移之后，lockOwnerId会刷新，此时只能等数据的锁超时，才可以拿到</p>
     */
    private String lockOwnerId;

    /**
     * 任务状态.
     */
    private StateEnum state;

}
