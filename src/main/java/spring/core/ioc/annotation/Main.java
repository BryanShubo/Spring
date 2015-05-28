package spring.core.ioc.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.core.ioc.annotation.controller.UserController;
import spring.core.ioc.annotation.repository.UserRepository;
import spring.core.ioc.annotation.repository.UserRepositoryImpl;
import spring.core.ioc.annotation.service.UserService;

/**
 * Created by Shubo on 4/11/2015.
 */
public class Main {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("ioc_annotation.xml");

        TestObject to = (TestObject) ctx.getBean("testObject");
        System.out.println(to);

//        UserController uc = (UserController) ctx.getBean("userController");
//        System.out.println(uc);
//        uc.execute();
//
//        UserService us = (UserService) ctx.getBean("userService");
//        System.out.println(us);
//
//        UserRepository ur = (UserRepository) ctx.getBean("userRepository");
//        System.out.println(ur);
    }
}
