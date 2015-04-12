package spring.core.aop.proxy;

/**
 * Created by Shubo on 4/11/2015.
 */
public class Main {
    public static void main(String[] args) {
        Calculator target = new CalculatorImpl();
        Calculator proxy = new CalculatorProxy(target).getProxy();

        int add = proxy.add(1, 4);
        int mul = proxy.mul(2, 6);

        System.out.println(add + " : " + mul);
    }
}
