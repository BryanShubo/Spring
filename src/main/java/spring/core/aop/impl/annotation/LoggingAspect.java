package spring.core.aop.impl.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 1. This aspect should be annotated as both component and aspect
 */

@Order(2)
@Component
@Aspect
public class LoggingAspect {

    @Before("execution(public int spring.core.aop.impl.annotation.CalculatorAOP.*(int, int))")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method " + methodName + " is starting with " + args);
    }

    // 1.This event always executes even the method throws an exception.
    // 2. This after event cannot access the target method result because the target method may
    //    throw an exception
    @After("execution(* spring.core.aop.impl.annotation.CalculatorAOP.*(int, int))")
    public void afterMethod(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method " + methodName + " is ending with " + args);
    }

    // 1. This after event can access the target method result.
    @AfterReturning(value = "execution(* spring.core.aop.impl.annotation.CalculatorAOP.*(int, int))", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method " +  methodName + " ends with " +  result);
    }

    // 1. This event will execute when target method throws an exception
    @AfterThrowing(value = "execution(* spring.core.aop.impl.annotation.CalculatorAOP.*(int, int))", throwing = "ex")
    public void afterReturningMethod(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method " +  methodName + " occurs exception: " +  ex);
    }

    // Requires ProceedingJoinPoint args
    // it is similar to proxy that ProceedingJoinPoint args can decide weather execute the target method
    // Return is required and the result is the target method return.
    @Around(value = "execution(* spring.core.aop.impl.annotation.CalculatorAOP.*(..))")
    public Object aroundMethod(ProceedingJoinPoint pjd) {

        Object result = null;
        String methodName = pjd.getSignature().getName();
        try {
            // Before
            System.out.println("Before method: ");
            result = pjd.proceed();
            //AfterReturning
            System.out.println("After method: ");
        } catch (Throwable throwable) {
            // Exception event
            System.out.println("Exception occurs : ");
            throwable.printStackTrace();
        }
        //After event
        System.out.println("After method---");
        return result;
    }
}
