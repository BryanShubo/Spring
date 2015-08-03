Chapter 3 Examples
==================
This folder contains example code for chapter 3 of Spring in Action, 4th Edition.
The samples are split across multiple directories:

 * profiles     : Examples illustrating profile configuration for section 3.1.
 * conditionals : Examples illustrating conditional configuration for section 3.2.
 * scopedbeans  : Examples illustrating scoped bean configuration for section 3.4.
 * externals    : Examples illustrating external configuration for section 3.5.

Note that because the examples evolve throughout the chapter and the book's text sometimes
shows multiple ways of accomplishing a goal, not all variations of the code in the book will
be represented in these samples. You are invited to use this source code as a starting point
and experiment using the variations presented in the text.



3.4 Scoping beans
Singleton: One instance of the bean is created for the entire application. 
Prototype—One instance of the bean is created every time the bean is injectedinto or retrieved from the Spring application context. 
Session—In a web application, one instance of the bean is created for each session. 
Request—In a web application, one instance of the bean is created for eachrequest.

3.4.1 Session scope:
For instance, in a typical e-commerce application,you may have a bean that represents the user’s shopping cart. 
1) If the shopping cartbean is a singleton, then all users will be adding products to the same cart. 
2) if the shopping cart is prototype-scoped, then products added to the cartin one area of the application may not be available in another part of the applicationwhere a different prototype-scoped shopping cart was injected.

In the case of a shopping cart bean, session scope makes the most sense, becauseit’s most directly attached to a given user.

```java

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION,
                 proxyMode=ScopedProxyMode.INTERFACES)
public ShoppingCart cart() { ... }
```

```java
@Component
public class StoreService {
    @Autowiredpublic 
    void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }...
}
```
Because StoreService is a singleton bean, it will be created as the Spring applicationcontext is loaded. As it’s created, Spring will attempt to inject ShoppingCart into thesetShoppingCart() method. But the ShoppingCart bean, being session scoped,doesn’t exist yet. There won’t be an instance of ShoppingCart until a user comesalong and a session is created.

Moreover, there will be many instances of ShoppingCart: one per user. You don’t want Spring to inject just any single instance of ShoppingCart into StoreService. You want StoreService to work with the ShoppingCart instance for whichever session happens to be in play when StoreService needs to work with the shopping cart. 

Instead of injecting the actual ShoppingCart bean into StoreService, Springshould inject a proxy to the ShoppingCart bean, as illustrated in listing 3.2. This proxywill expose the same methods as ShoppingCart so that for all StoreService knows, it is the shopping cart. But when StoreService calls methods on ShoppingCart, the proxy will lazily resolve it and delegate the call to the actual session-scoped Shopping-Cart bean.

Now let’s take this understanding of scoped proxies and discuss the proxyModeattribute. As configured, proxyMode is set to ScopedProxyMode.INTERFACES, indicating that the proxy should implement the ShoppingCart interface and delegate to theimplementation bean.

This is fine (and the most ideal proxy mode) as long as ShoppingCart is an inter-face and not a class. But if ShoppingCart is a concrete class, there’s no way Spring cancreate an interface-based proxy. Instead, it must use CGLib to generate a class-basedproxy. So, if the bean type is a concrete class, you must set proxyMode to ScopedProxy-Mode.TARGET_CLASSto indicate that the proxy should be generated as an extension ofthe target class.

Although I’ve focused on session scope, know that request-scoped beans pose the same wiring challenges as session-scoped beans. Therefore, request-scoped beans should also be injected as scoped proxies.

Declaring scoped proxies in XML
```xml
<bean id="cart"
          class="com.myapp.ShoppingCart"
          scope="session">
    <aop:scoped-proxy />
</bean>
```

3.5 Runtime value injection
3.5.1 Injecting external values

```java
@Configuration
@PropertySource("classpath:/com/soundsystem/app.properties")
public class ExpressiveConfig {
    @Autowired
    Environment env;

    @Bean
    public BlankDisc disc() {
    return new BlankDisc(env.getProperty("disc.title"),env.getProperty("disc.artist"));
    }
}
```


Chapter_04: AOP
In software development, functions that span multiple points of an application are
called cross-cutting concerns.

Cross-cutting concerns: Logging, security, and transaction management

DI: DI helps you decouple application objects from each other.
AOP helps you decouple cross-cutting concerns from the objects they affect.

Advice: the job of an aspect is called advice. Advice defines both the what and the when of an aspect.
Spring can work with five kinds of advice: Before, After, After-returning, After-throwing, and Around.

@After The advice method is called after the advised method returns or throws an exception.
@AfterReturning The advice method is called after the advised method returns.
@AfterThrowing The advice method is called after the advised method throws an exception.
@Around The advice method wraps the advised method.
@Before The advice method is called before the advised method is called.

Join Points: A join point is a point in
             the execution of the application where an aspect can be plugged in.

Pointcuts: If advice defines the what and when of aspects, then pointcuts define the where. A
           pointcut definition matches one or more join points at which advice should be woven.

           An aspect does not  necessarily advise all join points in an application. Pointcuts help narrow
           down the join points advised by an aspect.
           
     
Aspect: An aspect is the merger of advice and pointcuts. what it does and where and when it does it.
                                                        

Weaving is the process of applying aspects to a target object to create a new proxied
object.
1) Compile time
2) Class load time
3) Runtime
  
                                                         
Spring’s support for AOP comes in four styles:
 Classic Spring proxy-based AOP
 Pure-POJO aspects
 @AspectJ annotation-driven aspects
 Injected AspectJ aspects (available in all versions of Spring)

SPRING ADVISES OBJECTS AT RUNTIME

SPRING ONLY SUPPORTS METHOD JOIN POINTS

4.3.4 Annotating introductions
      Some languages, such as Ruby and Groovy, have the notion of open classes. They
      make it possible to add new methods to an object or class without directly changing
      the definition of those objects or classes. Unfortunately, Java isn’t that dynamic. Once
      a class has been compiled, there’s little you can do to append new functionality to it.
      But if you think about it, isn’t that what you’ve been doing in this chapter with
      aspects? Sure, you haven’t added any new methods to objects, but you’re adding new
      functionality around the methods that the objects already have. If an aspect can wrap
      existing methods with additional functionality, why not add new methods to the
      object? In fact, using an AOP concept known as introduction, aspects can attach new
      methods to Spring beans.
      
      
      
      9.2.5 Introductions
      
      Introductions (known as inter-type declarations in AspectJ) enable an aspect to declare that advised objects implement a given interface, and to provide an implementation of that interface on behalf of those objects.
      
      An introduction is made using the @DeclareParents annotation. This annotation is used to declare that matching types have a new parent (hence the name). For example, given an interface UsageTracked, and an implementation of that interface DefaultUsageTracked, the following aspect declares that all implementors of service interfaces also implement the UsageTracked interface. (In order to expose statistics via JMX for example.)
      
      @Aspect
      public class UsageTracking {
      
          @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
          public static UsageTracked mixin;
      
          @Before("com.xyz.myapp.SystemArchitecture.businessService() && this(usageTracked)")
          public void recordUsage(UsageTracked usageTracked) {
              usageTracked.incrementUseCount();
          }
      
      }
      The interface to be implemented is determined by the type of the annotated field. The value attribute of the @DeclareParents annotation is an AspectJ type pattern :- any bean of a matching type will implement the UsageTracked interface. Note that in the before advice of the above example, service beans can be directly used as implementations of the UsageTracked interface. If accessing a bean programmatically you would write the following:
      
      UsageTracked usageTracked = (UsageTracked) context.getBean("myService");
      
      
      
      4.4 Declaring aspects in XML
      ```
      AOP configuration element Purpose
      <aop:advisor> Defines an AOP advisor.
      <aop:after> Defines an AOP after advice (regardless of whether the advised
      method returns successfully).
      <aop:after-returning> Defines an AOP after-returning advice.
      <aop:after-throwing> Defines an AOP after-throwing advice.
      <aop:around> Defines an AOP around advice.
      <aop:aspect> Defines an aspect.
      <aop:aspectj-autoproxy> Enables annotation-driven aspects using @AspectJ.
      <aop:before> Defines an AOP before advice.
      <aop:config> The top-level AOP element. Most \<aop:\*\> elements must be
      contained within \<aop:config\>.
      <aop:declare-parents> Introduces additional interfaces to advised objects that are transparently implemented.
      <aop:pointcut> Defines a pointcut
      
      ```