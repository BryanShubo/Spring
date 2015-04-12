package spring.core.aop.without;

/**
 * Created by Shubo on 4/11/2015.
 */
public class CalculatorImpl implements Calculator {
    @Override
    public int add(int i, int j) {
        System.out.println("start---");
        int result = i + j;
        System.out.println("end---");
        return result;
    }

    @Override
    public int sub(int i, int j) {
        System.out.println("start---");
        int result = i - j;
        System.out.println("end---");
        return result;
    }

    @Override
    public int mul(int i, int j) {
        System.out.println("start---");
        int result = i * j;
        System.out.println("end---");
        return result;
    }

    @Override
    public int div(int i, int j) {
        System.out.println("start---");
        int result = i / j;
        System.out.println("end---");
        return result;
    }
}
