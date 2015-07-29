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

Session scope:
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
@Autowiredpublic void setShoppingCart(ShoppingCart shoppingCart) {
this.shoppingCart = shoppingCart;
}...
}

```



