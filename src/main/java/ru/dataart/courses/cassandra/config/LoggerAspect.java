package ru.dataart.courses.cassandra.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Before("execution(* ru.dataart.courses.cassandra.web.CassandraRestApi.*)")
    public void restApiLogging(JoinPoint joinPoint){
        logger.info("Executing REST API method:{}", joinPoint.getSignature());
    }

}
