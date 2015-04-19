package spring.core.ioc.beans;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        // 0.  Not Spring
        HelloWorld hi = new HelloWorld();
        hi.setUser("Tom");
        hi.hello();

        //1. Create Spring IOC container object
        // ApplicationContext is an interface, ClassPathXmlApplicationContext is an implementation class
        //ApplicationContext ctx = new ClassPathXmlApplicationContext("ioc_beans.xml");
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("ioc_beans.xml");

        //2. Get bean instance from IOC container
        // getBean(className.class), there must be only one bean in this case.
        HelloWorld helloWorld = (HelloWorld) ctx.getBean("helloWorld");

        //3. Invoke bean method, setter injection
        helloWorld.hello();

        //4. Constructor injection
        Car car = (Car) ctx.getBean("car");
        System.out.println(car);

        Car car2 = (Car) ctx.getBean("car2");
        System.out.println(car2);

        Car car4 = (Car) ctx.getBean("car4");
        System.out.println(car4);

        //5. Map, List
        User user = (User) ctx.getBean("user2");
        System.out.println(user);


        //6. props
        DataSource dataSource = (DataSource)ctx.getBean("dataSource");
        System.out.println(dataSource);

        //7. Autowire: byName
        Person p1 = (Person) ctx.getBean("person");
        System.out.println(p1);

        /*//8. Autowire: byType
        Person p2 = (Person) ctx.getBean("person2");
        System.out.println(p2);*/

        // 9. Bean inheritance
        Address ad2 = (Address) ctx.getBean("address2");
        System.out.println(ad2);

        // 9. Bean inherit Abstract bean
        Address ad3 = (Address) ctx.getBean("address3");
        System.out.println(ad3);

        // 10. Bean inherit Abstract bean
        Address ad4 = (Address) ctx.getBean("address4");
        System.out.println(ad4);

        // 11. Bean scope: default singleton
        // singleton: 单例的. 每次调用 getBean 方法都会返回同一个 bean. 且在 IOC 容器初始化时即创建 bean 的实例. 默认值
        //prototype: 原型的. 每次调用 getBean 方法都会返回一个新的 bean. 且在第一次调用 getBean 方法时才创建实例
        Car car100 = (Car) ctx.getBean("car");
        Car car200 = (Car) ctx.getBean("car");
        System.out.println(car100 == car200);


        // 12. Bean scope: testing prototype
        Car car300 = (Car) ctx.getBean("carPrototype");
        Car car400 = (Car) ctx.getBean("carPrototype");
        System.out.println(car300 == car400);

        //13. Testing dataSource
        ComboPooledDataSource source = (ComboPooledDataSource) ctx.getBean("source");
        System.out.println(source.getUser() + " : " + source.getPassword() + " : " + source.getJdbcUrl() + " : " + source.getDriverClass());

        //14. Testing ioc_db.properties
        ComboPooledDataSource source2 = (ComboPooledDataSource) ctx.getBean("source2");
        System.out.println(source2.getUser() + " : " + source2.getPassword() + " : " + source2.getJdbcUrl() + " : " + source2.getDriverClass());

        // 15. SpEL: assign a value
        Address addressEL = (Address) ctx.getBean("addressEL");
        System.out.println(addressEL);

        //16. SpEL: assign a static property
        Car carEL = (Car) ctx.getBean("carEL");
        System.out.println(carEL.toString());

        //17. SpEl: refer to another bean, bean.field, and operator
        Person personEL = (Person) ctx.getBean("personEL");
        System.out.println(personEL);

        // 18. Testing Bean Cycle
        Car carBeanCycle = (Car) ctx.getBean("carBeanCycle");
        System.out.println(carBeanCycle);

        //19. Testing beanPostProcessor
       // ctx.close();


        // 20. Testing staticFactory
        Car carFromStaticFactory = (Car) ctx.getBean("carFromStaticFactory");
        System.out.println(carFromStaticFactory.getBrand() + " : " + carFromStaticFactory.getPrice());


        // 20. Testing staticFactory
        Car carFromInstanceFactory = (Car) ctx.getBean("carFromInstanceFactory");
        System.out.println(carFromInstanceFactory.getBrand() + " : " + carFromInstanceFactory.getPrice());
    }

}
