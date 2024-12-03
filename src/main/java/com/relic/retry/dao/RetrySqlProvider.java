//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.dao;

import com.relic.retry.pojo.consts.StateEnum;
import com.relic.retry.pojo.dto.RetryJobQuery;
import com.relic.retry.pojo.model.RetryJobDO;
import com.relic.retry.util.ConfigUtil;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public final class RetrySqlProvider {
    private static final String ALL_COLUMNS_FOR_SELECT = "c_bh as bh,c_type as type,c_param as param,n_retries_number as retriesNumber, n_max_retries as maxRetries,n_interval_in_minutes as intervalInMinutes,n_interval_multiple as intervalMultiple, dt_job_start_time as jobStartTime,dt_job_end_time as jobEndTime, dt_next_run_date_time as nextRunDateTime,dt_zhgxsj as zhgxsj,dt_cjsj as cjsj, n_last_run_duration_milli_second as lastRunDurationMilliSecond, dt_lock_time as lockTime,lock_milli_second as lockMilliSecond, c_lock_owner_id as lockOwnerId, c_state as state";

    public RetrySqlProvider() {
    }

    public static String list(@Param("lockOwnerId") String lockOwnerId, @Param("currentDateTime") LocalDateTime currentDateTime, @Param("currentDate") String currentDate, @Param("currentTime") String currentTime) {
        return (new SQL() {
            {
                this.SELECT("c_bh as bh,c_type as type,c_param as param,n_retries_number as retriesNumber, n_max_retries as maxRetries,n_interval_in_minutes as intervalInMinutes,n_interval_multiple as intervalMultiple, dt_job_start_time as jobStartTime,dt_job_end_time as jobEndTime, dt_next_run_date_time as nextRunDateTime,dt_zhgxsj as zhgxsj,dt_cjsj as cjsj, n_last_run_duration_milli_second as lastRunDurationMilliSecond, dt_lock_time as lockTime,lock_milli_second as lockMilliSecond, c_lock_owner_id as lockOwnerId, c_state as state");
                this.FROM(ConfigUtil.getTableName() + " retry");
                this.WHERE("(#{currentTime}::time between dt_job_start_time and dt_job_end_time or (dt_job_end_time < dt_job_start_time and #{currentDateTime} between #{currentDate}::date + dt_job_start_time and #{currentDate}::date + dt_job_end_time + interval '1 day'))");
                this.WHERE("#{currentDateTime} > dt_next_run_date_time");
                this.WHERE("n_retries_number < n_max_retries");
                this.WHERE("((c_state = 'NEW' and not exists (select 1 from " + ConfigUtil.getTableName() + " where retry.c_type_hex = c_type_hex and retry.c_param_hex = c_param_hex and c_state <> 'NEW')) or (c_state <> 'NEW' and (c_lock_owner_id is null or c_lock_owner_id = #{lockOwnerId} or #{currentDateTime} > dt_lock_time + lock_milli_second)))");
                this.ORDER_BY("n_order asc");
                this.ORDER_BY("dt_zhgxsj asc");
            }
        }).toString();
    }

    public static String insert(RetryJobDO job) {
        return (new SQL() {
            {
                this.INSERT_INTO(ConfigUtil.getTableName());
                this.VALUES("c_bh", "#{bh}");
                this.VALUES("c_type", "#{type}");
                this.VALUES("c_type_hex", "#{typeHex}");
                this.VALUES("c_param", "#{param}");
                this.VALUES("c_param_hex", "#{paramHex}");
                this.VALUES("n_order", "#{order}");
                this.VALUES("n_retries_number", "#{retriesNumber}");
                this.VALUES("n_max_retries", "#{maxRetries}");
                this.VALUES("n_interval_in_minutes", "#{intervalInMinutes}");
                this.VALUES("n_interval_multiple", "#{intervalMultiple}");
                this.VALUES("dt_job_start_time", "#{jobStartTime}");
                this.VALUES("dt_job_end_time", "#{jobEndTime}");
                this.VALUES("dt_next_run_date_time", "#{nextRunDateTime}");
                this.VALUES("dt_zhgxsj", "#{zhgxsj}");
                this.VALUES("dt_cjsj", "#{cjsj}");
                this.VALUES("n_last_run_duration_milli_second", "#{lastRunDurationMilliSecond}");
                this.VALUES("dt_lock_time", "#{lockTime}");
                this.VALUES("c_lock_owner_id", "#{lockOwnerId}");
                this.VALUES("c_state", "#{state}");
            }
        }).toString();
    }

    public static String deleteByBh(String bh) {
        return (new SQL() {
            {
                this.DELETE_FROM(ConfigUtil.getTableName());
                this.WHERE("c_bh = #{bh}");
            }
        }).toString();
    }

    public static String updateByBh(@Param("job") final RetryJobDO job, @Param("currentDateTime") LocalDateTime currentDateTime) {
        return (new SQL() {
            {
                this.UPDATE(ConfigUtil.getTableName());
                if (job.getRetriesNumber() != null) {
                    this.SET("n_retries_number = #{job.retriesNumber}");
                }

                if (job.getNextRunDateTime() != null) {
                    this.SET("dt_next_run_date_time = #{job.nextRunDateTime}");
                }

                if (job.getZhgxsj() != null) {
                    this.SET("dt_zhgxsj = #{job.zhgxsj}");
                }

                if (job.getState() != null) {
                    this.SET("c_state = #{job.state}");
                }

                if (StringUtils.isNotEmpty(job.getLockOwnerId())) {
                    this.SET("c_lock_owner_id = #{job.lockOwnerId}");
                }

                if (StateEnum.WAITING.equals(job.getState()) || StateEnum.RUNNABLE.equals(job.getState())) {
                    this.WHERE("(c_state = 'NEW' or (c_state <> 'NEW' and (c_lock_owner_id is null or c_lock_owner_id = #{job.lockOwnerId} or #{currentDateTime} > dt_lock_time + lock_milli_second)))");
                }

                this.WHERE("c_bh = #{job.bh}");
            }
        }).toString();
    }

    public static String listByParamAndTypeAndState(RetryJobDO job) {
        return (new SQL() {
            {
                this.SELECT("c_bh as bh,c_type as type,c_param as param,n_retries_number as retriesNumber, n_max_retries as maxRetries,n_interval_in_minutes as intervalInMinutes,n_interval_multiple as intervalMultiple, dt_job_start_time as jobStartTime,dt_job_end_time as jobEndTime, dt_next_run_date_time as nextRunDateTime,dt_zhgxsj as zhgxsj,dt_cjsj as cjsj, n_last_run_duration_milli_second as lastRunDurationMilliSecond, dt_lock_time as lockTime,lock_milli_second as lockMilliSecond, c_lock_owner_id as lockOwnerId, c_state as state");
                this.FROM(ConfigUtil.getTableName());
                this.WHERE("c_param_hex = #{paramHex}");
                this.WHERE("c_type_hex = #{typeHex}");
                this.WHERE("c_state = #{state}");
                this.ORDER_BY("n_order asc");
                this.ORDER_BY("dt_zhgxsj asc");
            }
        }).toString();
    }

    public static String updateLockOwner(@Param("job") RetryJobDO job, @Param("interval") final String interval, @Param("now") LocalDateTime now) {
        return (new SQL() {
            {
                this.UPDATE(ConfigUtil.getTableName());
                this.SET("c_lock_owner_id = #{job.lockOwnerId}");
                this.SET("dt_lock_time = #{now}");
                this.SET("lock_milli_second = " + interval);
                this.SET("c_state = 'RUNNING'");
                this.WHERE("c_bh = #{job.bh}");
                this.WHERE("(c_lock_owner_id = #{job.lockOwnerId} or (c_lock_owner_id is null or #{now} > dt_lock_time + lock_milli_second))");
            }
        }).toString();
    }

    public static String query(@Param("query") final RetryJobQuery query) {
        return (new SQL() {
            {
                this.SELECT("c_bh as bh,c_type as type,c_param as param,n_retries_number as retriesNumber, n_max_retries as maxRetries,n_interval_in_minutes as intervalInMinutes,n_interval_multiple as intervalMultiple, dt_job_start_time as jobStartTime,dt_job_end_time as jobEndTime, dt_next_run_date_time as nextRunDateTime,dt_zhgxsj as zhgxsj,dt_cjsj as cjsj, n_last_run_duration_milli_second as lastRunDurationMilliSecond, dt_lock_time as lockTime,lock_milli_second as lockMilliSecond, c_lock_owner_id as lockOwnerId, c_state as state");
                this.FROM(ConfigUtil.getTableName());
                if (StringUtils.isNotEmpty(query.getType())) {
                    this.WHERE("c_type like concat('%', #{query.type}, '%')");
                }

                if (null != query.getRetriesNumber()) {
                    this.WHERE("n_retries_number = #{query.retriesNumber}");
                }

                if (null != query.getCjsjKs()) {
                    this.WHERE("dt_cjsj > #{query.cjsjKs}");
                }

                if (null != query.getCjsjJs()) {
                    this.WHERE("dt_cjsj > #{query.cjsjJs}");
                }

            }
        }).toString();
    }
}
