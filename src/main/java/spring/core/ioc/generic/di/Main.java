package spring.core.ioc.generic.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * Created by Shubo on 4/11/2015.
 */
public class Main {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("ioc_generic-di.xml");

        UserService us = (UserService) ctx.getBean("userService");
        us.add();
    }
}
