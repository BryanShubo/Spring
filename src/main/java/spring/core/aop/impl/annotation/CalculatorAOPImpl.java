package spring.core.aop.impl.annotation;

import org.springframework.stereotype.Component;

/**
 * Created by Shubo on 4/11/2015.
 */
@Component
public class CalculatorAOPImpl implements CalculatorAOP {
    @Override
    public int add(int i, int j) {
        int result = i + j;
        return result;
    }

    @Override
    public int sub(int i, int j) {
        int result = i - j;
        return result;
    }

    @Override
    public int mul(int i, int j) {
        int result = i * j;
        return result;
    }

    @Override
    public int div(int i, int j) {
        int result = i / j;
        return result;
    }
}
