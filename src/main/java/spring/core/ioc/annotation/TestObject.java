package spring.core.ioc.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Shubo on 4/11/2015.
 */
@Component
public class TestObject {

    @Autowired
    @Qualifier("Test1Object")
    private Test1Object test1Object = new Test1Object();

    @Autowired
    @Qualifier("Test2Object")
    private Test2Object test2Object = new Test2Object();


    TestObject() {
        System.out.println(this.test1Object);

        System.out.println(this.test2Object);

        System.out.println("TestObject-------");

        System.out.println(test1Object.getX());

        System.out.println(test2Object.getX());
    }


}
