package spring.core.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by Shubo on 4/12/2015.
 */
public class CalculatorProxy {

    private Calculator target;

    public CalculatorProxy(Calculator target) {
        this.target = target;
    }

    public Calculator getProxy() {
        Calculator proxy = null;

        // 代理对象由那一个类加载器负责加载
        ClassLoader loader = target.getClass().getClassLoader();
        // 代理对象的类型， 即其中有哪些方法
        Class [] interfaces = new Class[]{Calculator.class};
        // 当调用代理对象其中的方法， 该执行的代码
        InvocationHandler h = new InvocationHandler() {
            /**
             *
             * @param proxy: returning the proxy. Usually, invoke method does not use it. If proxy is used in invoke method, code will trigger an infinite loop.
             * @param method: invoking method
             * @param args: args for invoking method
             * @return
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                // Logging
                System.out.println("The method " + methodName + " begins with " + Arrays.asList(args));
                // Executing method
                Object result = method.invoke(target, args);
                // Logging
                System.out.println("The method " + methodName + " ends with " + result);
                return result;
            }
        };

        proxy = (Calculator)Proxy.newProxyInstance(loader, interfaces, h);

        return proxy;
    }
}
