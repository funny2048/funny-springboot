package com.funny.framework.task.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.funny.framework.log.tracing.TraceContext;
import com.xxl.job.core.handler.annotation.XxlJob;

@Aspect
@Component
@Order(1)
public class JobMDCAspect {
    private static final Logger logger = LoggerFactory.getLogger(JobMDCAspect.class);
    @Around("@annotation(is)")
    public Object verify(ProceedingJoinPoint pjp, XxlJob is) throws Throwable {
        try {
            TraceContext.ensureContext();
        }catch (Exception e){
            logger.error("create mdc in job error",e);
        }
        try{
            return pjp.proceed();
        }finally {
            TraceContext.clearContext();
        }
    }
}
