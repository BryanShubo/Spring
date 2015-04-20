package spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Shubo on 4/19/2015.
 */
@Controller
public class HelloWorld {

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello shubo");

        return "hello";
    }

    @RequestMapping("/index")
    public String index(){
        System.out.println("Index page");
        return "index";
    }
}
