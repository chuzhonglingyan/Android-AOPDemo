package com.yuntian.aoplib.aspect;

import com.yuntian.aoplib.aspect.trace.DebugLog;
import com.yuntian.aoplib.aspect.trace.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TraceAspect {

    //注解作用的方法
    private static final String POINTCUT_METHOD =
            "execution(@com.yuntian.aoplib.annotation.DebugTrace * *(..))";

    //注解作用的构造方法
    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.yuntian.aoplib.annotation.DebugTrace *.new(..))";

    //方法切入点
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugTrace() {}

    //构造方法切入点
    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedDebugTrace() {}

    // @Around:注入到class文件中的代码
    @Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//获得方法签名
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();  //方法执行之前
        Object result = joinPoint.proceed();  //连接点: 一个方法调用或者方法入口。
        stopWatch.stop(); //方法执行之后

        DebugLog.log(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));

        return result;

    }


    /**
     * Create a log message.
     *
     * @param methodName A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("Gintonic --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }

}
