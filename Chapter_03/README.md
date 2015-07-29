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
