package cn.springcloud.gray.zuul.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

//@Component
@Aspect
public class GrayPerformanceAspect {


    @Pointcut("execution(* cn.springcloud.gray..*.*(..))")
    public void pointCut() {

    }


    @Around("pointCut()")
    public Object executeAround(ProceedingJoinPoint joinpoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinpoint.proceed();
        } finally {
//            System.out.println(joinpoint.getSignature().getDeclaringTypeName() + "#" + joinpoint.getSignature().getName() + (System.currentTimeMillis() - start));
            PerformanceLogger.printMethodUsedTime(joinpoint.toLongString(), (System.currentTimeMillis() - start));
        }
    }

}
