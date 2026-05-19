package com.funny.framework.task.config;

import com.funny.framework.log.common.LogFieldName;
import com.funny.framework.log.tracing.TraceIdGenerator;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class JobMDCAspect {
    private static final Logger logger = LoggerFactory.getLogger(JobMDCAspect.class);
    @Around("@annotation(is)")
    public Object verify(ProceedingJoinPoint pjp, XxlJob is) throws Throwable {
        try {
            String traceId = MDC.get(LogFieldName.TRACE_ID);
            if(StringUtils.isBlank(traceId)){
                MDC.put(LogFieldName.TRACE_ID, TraceIdGenerator.generate());
            }
        }catch (Exception e){
            logger.error("create mdc in job error",e);
        }
        try{
            return pjp.proceed();
        }finally {
            MDC.remove(LogFieldName.TRACE_ID);
        }
    }
}
