package spring.core.aop.impl.annotation;

import org.springframework.stereotype.Component;

/**
 * Created by Shubo on 4/11/2015.
 */

public interface CalculatorAOP {
    public int add(int i, int j);

    public int sub(int i, int j);

    public int mul(int i, int j);

    public int div(int i, int j);
}
