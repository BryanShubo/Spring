package spring.core.ioc.annotation;

import org.springframework.stereotype.Component;

/**
 * Created by uc0006 on 5/28/2015.
 */
@Component("Test1Object")
public class Test1Object {

    private int x;

    Test1Object() {
        this.x = 10;
        System.out.println("Test1Object--------------");
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
