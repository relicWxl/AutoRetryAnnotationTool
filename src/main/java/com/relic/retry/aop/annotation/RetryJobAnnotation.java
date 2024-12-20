package com.relic.retry.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.relic.retry.service.IRetryService;
/**
 *    注解使用说明
 *     @RetryJobAnnotation(intervalMultiple=1.2d, order=500, intervalInMinutes=1)
 *     public boolean updateTest(TestParam testParam) {
 *         //需要执行的内容
 *         //如果出现错误，会自动记录到t_retry表中，自动重试
 *     }
 */

/**
 * RetryJob
 * <p>
 * 重试job注解 借助于此注解可当请求失败，或请求返回结果集不符合业务时进行重试
 *
 * @author wxl
 * @version v1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryJobAnnotation {

    /**
     * HTTP RETRY JOB类型，用以区分不同的业务.
     * <p>
     * 目前支持两个模式：<br/>
     * 1. 默认为空时，将会获取当前的类名和方法名作作为类型，后续会通过Spring上下文获取代理类，再通过反射调用对应的方法<br/>
     * 2. 配置{@link IRetryService#getType()}方法返回的值，并自己实现{@link IRetryService}，此方案适合用于解决部分业务中的`ABA`问题
     * </p>
     */
    String type() default "";

    /**
     * 任务开始时间。
     * <p>
     * 对于特别的业务，可能需要指定任务的开始时间范围，如发送短信的业务，仅允许在早上八点到下午五点发送。
     */
    String jobStartTime() default "00:00:00";

    /**
     * 任务结束时间。
     * <p>
     * 对于特别的业务，可能需要指定任务的结束时间范围，如发送短信的业务，仅允许在早上八点到下午五点发送。
     */
    String jobEndTime() default "23:59:59";

    /**
     * 任务的排序。 默认情况下先按此字段大小顺序进行排序，再按照提交的时间顺序进行排序。
     */
    int order() default Integer.MAX_VALUE;

    /**
     * 最大重试次数 <br/>
     * 默认值10次，可根据不同的业务场景自行定义
     */
    int maxRetries() default 10;

    /**
     * 间隔重试时间（单位：分钟）.
     * <p>
     * 默认为5分钟
     * </p>
     */
    int intervalInMinutes() default 5;

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
    double intervalMultiple() default 2d;

}
