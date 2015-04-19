package spring.core.aop.impl.xml;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;


public class ValidationAspect {


    public void declareJointPointExpression(){}


    public void validateArgs(JoinPoint joinPoint) {
        System.out.println("validate: " + Arrays.asList(joinPoint.getArgs()));
    }
}
