package com.relic.retry.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.relic.retry.pojo.consts.Const;
import com.relic.retry.aop.annotation.RetryJobAnnotation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * retry job 视图对象.
 * <p>主要是针对新增的任务类型中，使用了Java的序列化，导致数据库中的值没有意义，需要做反序列化才可以展示</p>
 *
 * @author wxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryJobVO {

    /**
     * 主键 编号
     */
    private String bh;

    /**
     * 任务类型
     */
    private String type;

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
    @DateTimeFormat(pattern = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    @JSONField(format = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    @JsonFormat(pattern = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    private LocalDateTime nextRunDateTime;

    /**
     * 最后更新时间
     */
    @DateTimeFormat(pattern = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    @JSONField(format = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    @JsonFormat(pattern = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    private LocalDateTime zhgxsj;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    @JSONField(format = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    @JsonFormat(pattern = Const.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    private LocalDateTime cjsj;

    /**
     * 经Java反序列化后的参数.
     */
    private Object[] params;

}
