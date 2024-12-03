package com.relic.retry.dao;

import com.relic.retry.pojo.dto.RetryJobQuery;
import com.relic.retry.pojo.model.RetryJobDO;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

/**
 * RetryMapper
 * <p>
 * Retry表 CRUD实现
 *
 * @author wxl
 * @version v1.0.0
 */
@Repository
public interface RetryMapper {

    /**
     * 往HttpRetry重试任务表中插入数据
     *
     * @param job http重试任务
     */
    @InsertProvider(type = RetrySqlProvider.class, method = "insert")
    int insert(RetryJobDO job);

    @DeleteProvider(type = RetrySqlProvider.class, method = "deleteByBh")
    int deleteByBh(String bh);

    @SelectProvider(type = RetrySqlProvider.class, method = "list")
    List<RetryJobDO> list(@Param("lockOwnerId") String lockOwnerId,
                          @Param("currentDateTime") LocalDateTime currentDateTime,
                          @Param("currentDate") String currentDate, @Param("currentTime") String currentTime);

    @UpdateProvider(type = RetrySqlProvider.class, method = "updateByBh")
    int updateByBh(@Param("job") RetryJobDO job, @Param("currentDateTime") LocalDateTime currentDateTime);

    /**
     * 根据参数和URL检查当前任务是否已经存在，避免循环插入job
     *
     * @param job 任务
     * @return 参数地址
     */
    @SelectProvider(type = RetrySqlProvider.class, method = "listByParamAndTypeAndState")
    List<RetryJobDO> listByParamAndTypeAndState(RetryJobDO job);

    @UpdateProvider(type = RetrySqlProvider.class, method = "updateLockOwner")
    boolean tryLock(@Param("job") RetryJobDO job, @Param("interval") String interval, @Param("now") LocalDateTime now);

    @SelectProvider(type = RetrySqlProvider.class, method = "query")
    List<RetryJobDO> query(@Param("query") RetryJobQuery retryJobQuery);

}
