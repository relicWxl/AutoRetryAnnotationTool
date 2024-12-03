package com.relic.retry.pojo.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * retry job 页面请求参数.
 * <p>主要是针对新增的任务类型中，使用了Java的序列化，导致数据库中的值没有意义，需要做反序列化才可以展示</p>
 *
 * @author wxl
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RetryJobQuery extends PageQuery {

    /**
     * 任务类型.
     */
    private String type;
    /**
     * 已经重试的次数.
     */
    private Integer retriesNumber;
    /**
     * 创建时间开始.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cjsjKs;
    /**
     * 创建时间结束.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cjsjJs;

    /**
     * 参数.
     * <p>存内存匹配</p>
     */
    private String param;

}
