package spring.core.aop.impl.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Shubo on 4/12/2015.
 */
public class Main {

    public static void main(String[] args){

        ApplicationContext ctx = new ClassPathXmlApplicationContext("aop_impl_annotation.xml");

        Calculator calculator = ctx.getBean(Calculator.class);

        int add = calculator.add(3,7);
        System.out.println("add " + add);

        int mul = calculator.mul(4, 6);
        System.out.println("mul " + mul);

        int div = calculator.div(13, 4);
        System.out.println("div " + div);

        // Testing that after event always executes even target method throws an exception
       /* int div1 = calculator.div(12, 0);
        System.out.println("div " + div1);*/

        // Testing afterReturn event
        int div2 = calculator.div(12, 5);
        System.out.println("div " + div2);
    }
}
