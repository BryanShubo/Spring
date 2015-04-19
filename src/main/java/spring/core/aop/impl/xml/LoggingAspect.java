package spring.core.aop.impl.xml;

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


public class LoggingAspect {


    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method " + methodName + " is starting with " + args);
    }


    public void afterMethod(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method " + methodName + " is ending with " + args);
    }


    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method " +  methodName + " ends with " +  result);
    }


    public void afterThrowingMethod(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method " +  methodName + " occurs exception: " +  ex);
    }


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
