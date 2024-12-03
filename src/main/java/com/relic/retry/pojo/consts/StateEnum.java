package com.relic.retry.pojo.consts;

import com.relic.retry.service.RetryService;

/**
 * retry job任务的状态枚举.
 * <p>码值参考了Java线程状态，但实际含义在一些细节上有所区别，可以通过对应的Javadoc获取详细信息</p>
 * <ul>
 *     <li>{@link #NEW}：新建，当一条数据库记录被创建时，其数据的状态就是NEW状态</li>
 *     <li>{@link #NEW} --> {@link #WAITING}：数据从数据库中加载到了线程池的阻塞队列中</li>
 *     <li>{@link #WAITING} --> {@link #RUNNABLE}：数据从线程池的阻塞队列中被取出，并且已获取到CPU资源，在真正的retry任务执行之前</li>
 * </ul>
 *
 * @author wxl
 */
public enum StateEnum {

    /**
     * 新建，没有加入到线程池的阻塞队列中.
     * <p>当一条数据库记录被创建时，其数据的状态就是NEW状态</p>
     */
    NEW,
    /**
     * 等待中，已加入到线程池阻塞队列中.
     * <p>当一条数据从数据库中加载到了线程池的阻塞队列中时，其数据状态由{@link #NEW}变为WAITING</p>
     * <p>⚠️ 数据处于WAITING状态时，可能会因为系统关机导致任务状态没有被更新，所以在获取数据时{@link RetryService#listAllJob(String)}会允许将WAITING状态的数据再次加入到线程池的阻塞队列中</p>
     */
    WAITING,
    /**
     * 运行中，此任务正在运行.
     * <p>当一条数据从线程池的阻塞队列中被取出并执行之前，其数据状态由{@link #WAITING}变为RUNNABLE</p>
     * <p>⚠️ 数据处于RUNNABLE状态时，可能会因为系统关机导致任务状态没有被更新，所以在获取数据时{@link RetryService#listAllJob(String)}会允许将RUNNABLE状态的数据再次加入到线程池的阻塞队列中</p>
     */
    RUNNABLE,

    /**
     * 正在运行中.
     */
    RUNNING,
    /**
     * 终止.
     * <p>就目前retry设定是成功以后就会删掉任务记录以减少数据库表大小，所以目前终止状态是不存在的</p>
     */
    TERMINATED

}
