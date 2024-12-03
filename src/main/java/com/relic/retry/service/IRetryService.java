package com.relic.retry.service;

import com.relic.retry.pojo.dto.InitRetryJobParam;
import com.relic.retry.pojo.model.RetryJobDO;
import com.relic.retry.util.SerializationUtil;

/**
 * IRetryJob
 * <p>
 * 重试任务接口
 * <p>
 * 主要用途是统一系统内部所有需要重试的任务
 *
 * @author wxl
 * @version v1.0.0
 */
public interface IRetryService {

    /**
     * 获取当前的重试任务类型标识
     *
     * @return 重试任务类型标识
     */
    String getType();

    /**
     * 任务重试.
     * <ol>
     *     <li>1.0.0版初期，retry有支持基于fastjson序列化的数据保存方案，但在2.0.0版本开始就去掉了这个方案</li>
     *     <li>从v3.3.0版本开始，retry为了支援业务上想要复用retry持久化任务表的需求，增加了手动创建retry
     *     任务的方法{@link RetryService#initRetryJob(Object[], String, Long)} 和
     *     {@link RetryService#initRetryJob(InitRetryJobParam)}</li>
     * </ol>
     * <p>如果你的任务是通过{@link RetryService#initRetryJob(Object[], String, Long)} 或
     * {@link RetryService#initRetryJob(InitRetryJobParam)}方法做的初始化，那么你需要用到
     * {@link SerializationUtil#deserialize(String)}方法对
     * {@link RetryJobDO#getParam()}反序列化，你会得到{@code Object[]}数组，这个数组顺序与你初始化时的顺序一致，你可以根据数据下标获取参数并实现强转。</p>
     * <h3>示例：</h3>
     * <pre>
     * {@code
     * // 要放到Spring IOC容器中，不需要声明为public，也不建议声明为public，要尽量控制访问权限
     * @Component
     * class YourRetryServiceImpl implements IRetryService {
     *     @Override
     *     public String getType() {
     *         // 任务标识需要全局唯一，建议使用全拼表示，要让人一眼就能看懂你这个任务是做啥的
     *         return "dzda_ji_suan_fu_jian_ye_shu_dang_fu_jian_shang_chuan";
     *     }
     *
     *     @Override
     *     public boolean retry(RetryJobDO job) {
     *         // 不要使用Map JSONObject这类对象，要新建POJO，并且POJO需要实现java.io.Serializable接口
     *         YourParam1Class yourParam1Instant  = (YourParam1Class) SerializationUtil.deserialize(job.getParam())[0];
     *         YourParam2Class yourParam2Instant  = (YourParam2Class) SerializationUtil.deserialize(job.getParam())[1];
     *         // 这里写上你需要实现的业务逻辑
     *         ...
     *         // 如果处理成功就返回true，失败就返回false或者抛出异常均可
     *         return false;
     *     }
     * }
     * }
     * </pre>
     *
     * @param job 重试任务存储信息
     * @return 任务执行是否成功，成功返回true，否则返回false
     */
    boolean retry(RetryJobDO job) throws Exception;

}
