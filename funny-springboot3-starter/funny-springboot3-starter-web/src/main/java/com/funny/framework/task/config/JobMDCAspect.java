package com.funny.framework.task.config;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//            String traceId = MDC.get(FieldName.TRACE_ID);
//            if(StringUtils.isBlank(traceId)){
//                MDC.put(FieldName.TRACE_ID, LogTraceUtil.getNewTraceId());
//            }
        }catch (Exception e){
            logger.error("create mdc in job error",e);
        }
        try{
            return pjp.proceed();
        }finally {
//            MDC.remove(FieldName.TRACE_ID);
        }
    }
}
