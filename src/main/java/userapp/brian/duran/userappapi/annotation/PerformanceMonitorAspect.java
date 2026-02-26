package userapp.brian.duran.userappapi.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class PerformanceMonitorAspect {

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // proceed() continúa con lo que se iba a hacer
        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info("⏱️ PERFORMANCE: El método [{}] tardó [{} ms] en ejecutarse.",
                joinPoint.getSignature().getName(),
                executionTime);

        return result;
    }
}
