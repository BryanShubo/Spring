package spring.core.ioc.annotation;

import org.springframework.stereotype.Component;

/**
 * Created by uc0006 on 5/28/2015.
 */
@Component("Test2Object")
public class Test2Object {

    private int x;
    Test2Object() {
        this.x = 20;
        System.out.println("Test2Object-------");
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
