SpringInActionExamples
======================
### Chapter 1
#####1.1 Without DI: DamselRescuingKnight
```
DamselRescuingKnight creates its own quest, a RescueDamselQuest, in the constructor. 
This makes a DamselRescuingKnight tightly coupled to a RescueDamselQuest and severely limits 
the knight’s quest-embarking repertoire. 
If a damsel needs rescuing, this knight’s there. But if a dragon needs slaying or a round table
needs … well … rounding, then this knight’s going to have to sit it out.

What’s more, it’d be terribly difficult to write a unit test for DamselRescuingKnight.

In such a test, you’d like to be able to assert that the quest’s embark() method is called when the knight’s embarkOnQuest() method is called.

But there’s no clear way to accomplish that here. Unfortunately, DamselRescuingKnight will remain
untested.
```
#####1.2. With DI: BraveKnight
```
BraveKnight doesn’t create his own quest. Instead, he’s given a quest at construction time as a constructor argument.

This is a type of DI known as constructor injection (others like setter, interface injection). What’s more, the quest he’s given is typed as Quest, an interface that all quests implement.

So BraveKnight could embark on a RescueDamselQuest, a SlayDragonQuest, a MakeRoundTableRounderQuest, or any other Quest implementation he’s given.

The point is that BraveKnight isn’t coupled to any specific implementation of Quest. It doesn’t matter to him what kind of quest he’s asked to embark on, as long as it implements the Quest interface. That’s the key benefit of DI—loose coupling. 
If an object only knows about its dependencies by their interface (not by their implementation or how they’re instantiated), then the dependency can be swapped out with a different implementation without the depending object knowing the difference.

One of the most common ways a dependency is swapped out is with a mock implementation during testing. You were unable to adequately test DamselRescuingKnight due to tight coupling, but you can easily test BraveKnight by giving it a mock implementation of Quest, as shown next.
```

#####1.3
```
Only Spring, through its configuration, knows how all the pieces come together. This makes it possible to change those dependencies with no changes to the depending classes.
```

###2. Aspected Oriented Programming (AOP)
#####2.1. Aspect-Oriented-Programming (AOP) enables you to capture functionality that’s used throughout your application in reusable components
```
System services such as logging, transaction management, and security each responsible for a specific piece of functionality. But often these components also carry additional responsibilities beyond their core functionality.


These system services are commonly referred to as cross-cutting concerns because they tend to cut across multiple components in a system.

Cause two problem:
1) The code that implements the system-wide concerns is duplicated across multiple components. It becomes very complicate to modify.
2) Your components are littered with code that isn’t aligned with their core functionality.


Solution:
1) AOP makes it possible to modularize these services and then apply them declaratively to the components they should affect.
2) In short, aspects ensure that POJOs remain plain.
3) With AOP, application-wide concerns (such as transactions and security) are decoupled from the objects to which they’re applied.
```      
      
### Chapter 3 Examples
#####3.1 Scoping beans
```
Singleton: One instance of the bean is created for the entire application. 
Prototype: One instance of the bean is created every time the bean is injected into or retrieved from 
           the Spring application context. 
Session: In a web application, one instance of the bean is created for each session. 
Request: In a web application, one instance of the bean is created for eachrequest.
```
#####3.1.1 Session scope: 
```
A session is the time users spend using the application, which ends when they close their browser, when they go to another Web site, or when the application designer wants (after a logout, for instance).

Why use session scope:
in a typical e-commerce application,you may have a bean that represents the user’s shopping cart. 
1) If the shopping cartBean is a singleton, then all users will be adding products to the same cart. 
2) if the shopping cart is prototype-scoped, then products added to the cartin one area of the application may not be available in another part of the applicationwhere a different prototype-scoped shopping cart was injected.

In the case of a shopping cart bean, session scope makes the most sense, becauseit’s most directly attached to a given user.
```
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
```
Because StoreService is a singleton bean, it will be created as the Spring applicationcontext is loaded. As it’s created, Spring will attempt to inject ShoppingCart into thesetShoppingCart() method. But the ShoppingCart bean, being session scoped,doesn’t exist yet. There won’t be an instance of ShoppingCart until a user comesalong and a session is created.

Moreover, there will be many instances of ShoppingCart: one per user. You don’t want Spring to inject just any single instance of ShoppingCart into StoreService. You want StoreService to work with the ShoppingCart instance for whichever session happens to be in play when StoreService needs to work with the shopping cart. 

Instead of injecting the actual ShoppingCart bean into StoreService, Springshould inject a proxy to the ShoppingCart bean, as illustrated in listing 3.2. This proxywill expose the same methods as ShoppingCart so that for all StoreService knows, it is the shopping cart. But when StoreService calls methods on ShoppingCart, the proxy will lazily resolve it and delegate the call to the actual session-scoped Shopping-Cart bean.

Now let’s take this understanding of scoped proxies and discuss the proxyModeattribute. As configured, proxyMode is set to ScopedProxyMode.INTERFACES, indicating that the proxy should implement the ShoppingCart interface and delegate to theimplementation bean.

This is fine (and the most ideal proxy mode) as long as ShoppingCart is an inter-face and not a class. But if ShoppingCart is a concrete class, there’s no way Spring cancreate an interface-based proxy. Instead, it must use CGLib to generate a class-basedproxy. So, if the bean type is a concrete class, you must set proxyMode to ScopedProxy-Mode.TARGET_CLASSto indicate that the proxy should be generated as an extension ofthe target class.

Although I’ve focused on session scope, know that request-scoped beans pose the same wiring challenges as session-scoped beans. Therefore, request-scoped beans should also be injected as scoped proxies.

Declaring scoped proxies in XML
```

```xml
<bean id="cart"
          class="com.myapp.ShoppingCart"
          scope="session">
    <aop:scoped-proxy />
</bean>
```

#####3.2 Runtime value injection
#####3.2.1 Injecting external values
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
    
### Chapter_04: AOP
#####4.1 AOP terms
```
In software development, functions that span multiple points of an application are called cross-cutting concerns.

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

Join Points: A join point is a point in the execution of the application where an aspect can be plugged in.

Pointcuts: If advice defines the what and when of aspects, then pointcuts define the where. A pointcut definition matches one or more join points at which advice should be woven.

An aspect does not  necessarily advise all join points in an application. Pointcuts help narrow
down the join points advised by an aspect.
     
**Aspect**: An aspect is the merger of advice and pointcuts. what it does and where and when it does it.

Weaving is the process of applying aspects to a target object to create a new proxied object.
1) Compile time
2) Class load time
3) Runtime
```

Spring’s support for AOP comes in four styles:
* Classic Spring proxy-based AOP
* Pure-POJO aspects
* @AspectJ annotation-driven aspects
* Injected AspectJ aspects (available in all versions of Spring)

SPRING ADVISES OBJECTS AT RUNTIME

SPRING ONLY SUPPORTS METHOD JOIN POINTS

#####4.2  Annotating introductions
```
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
          
Introductions
Introductions (known as inter-type declarations in AspectJ) enable an aspect to declare that advised objects implement a given interface, and to provide an implementation of that interface on behalf of those objects.
  
An introduction is made using the @DeclareParents annotation. This annotation is used to declare that matching types have a new parent (hence the name). For example, given an interface UsageTracked, and an implementation of that interface DefaultUsageTracked, the following aspect declares that all implementors of service interfaces also implement the UsageTracked interface. (In order to expose statistics via JMX for example.)
```
```java
  @Aspect
  public class UsageTracking {
  
      @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
      public static UsageTracked mixin;
  
      @Before("com.xyz.myapp.SystemArchitecture.businessService() && this(usageTracked)")
      public void recordUsage(UsageTracked usageTracked) {
          usageTracked.incrementUseCount();
      }
  
  }
```

The interface to be implemented is determined by the type of the annotated field. The value attribute of the @DeclareParents annotation is an AspectJ type pattern :- any bean of a matching type will implement the UsageTracked interface. Note that in the before advice of the above example, service beans can be directly used as implementations of the UsageTracked interface. If accessing a bean programmatically you would write the following:
```java 
  UsageTracked usageTracked = (UsageTracked) context.getBean("myService");
```          
          
 4.3 Declaring aspects in XML
  ```
  AOP configuration element Purpose
  <aop:advisor> Defines an AOP advisor.
  <aop:after> Defines an AOP after advice (regardless of whether the advised method returns successfully).
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
            
            
### Chapter 5 Spring Web   
#####5.1 Spring MVC work flow
```
Spring moves requests between a dispatcher servlet, handler mappings, controllers, and view resolvers.

1) A request with information goes to DispatcherServlet.
2) DispatcherServlet checks with handler mappings to figure out the selected Spring MVC controller.
3) The request drops off its payload and waits the controller processes that information.
4) Controller sends the request along with the model and view name back to the DispatcherServlet.
5) DispatcherServlet consults a view resolver to map the logical view name to a specific view implementation.
6) The view will use the model data to render output that the response object is back to client.
```

5.2 Setting up Spring MVC
```
1) Configuring DispatcherServlet
When DispatcherServlet starts up, it creates  a Spring application context  and starts
loading  it with beans declared in the configuration files or classes that it’s given.
```

2) Enable Spring MVC
a. Create  a class annotated with @EnableWebMvc
```java
@Configuration
@EnableWebMvc
@ComponentScan("spittr.web")
public class WebConfig extends WebMvcConfigurerAdapter {

  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
  }
  
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // TODO Auto-generated method stub
    super.addResourceHandlers(registry);
  }

}
```
b. Configure it via XML file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
		http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd">

    <!-- 配置自动扫描的包 -->
    <context:component-scan base-package="spring"></context:component-scan>

    <!-- 配置视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>

    <mvc:annotation-driven></mvc:annotation-driven>

    <!-- 1 example for annotation driven-->
    <mvc:view-controller path="/success" view-name="success"/>

    <!--
        default-servlet-handler 将在 SpringMVC 上下文中定义一个 DefaultServletHttpRequestHandler,
        它会对进入 DispatcherServlet 的请求进行筛查, 如果发现是没有经过映射的请求, 就将该请求交由 WEB 应用服务器默认的
        Servlet 处理. 如果不是静态资源的请求，才由 DispatcherServlet 继续处理
        一般 WEB 应用服务器默认的 Servlet 的名称都是 default.
        若所使用的 WEB 服务器的默认 Servlet 名称不是 default，则需要通过 default-servlet-name 属性显式指定
    -->
    <!-- 2 example for annotation driven-->
    <mvc:default-servlet-handler/>

    <mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven>

    <!-- 3 example for annotation driven-->
    <!-- 配置 ConversionService -->
    <bean id="conversionService"
          class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
        <property name="converters">
            <set>
                <ref bean="employeeConverter"/>
            </set>
        </property>
    </bean>


    <!-- 配置国际化资源文件 -->
    <bean id="messageSource"
          class="org.springframework.context.support.ResourceBundleMessageSource">
        <property name="basename" value="i18n"></property>
    </bean>


	<mvc:view-controller path="/i18n" view-name="i18n"/>

    <mvc:view-controller path="/i18n2" view-name="i18n2"/>

    <!-- 配置 SessionLocalResolver -->
    <bean id="localeResolver"
          class="org.springframework.web.servlet.i18n.SessionLocaleResolver"></bean>

    <mvc:interceptors>
        <!-- 配置自定义的拦截器 -->
        <bean class="spring.restful.interceptors.FirstInterceptor"></bean>

        <!-- 配置拦截器(不)作用的路径 -->
        <mvc:interceptor>
            <mvc:mapping path="/emps"/>
            <bean class="spring.restful.interceptors.SecondInterceptor"></bean>
        </mvc:interceptor>

        <!-- 配置 LocaleChanceInterceptor -->
        <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor"></bean>
    </mvc:interceptors>


    <!-- 配置 MultipartResolver -->
    <bean id="multipartResolver"
          class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="defaultEncoding" value="UTF-8"></property>
        <property name="maxUploadSize" value="1024000"></property>
    </bean>

    <!-- 配置使用 SimpleMappingExceptionResolver 来映射异常 -->
    <bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <property name="exceptionAttribute" value="ex"></property>
        <property name="exceptionMappings">
            <props>
                <prop key="java.lang.ArrayIndexOutOfBoundsException">error</prop>
            </props>
        </property>
    </bean>



    <!--
        需要进行 Spring 整合 SpringMVC 吗 ?
        还是否需要再加入 Spring 的 IOC 容器 ?
        是否需要再 web.xml 文件中配置启动 Spring IOC 容器的 ContextLoaderListener ?
        1. 需要: 通常情况下, 类似于数据源, 事务, 整合其他框架都是放在 Spring 的配置文件中(而不是放在 SpringMVC 的配置文件中).
        实际上放入 Spring 配置文件对应的 IOC 容器中的还有 Service 和 Dao.
        2. 不需要: 都放在 SpringMVC 的配置文件中. 也可以分多个 Spring 的配置文件, 然后使用 import 节点导入其他的配置文件
    -->

    <!--
        问题: 若 Spring 的 IOC 容器和 SpringMVC 的 IOC 容器扫描的包有重合的部分, 就会导致有的 bean 会被创建 2 次.
        解决:
        1. 使 Spring 的 IOC 容器扫描的包和 SpringMVC 的 IOC 容器扫描的包没有重合的部分.
        2. 使用 exclude-filter 和 include-filter 子节点来规定只能扫描的注解
    -->

    <!--
        SpringMVC 的 IOC 容器中的 bean 可以来引用 Spring IOC 容器中的 bean.
        返回来呢 ? 反之则不行. Spring IOC 容器中的 bean 却不能来引用 SpringMVC IOC 容器中的 bean!
    -->


    <!--
		SpringMVC 的 IOC 容器中的 bean 可以来引用 Spring IOC 容器中的 bean.
		返回来呢 ? 反之则不行. Spring IOC 容器中的 bean 却不能来引用 SpringMVC IOC 容器中的 bean!
	-->
    <!--
    <context:component-scan base-package="com.atguigu.springmvc" use-default-filters="false">
        <context:include-filter type="annotation"
                                expression="org.springframework.stereotype.Controller"/>
        <context:include-filter type="annotation"
                                expression="org.springframework.web.bind.annotation.ControllerAdvice"/>
    </context:component-scan>
-->
</beans>

```

#####5.3 Annotations:
```
@Controller: (for class) declare the bean in the Spring application context. 
@RequestMapping(name="/", method=GET): (for both class and method )
@RequestMapping({"/","/home"}, method=GET): (for both class and method )

```

#####5.4 Spring MVC provides several ways that a client can pass data into a controller’s handler method. These include
```
    1) Query parameters: /spittles?max=238900&count=50
			    public List<Spittle> spittles(
			          @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
			          @RequestParam(value="count", defaultValue="20") int count)
    2) Path variables: /spittles/12345
			@RequestMapping(value="/{spittleId}", method=RequestMethod.GET)
			public String spittle(
			  @PathVariable("spittleId") long spittleId, 
			  Model model)
    3) Form parameters: 
    Note: When handling a POST request, it’s usually a good idea to send a redirect after the
          POST has completed processing so that a browser refresh won’t accidentally submit the
          form a second time. This test
    Note: When InternalResourceViewResolver sees the redirect / forward: prefix on the view specification,
          it knows to interpret it as a redirect specification instead of as a view name.
    Note: Notice that the <form> tag doesn’t have an action parameter set. Because of that,
          when this form is submitted, it will be posted back to the same URL path that displayed
          it. That is, it will be posted back to /spitters/register.
          That means you’ll need something back on the server to handle the HTTP POST
          request. Let’s add another method to SpitterController to handle form submission.
          
	    @RequestMapping(value="/register", method=POST)
	      public String processRegistration(
	          @Valid Spitter spitter, 
	          Errors errors) {
	        if (errors.hasErrors()) {
	          return "registerForm";
	        }        
	        spitterRepository.save(spitter);
	        return "redirect:/spitter/" + spitter.getUsername();
	      }
```    
#####5. Validating forms
```
One way to handle validation, is to add code to the processRegistration() method to check for invalid values 
and send the user back to the registration form unless the data is valid.

Two: using Java Validation API
Annotation Description
@AssertFalse The annotated element must be a Boolean type and be false.
@AssertTrue The annotated element must be a Boolean type and be true.
@DecimalMax The annotated element must be a number whose value is less than or equal to a given BigDecimalString value.
@DecimalMin The annotated element must be a number whose value is greater than or equal to a given BigDecimalString value.
@Digits The annotated element must be a number whose value has a specified number of digits.
@Future The value of the annotated element must be a date in the future.
@Max The annotated element must be a number whose value is less than or equal to a given value.
@Min The annotated element must be a number whose value is greater than or equal to a given value.
@NotNull The value of the annotated element must not be null.
@Null The value of the annotated element must be null.
@Past The value of the annotated element must be a date in the past.
@Pattern The value of the annotated element must match a given regular expression.
@Size The value of the annotated element must be either a String, a collection, or an array whose length fits within the given range.
```
    
    
### Chapter 6: Rendering web views
#####6.1 View Resolution
```
At most, the controller methods and view implementations should agree on the contents of the model;
apart from that, they should keep an arms-length distance from each other.
```
```java
public interface ViewResolver {
View resolveViewName(String viewName, Locale locale)
throws Exception;
}

The resolveViewName() method, when given a view name and a Locale, returns a
View instance. View is another interface that looks like this

public interface View {
String getContentType();
void render(Map<String, ?> model,
HttpServletRequest request,
HttpServletResponse response) throws Exception;
}

The View interface’s job is to take the model, as well as the servlet request and
response objects, and render output into the response.
```       

View resolver Description           
```
BeanNameViewResolver: Resolves views as beans in the Spring application context whose ID is the same as the view name.

ContentNegotiatingViewResolver: Resolves views by considering the content type desired by the client and delegating to another view resolver that can produce that type.

FreeMarkerViewResolver:  Resolves views as FreeMarker templates.

**InternalResourceViewResolver**(JSPs): Resolves views as resources internal to the web application

JasperReportsViewResolver:  Resolves views as JasperReports definitions.

ResourceBundleViewResolver:  Resolves views from a resource bundle (typically a properties
file).

**TilesViewResolver**(apache tile views):  Resolves views as Apache Tile definitions, where the tile ID is the same as the view name. Note that there are two different TilesViewResolver implementations, one each for Tiles 2.0 and Tiles 3.0.

UrlBasedViewResolver:  Resolves views directly from the view name, where the view name matches the name of a physical view definition.

VelocityLayoutViewResolver: Resolves views as Velocity layouts to compose pages from different Velocity templates.

VelocityViewResolver: Resolves views as Velocity templates.

XmlViewResolver: Resolves views as bean definitions from a specified XML file. Similar to BeanNameViewResolver.

XsltViewResolver: Resolves views to be rendered as the result of an XSLT transformation.
```

#####6.2 Creating JSP views
Spring supports JSP views in two ways:
* InternalResourceViewResolver can be used to resolve view names into JSP files. Moreover, if you’re using JavaServer Pages Standard Tag Library (JSTL) tags in your JSP pages, InternalResourceViewResolver can resolve view names into JSP files fronted by JstlView to expose JSTL locale and resource bundle variables to JSTL’s formatting and message tags.

* Spring provides two JSP tag libraries, one for form-to-model binding and one providing general utility features.

InternalResourceViewResolver resolves views by adding a prefix and a suffix to the view name.
```
1) Resolving JSTL views
@Bean
public ViewResolver viewResolver() {
InternalResourceViewResolver resolver =
new InternalResourceViewResolver();
resolver.setPrefix("/WEB-INF/views/");
resolver.setSuffix(".jsp");
resolver.setViewClass(
org.springframework.web.servlet.view.JstlView.class);
return resolver;
}
Again, you can accomplish the same thing with XML:
<bean id="viewResolver"
class="org.springframework.web.servlet.view.
InternalResourceViewResolver"
p:prefix="/WEB-INF/views/"
p:suffix=".jsp"
p:viewClass="org.springframework.web.servlet.view.JstlView" />


2) Spring offers two JSP tag libraries to
   help define the view of your Spring MVC web views. One tag library renders HTML
   form tags that are bound to a model attribute. The other has a hodgepodge of utility
   tags that come in handy from time to time.
   
Spring’s form-binding tag library includes tags to bind model objects to and from rendered
HTML forms.
JSP tag Description
<sf:checkbox> Renders an HTML <input> tag with type set to checkbox.
<sf:checkboxes> Renders multiple HTML <input> tags with type set to checkbox.
<sf:errors> Renders field errors in an HTML <span> tag.
<sf:form> Renders an HTML <form> tag and exposed binding path to inner tags
for data-binding.
<sf:hidden> Renders an HTML <input> tag with type set to hidden.
<sf:input> Renders an HTML <input> tag with type set to text.
<sf:label> Renders an HTML <label> tag.
<sf:option> Renders an HTML <option> tag. The selected attribute is set
according to the bound value.
<sf:options> Renders a list of HTML <option> tags corresponding to the bound
collection, array, or map.
<sf:password> Renders an HTML <input> tag with type set to password.
<sf:radiobutton> Renders an HTML <input> tag with type set to radio.
<sf:radiobuttons> Renders multiple HTML <input> tags with type set to radio.
<sf:select> Renders an HTML <select> tag.
<sf:textarea> Renders an HTML <textarea> tag.
   
   
 Using Spring’s form-binding tags gives you a slight improvement over using standard
 HTML tags—the form is prepopulated with the previously entered values after failed
 validation
   
Spring’s other JSP tag library offers a handful of convenient utility tags in addition to some
legacy data-binding tags.
JSP tag Description
<s:bind> Exports a bound property status to a page-scoped status property. Used
along with <s:path> to obtain a bound property value.
<s:escapeBody> HTML and/or JavaScript escapes the content in the body of the tag.
<s:hasBindErrors> Conditionally renders content if a specified model object (in a request attribute)
has bind errors.
<s:htmlEscape> Sets the default HTML escape value for the current page.
<s:message> Retrieves the message with the given code and either renders it (default) or
assigns it to a page-, request-, session-, or application-scoped variable
(when using the var and scope attributes).
<s:nestedPath> Sets a nested path to be used by <s:bind>.
<s:theme> Retrieves a theme message with the given code and either renders it
(default) or assigns it to a page-, request-, session-, or application-scoped
variable (when using the var and scope attributes).
<s:transform> Transforms properties not contained in a command object using a command
object’s property editors.
<s:url> Creates context-relative URLs with support for URI template variables and
HTML/XML/JavaScript escaping. Can either render the URL (default) or
assign it to a page-, request-, session-, or application-scoped variable
(when using the var and scope attributes).
<s:eval> Evaluates Spring Expression Language (SpEL) expressions, rendering the
result (default) or assigning it to a page-, request-, session-, or applicationscoped
variable (when using the var and scope attributes).   
```   
   
#####6.3 Apache Tiles Views
   
   
#####6.4 Thymeleaf
   
   
   
### Chapter 7 Advanced Spring MVC (upload file, handle exception)
#####7.1 Setup Spring MVC 
web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5"
xmlns="http://java.sun.com/xml/ns/javaee"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
<context-param>
<param-name>contextConfigLocation</param-name>
<param-value>/WEB-INF/spring/root-context.xml</param-value>
</context-param>
<listener>
<listener-class>
org.springframework.web.context.ContextLoaderListener
</listener-class>
</listener>
<servlet>
<servlet-name>appServlet</servlet-name>
<servlet-class>
org.springframework.web.servlet.DispatcherServlet
</servlet-class>
<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
<servlet-name>appServlet</servlet-name>
<url-pattern>/</url-pattern>
</servlet-mapping>
</web-app>
```

Servlet Filter is used for monitoring request and response from client to the servlet, or to modify the request and response, or to audit and log.
   
Servlet Listener is used for listening to events in a web containers, such as when you create a session, or place an attribute in an session or if you 
passivate and activate in another container, to subscribe to these events you can configure listener in web.xml, for example HttpSessionListener
   
One important difference is often overlooked: while listeners get triggered for an actual physical request, filters work with servlet container dispatches. 

For one listener invocation there may be multiple filters/servlet invocations.

1) The contextConfigLocation context parameter specifies the location of the XML file that defines the root 
application context loaded by ContextLoaderListener.
2) DispatcherServlet loads its application context with beans defined in a file whose
name is based on the servlet name.
      
      
 #####7.2 Multipart form data (upload file)
 ```
 Configuring a multipart resolver
 Two out-of-the-box implementations of MultipartResolver to choose from:
 * StandardServletMultipartResolver (recommended)—Relies on Servlet 3.0 support for multipart requests (since Spring 3.1)
 * CommonsMultipartResolver (for pre-Servlet 3.0)—Resolves multipart requests using Jakarta Commons FileUpload

Generally speaking, StandardServletMultipartResolver should probably be your first choice of these two. It uses existing 
support in your servlet container and does not require any additional project dependencies
```

1)StandardServletMultipartResolver: 
constructor accepts the following:
  * Temporary file path where the file will be written during the upload
  * The maximum size (in bytes) of any file uploaded. By default there is no limit.
  * The maximum size (in bytes) of the entire multipart request, regardless of how
  many parts or how big any of the parts are. By default there is no limit.
  * The maximum size (in bytes) of a file that can be uploaded without being written
  to the temporary location. The default is 0, meaning that all uploaded files
  will be written to disk.
  
  ```xml
  <servlet>
  <servlet-name>appServlet</servlet-name>
  <servlet-class>
  org.springframework.web.servlet.DispatcherServlet
  </servlet-class>
  <load-on-startup>1</load-on-startup>
  <multipart-config>
  <location>/tmp/spittr/uploads</location>
  <max-file-size>2097152</max-file-size>
  <max-request-size>4194304</max-request-size>
  </multipart-config>
  </servlet
  ```
  
  2)CommonsMultipartResolver: 
Unlike StandardServletMultipartResolver, there’s no need to configure a temporary file location with CommonsMultipartResolver.
By default, the location is the servlet container’s temporary directory. But you can specify  a different location by setting 
the uploadTempDir property:
  ```java
  @Bean
  public MultipartResolver multipartResolver() throws IOException {
  CommonsMultipartResolver multipartResolver =
  new CommonsMultipartResolver();
  multipartResolver.setUploadTempDir(
  new FileSystemResource("/tmp/spittr/uploads"));
  multipartResolver.setMaxUploadSize(2097152);
  multipartResolver.setMaxInMemorySize(0);
  return multipartResolver;
  }
  ```
  
#####7.3 handling multipart requests
  in controller method:@RequestPart
  ```java
  i: byte[], using transferTo() method
  @RequestMapping(value="/register", method=POST)
  public String processRegistration(
  @RequestPart("profilePicture") byte[] profilePicture,
  @Valid Spitter spitter,
  Errors errors) {
  ...
  }
  
  
  profilePicture.transferTo(
  new File("/data/spittr/" + profilePicture.getOriginalFilename()));
  
  ii: Part, using write() method
  @RequestMapping(value="/register", method=POST)
  public String processRegistration(
  @RequestPart("profilePicture") Part profilePicture,
  @Valid Spitter spitter,
  Errors errors) {
  ...
  }
  
  profilePicture.write("/data/spittr/" +
  profilePicture.getOriginalFilename());
  ```
  It’s worth noting that if you write your controller handler methods to accept file
  uploads via a Part parameter, then you don’t need to configure the StandardServlet-
  MultipartResolver bean. StandardServletMultipartResolver is required only
  when you’re working with MultipartFile.
  
  
  7.3 Handling exceptions
  Spring offers a handful of ways to translate exceptions to responses:
  * Certain Spring exceptions are automatically mapped to specific HTTP status codes.
  * An exception can be annotated with @ResponseStatus to map it to an HTTP status code.
  * A method can be annotated with @ExceptionHandler to handle the exception
  
  ```
  Spring exception HTTP status code
  BindException 400 - Bad Request
  ConversionNotSupportedException 500 - Internal Server Error
  HttpMediaTypeNotAcceptableException 406 - Not Acceptable
  HttpMediaTypeNotSupportedException 415 - Unsupported Media Type
  HttpMessageNotReadableException 400 - Bad Request
  HttpMessageNotWritableException 500 - Internal Server Error
  HttpRequestMethodNotSupportedException 405 - Method Not Allowed
  MethodArgumentNotValidException 400 - Bad Request
  MissingServletRequestParameterException 400 - Bad Request
  MissingServletRequestPartException 400 - Bad Request
  NoSuchRequestHandlingMethodException 404 - Not Found
  TypeMismatchException 400 - Bad Request
  ```
  
  1) Mapping exceptions to HTTP status code (Customize exception display info)
  ```java
  @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Spittle Not Found")
  public class SpittleNotFoundException extends RuntimeException {
  
  }
  ```

  2) Writing exception-handling methods
  if you want the response to carry more than just a status code that represents the error
  that occurred
  ```java
  @RequestMapping(method=RequestMethod.POST)
  public String saveSpittle(SpittleForm form, Model model) {
  spittleRepository.save(
  new Spittle(null, form.getMessage(), new Date(),
  form.getLongitude(), form.getLatitude()));
  return "redirect:/spittles";
  }
  As you can see, saveSpittle() is now much simpler. Because it’s written to only be
  concerned with the successful saving of a Spittle, it has only one path and is easy to
  follow (and test).
  Now let’s add a new method to SpittleController that will handle the case where
  DuplicateSpittleException is thrown:
  @ExceptionHandler(DuplicateSpittleException.class)
  public String handleDuplicateSpittle() {
  return "error/duplicate";
  }
  ```
  The @ExceptionHandler annotation has been applied to the handleDuplicateSpittle() method, 
  designating it as the go-to method when a DuplicateSpittle-
  Exception is thrown. It returns a String, which, just as with the request-handling
  method, specifies the logical name of the view to render, telling the user that they
  attempted to create a duplicate entry.
  
  What’s especially interesting about @ExceptionHandler methods is that they handle
  their exceptions from any handler method in the same controller. So although
  you created the handleDuplicateSpittle() method from the code extracted from
  saveSpittle(), it will handle a DuplicateSpittleException thrown from any
  method in SpittleController. Rather than duplicate exception-handling code in
  every method that has the potential for throwing a DuplicateSpittleException, this
  one method covers them all.
  
  @ExceptionHandler methods can handle exceptions thrown from any handler
  method in the **same controller class**.
  
  **advice class**(Spring 3.2) can handle exceptions thrown from handler methods in **any controller**. 
  
  
  
  7.4 Advising controllers
  To consistently handle common tasks, including exception handling, across all
  controllers in your application.
  
  A controller advice is any class that’s annotated with @ControllerAdvice and has one or more of the following
  kinds of methods:
  ** @ExceptionHandler-annotated
  ** @InitBinder-annotated
  ** @ModelAttribute-annotated
  
  
  Those methods in an @ControllerAdvice-annotated class are applied globally across
  all @RequestMapping-annotated methods on all controllers in an application.
  ```java
  package spitter.web;
  import org.springframework.web.bind.annotation.ControllerAdvice;
  import org.springframework.web.bind.annotation.ExceptionHandler;
  @ControllerAdvice
  public class AppWideExceptionHandler {
  @ExceptionHandler(DuplicateSpittleException.class)
  public String duplicateSpittleHandler() {
  return "error/duplicate";
  }
  }
  ```
  
  7.5 Carrying data across redirect requests
  It’s generally a good practice to perform a redirect after handling a POST request. Among other
  things, this prevents the client from reissuing a dangerous POST request if the user clicks the Refresh or 
  back-arrow button in their browser
  
  ```java
  return "redirect:/spitter/" + spitter.getUsername();
  ```
  **Forward**: when a handler method completes, any model data specified in the method is copied into the request 
  as request attributes, and the request is forwarded to the view for rendering. Because it’s the **same request** 
  that’s handled by both the controller method and the view, the request attributes survive the forward.
  
  **Redirect**: Any model data carried in the original request dies with the request. 
                The new request is devoid of any model data in its attributes and has to figure it out on its own.
                
  Two options to solve carrying data across redirect request:
  * Passing data as path variables and/or query parameters using URL templates
  * Sending data in flash attributes
  
  1) Redirecting with URL templates
  Sending data across a redirect via path variables and query parameters is only good for sending simple values,
  such as String and numeric values. It does not work for **passing object**.
  ```
  return "redirect:/spitter/{username}";
  return "redirect:/spitter/habuma?spitterId=42"
  ```
  
  2) Flash Attributes
  One option is to put the Spitter into the session. You put the Spitter into the session before the redirect and
  then retrieve it from the session after the redirect. One more step, cleaning it up from the session after the redirect
  
  Flash attributes, by definition, carry data until the next request; then they go away.
  method: model.addFlashAttribute()
  ```java
  @RequestMapping(value="/register", method=POST)
  public String processRegistration(
  Spitter spitter, RedirectAttributes model) {
  spitterRepository.save(spitter);
  model.addAttribute("username", spitter.getUsername());
  model.addFlashAttribute("spitter", spitter);
  return "redirect:/spitter/{username}";
  }
  ```
  
  Before the redirect takes place, all flash attributes are copied into the session. After
  the redirect, the flash attributes stored in the session are moved out of the session and
  into the model. The method that handles the redirect request can then access the
  Spitter from the model, just like any other model object. 
  

