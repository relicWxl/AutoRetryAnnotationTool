//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.aop;

import com.alibaba.fastjson.JSONObject;
import com.relic.retry.aop.annotation.RetryJobAnnotation;
import com.relic.retry.pojo.dto.RetryJobTypeDTO;
import com.relic.retry.service.RetryService;
import com.relic.retry.util.SerializationUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class JobAspect {
    private static final Logger log = LoggerFactory.getLogger(JobAspect.class);
    private final RetryService retryService;

    @Autowired
    public JobAspect(RetryService retryService) {
        this.retryService = retryService;
    }

    @Around("@annotation(com.relic.retry.aop.annotation.RetryJobAnnotation) && @annotation(retryJobAnnotation)")
    public Object retryAround(ProceedingJoinPoint point, RetryJobAnnotation retryJobAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = point.proceed(point.getArgs());
            if (result instanceof Boolean) {
                this.retryWhenReturnIsFalse(point, retryJobAnnotation, (Boolean)result, System.currentTimeMillis() - startTime);
            }

            return result;
        } catch (Throwable var6) {
            this.retryWhenThrowException(point, retryJobAnnotation, System.currentTimeMillis() - startTime);
            throw var6;
        }
    }

    private void retryWhenReturnIsFalse(JoinPoint point, RetryJobAnnotation retryJobAnnotation, Boolean result, long durationTime) {
        if (null != result && !result) {
            this.createRetryJob(retryJobAnnotation, this.getType(retryJobAnnotation, point), this.getParam(point), durationTime);
        }

    }

    private void retryWhenThrowException(JoinPoint point, RetryJobAnnotation retryJobAnnotation, long durationTime) {
        this.createRetryJob(retryJobAnnotation, this.getType(retryJobAnnotation, point), this.getParam(point), durationTime);
    }

    private void createRetryJob(RetryJobAnnotation retryJobAnnotation, String type, String param, long durationTime) {
        this.retryService.initRetryJob(retryJobAnnotation, type, param, durationTime);
    }

    private String getType(RetryJobAnnotation retryJobAnnotation, JoinPoint point) {
        if (StringUtils.isNotEmpty(retryJobAnnotation.type())) {
            return retryJobAnnotation.type();
        } else {
            CodeSignature signature = (CodeSignature) point.getSignature();
            RetryJobTypeDTO dto = RetryJobTypeDTO.builder()
                    .className(point.getSignature().getDeclaringTypeName())
                    .methodName(point.getSignature().getName())
                    .paramClassNameList(Arrays.stream(Optional.ofNullable(signature.getParameterTypes()).orElse(new Class[0]))
                            .map(cls -> cls.getName()) // 使用 lambda 表达式
                            .collect(Collectors.toList()))
                    .build();
            return JSONObject.toJSONString(dto);
        }
    }

    private String getParam(JoinPoint point) {
        return SerializationUtil.serialize(point.getArgs());
    }
}
