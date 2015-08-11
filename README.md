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

In such a test, you’d like to be able to assert that the quest’s embark() method is called when the knight’s 
embarkOnQuest() method is called.

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
  


### Chapter 8: Spring Web Flow
Applications:
```
Checkout process on an e-commerce site.
Application process
```
What is Spring Web Flow:
```
Spring Web Flow is a web framework that enables the development of elements following
a prescribed flow.

It does this by separating the definition of an application’s
flow from the classes and views that implement the flow’s behavior.
```

#####8.1 Configure Web Flow
1) **flow executor** creates and launches an instance of the flow execution for that user.
2) **flow registry** is used to load flow definitions and make them available to the flow executor.
3) **FlowHandlerMapping** is used to help DispatcherServlet know that it should send flow requests to Spring Web Flow.
4) **FlowHandlerAdapter** is used to answer that call. A FlowHandlerAdapter is equivalent to a Spring MVC controller in that
      it handles requests coming in for a flow and processes those requests.


1) **flow executor** drives the execution of a flow. When a user enters a flow, the flow executor creates and launches
an instance of the flow execution for that user.
```xml
<flow:flow-executor id="flowExecutor" />
```
**flow executor** is responsible for creating and executing flows. **NOT** responsible for loading flow definitions.

2) **flow registry** is to load flow definitions and make them available to the flow executor.
```xml
<flow:flow-registry id="flowRegistry" base-path="/WEB-INF/flows">
<flow:flow-location-pattern value="*-flow.xml" />
</flow:flow-registry>
```
or
```xml
<flow:flow-registry id="flowRegistry">
<flow:flow-location path="/WEB-INF/flows/springpizza.xml" />
</flow:flow-registry>
```

3) **FlowHandlerMapping** to help DispatcherServlet know that it should send flow requests to Spring Web Flow.
```xml
   <bean class="org.springframework.webflow.mvc.servlet.FlowHandlerMapping">
   <property name="flowRegistry" ref="flowRegistry" />
   </bean>
```

4) FlowHandlerMapping’s job is to direct flow requests to Spring Web Flow,
   **FlowHandlerAdapter** to answer that call. A FlowHandlerAdapter is equivalent to a Spring MVC controller in that
   it handles requests coming in for a flow and processes those requests.
```xml
   <bean class="org.springframework.webflow.mvc.servlet.FlowHandlerAdapter">
   <property name="flowExecutor" ref="flowExecutor" />
   </bean>
```


#####8.2 Components
1) States: are points in a flow where something happens.
2) Transitions: are roads that are connect these points.
3) Flow Data: the current condition of flow.

#####8.2.1 States: five states (Action, Decision, End, Subflow, and View)
```
i Action: Action states are where the logic of a flow takes place.
ii Decision: Decision states branch the flow in two directions, routing the flow based on the outcome of evaluating flow data.
iii End: The end state is the last stop for a flow. Once a flow has reached its end state, the flow is terminated.
iv Subflow: A subflow state starts a new flow in the context of a flow that is already underway.
v View: A view state pauses the flow and invites the user to participate in the flow
```

**View**
View states are used to display information to the user and to offer the user an opportunity
to play an active role in the flow.
```xml
<view-state id="welcome" />
or
<view-state id="welcome" view="greeting" />
or
<view-state id="takePayment" model="flowScope.paymentDetails"/>
```

**ACTION STATES**
Action states are where the application itself goes to work.
Action states typically invoke some method on a Spring-managed bean and then transition to another state depending on the outcome of the method call.
```xml
<action-state id="saveOrder">
<evaluate expression="pizzaFlowActions.saveOrder(order)" />
<transition to="thankYou" />
</action-state>
```

**DECISION STATES**
Decision states enable a binary branch in a flow execution. A decision state evaluates
a Boolean expression and takes one of two transitions, depending on whether the
expression evaluates to true or false.
```xml
<decision-state id="checkDeliveryArea">
<if test="pizzaFlowActions.checkDeliveryArea(customer.zipCode)"
then="addCustomer"
else="deliveryWarning" />
</decision-state>
```

**SUBFLOW STATES**
The <subflow-state> element lets you call another flow from within an executing flow.
It’s analogous to calling a method from within another method.
```xml
<subflow-state id="order" subflow="pizza/order">
<input name="order" value="order"/>
<transition on="orderCreated" to="payment" />
</subflow-state>
```
Here, the <input> element is used to pass the order object as input to the subflow.
And if the subflow ends with an <end-state> whose ID is orderCreated, then the flow
will transition to the state whose ID is payment.


**END STATES**
The <end-state> element designates the end of a flow:
```xml
<end-state id="customerReady" />
```
When the flow reaches an <end-state>, the flow ends. What happens next depends
on a few factors:
```
i: If the flow that’s ending is a subflow, the calling flow will proceed from the
<subflow-state>. The <end-state>’s ID will be used as an event to trigger the transition away from the <subflow-state>.

ii: If the <end-state> has its view attribute set, the specified view will be rendered.
The view may be a flow-relative path to a view template, prefixed with externalRedirect: to redirect to some page
external to the flow, or prefixed with flowRedirect: to redirect to another flow.

iii: If the ending flow isn’t a subflow and no view is specified, the flow ends. The
browser lands on the flow’s base URL, and, with no current flow active, a new
instance of the flow begins.
```

#####8.2.2 Transitions
Every state in a flow, with the exception of end states, should have at least one transition so that the
flow will know where to go once that state has completed. A state may have multiple
transitions, each one representing a different path that could be taken on completion of the state.

```xml
<transition to="customerReady" />
or
<transition on="phoneEntered" to="lookupCustomer"/>
<!--In above example, the flow will transition to the state whose ID is lookupCustomer if a
    phoneEntered event is fired.-->

<transition on-exception= "com.springinaction.pizza.service.CustomerNotFoundException"
            to="registrationForm" />
```

**GLOBAL TRANSITIONS**
Rather than repeat common transitions in multiple states, you can define them
as global transitions by placing the <transition> element as a child of a <globaltransitions>
element.
```xml
<global-transitions>
<transition on="cancel" to="endState" />
</global-transitions>
```
With this global transition in place, all states in the flow will have an implicit cancel
transition.

#####8.2.3 Flow Data
**DECLARING VARIABLES**
Flow data is stored in variables that can be referenced at various points in the flow. It
can be created and accumulated in several ways.
<var> is always flow scoped.
```xml
<var name="customer" class="com.springinaction.pizza.domain.Customer"/>
```
This variable is available to all states in a flow.

<evaluate> element: view-scoped.
```xml
<evaluate result="viewScope.toppingsList"
expression="T(com.springinaction.pizza.domain.Topping).asList()" />
```

<set> element can set a variable’s value: flow scoped.
```xml
<set name="flowScope.pizza"
value="new com.springinaction.pizza.domain.Pizza()" />
```

**SCOPING FLOW DATA**
The lifespan and visibility of data carried in a flow will vary depending on the scope of
the variable it’s kept in.
```
i Conversation: Created when a top-level flow starts, and destroyed when the top-level flow ends. Shared by a top-level flow and all of its subflows.
ii Flow:  Created when a flow starts, and destroyed when the flow ends. Only visible in the flow it was created by.
iii Request: Created when a request is made into a flow, and destroyed when the flow returns.
iv Flash:  Created when a flow starts, and destroyed when the flow ends. It’s also cleared out after a view state renders.
v View: Created when a view state is entered, and destroyed when the state exits. Visible only in the view state.
```

#####8.3 Pizza flow
Simple flow:

start->identify customer--(customerReady)-->buildOrder--(orderCreated)-->takePayment
--(paymentTaken)-->saveOrder->thankCustomer->endState

```xml
<?xml version="1.0" encoding="UTF-8"?>
<flow xmlns="http://www.springframework.org/schema/webflow"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/webflow
http://www.springframework.org/schema/webflow/spring-webflow-2.3.xsd">
<var name="order"
class="com.springinaction.pizza.domain.Order"/>
<subflow-state id="identifyCustomer" subflow="pizza/customer">
<output name="customer" value="order.customer"/>
<transition on="customerReady" to="buildOrder" />
</subflow-state>
<subflow-state id="buildOrder" subflow="pizza/order">
<input name="order" value="order"/>
<transition on="orderCreated" to="takePayment" />
</subflow-state>
<subflow-state id="takePayment" subflow="pizza/payment">
<input name="order" value="order"/>
<transition on="paymentTaken" to="saveOrder"/>
</subflow-state>
<action-state id="saveOrder">
<evaluate expression="pizzaFlowActions.saveOrder(order)" />
<transition to="thankCustomer" />
</action-state>
<view-state id="thankCustomer">
<transition to="endState" />
</view-state>
<end-state id="endState" />
<global-transitions>
<transition on="cancel" to="endState" />
</global-transitions>
</flow
```

Customer state
```xml
<html xmlns:jsp="http://java.sun.com/JSP/Page"
xmlns:form="http://www.springframework.org/tags/form">
<jsp:output omit-xml-declaration="yes"/>
<jsp:directive.page contentType="text/html;charset=UTF-8" />
<head><title>Spizza</title></head>
<body>
<h2>Welcome to Spizza!!!</h2>
<form:form>
<input type="hidden" name="_flowExecutionKey"
value="${flowExecutionKey}"/>
<input type="text" name="phoneNumber"/><br/>
<input type="submit" name="_eventId_phoneEntered"
value="Lookup Customer" />
</form:form>
</body>
</html>
```

#####8.4 Secure Web Flow
States, transitions, and entire flows can be secured in Spring Web Flow by using the
<secured> element as a child of those elements. For example, to secure access to a
view state, you might use <secured> like this:
```xml
<view-state id="restricted">
<secured attributes="ROLE_ADMIN" match="all"/>
</view-state>
```


###Chapter 9: Securing Web Applications
Spring Security is a security framework that provides declarative security for your
Spring-based applications.

Spring Security provides a comprehensive security solution, handling authentication and authorization at
both the **web request level** and at the **method invocation level**.

Now at version 3.2, Spring Security tackles security from two angles.
1)To secure web requests and restrict access at the URL level, Spring Security uses servlet filters.

2)Spring Security can also secure method invocations using Spring AOP, proxying objects and applying advice to ensure that the user has the proper authority to invoke secured
methods.

We’ll focus on web-layer security with Spring Security in this chapter.
Later, in chapter 14, we’ll revisit Spring Security and see how it can be used to secure method invocations.

#####9.1.1 Spring Security Modules

11 Spring Security Modules (core, configuration,web)
```
i ACL: Provides support for domain object security through access control lists (ACLs).
ii Aspects:  A small module providing support for AspectJ-based aspects instead of standard
             Spring AOP when using Spring Security annotations.
iii CAS:  Client Support for single sign-on authentication using Jasig’s Central Authentication
          Service (CAS).
iv: Configuration: Contains support for configuring Spring Security with XML and Java. (Java configuration
                   support introduced in Spring Security 3.2.)
v Core: Provides the essential Spring Security library.
vi Cryptography: Provides support for encryption and password encoding.
vii LDAP: Provides support for LDAP-based authentication.
viii OpenID:  Contains support for centralized authentication with OpenID.
ix Remoting: Provides integration with Spring Remoting.
x Tag Library: Spring Security’s JSP tag library.
xi Web: Provides Spring Security’s filter-based web security support.
```

#####9.1.2 Filtering web requests
DelegatingFilterProxy is a special servlet filter that, by itself, doesn’t do much.
Instead, it delegates to an implementation of javax.servlet.Filter that’s registered
as a <bean> in the Spring application context:

If you like configuring servlets and filters in the traditional web.xml file, you can do
that with the <filter> element, like this:
```xml
<filter>
<filter-name>springSecurityFilterChain</filter-name>
<filter-class>
org.springframework.web.filter.DelegatingFilterProxy
</filter-class>
</filter>
```

#####9.1.3 examples
```java
package spitter.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.
configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.
configuration.WebSecurityConfigurerAdapter;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
```
Enable spring secure for Spring MVC
```java
package spitter.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.
configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.
configuration.EnableWebMvcSecurity;
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
```

Overriding WebSecurityConfigurerAdapter’s configure() methods
```java
configure(WebSecurity) //Override to configure Spring Security’s filter chain.
configure(HttpSecurity) //Override to configure how requests are secured by interceptors.
configure(AuthenticationManagerBuilder) //Override to configure user-details services
```

Three configurations need to be specified:
```
i Configure a user store
ii Specify which requests should and should not require authentication, as well as
what authorities they require
iii Provide a custom login screen to replace the plain default login screen
```

#####9.2 User store
Three types: in memory, relational database, and LDAP

#####9.2.1 Working with an in-memory user store
```java
package spitter.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.
authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.
configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.
configuration.EnableWebMvcSecurity;
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
@Override
protected void configure(AuthenticationManagerBuilder auth)
throws Exception {
auth
.inMemoryAuthentication()
.withUser("user").password("password").roles("USER").and()
.withUser("admin").password("password").roles("USER", "ADMIN");
}
}
```
Although an in-memory user store is very useful for debugging and developer testing
purposes, it’s probably not the most ideal choice for a production application. For
production-ready purposes, it’s usually better to maintain user data in a database of
some sort.

#####9.2.2 Authenticating against database tables
It’s quite common for user data to be stored in a relational database, accessed via
JDBC. To configure Spring Security to authenticate against a JDBC-backed user store,
you can use the jdbcAuthentication() method. The minimal configuration required
is as follows:
```java
@Autowired
DataSource dataSource;
@Override
protected void configure(AuthenticationManagerBuilder auth)
throws Exception {
auth
.jdbcAuthentication()
.dataSource(dataSource);
}
```

WORKING WITH ENCODED PASSWORDS
```
To remedy this problem, you need to specify a password encoder by calling the
passwordEncoder() method:
@Override
protected void configure(AuthenticationManagerBuilder auth)
throws Exception {
auth
.jdbcAuthentication()
.dataSource(dataSource)
.usersByUsernameQuery(
"select username, password, true " +
"from Spitter where username=?")
.authoritiesByUsernameQuery(
"select username, 'ROLE_USER' from Spitter where username=?")
.passwordEncoder(new StandardPasswordEncoder("53cr3t"));
}
```
Use provided encode implementations or implement your own encoding.

No matter which password encoder you use, it’s important to understand that the
password in the database is never decoded. Instead, the password that the user enters
at login is encoded using the same algorithm and is then compared with the encoded
password in the database. That comparison is performed in the PasswordEncoder’s
matches() method.


#####9.2.3 Applying LDAP-backed authentication
```java
@Override
protected void configure(AuthenticationManagerBuilder auth)
throws Exception {
auth
.ldapAuthentication()
.userSearchFilter("(uid={0})")
.groupSearchFilter("member={0}");
}
```
The **userSearchFilter()** and **groupSearchFilter()** methods are used to provide a
filter for the base LDAP queries, which are used to search for users and groups. By
default, the base queries for both users and groups are empty, indicating that the
search will be done from the root of the LDAP hierarchy


#####9.2.4 Configuring a custom user service
Suppose that you need to authenticate against users in a non-relational database such
as Mongo or Neo4j. In that case, you’ll need to implement a custom implementation
of the UserDetailsService interface.
The UserDetailsService interface is rather straightforward:
```java
public interface UserDetailsService {
UserDetails loadUserByUsername(String username)
throws UsernameNotFoundException;
}
```
All you need to do is implement the loadUserByUsername() method to find a user
given the user’s username. loadUserByUsername() then returns a UserDetails object
representing the given user. The following listing shows an implementation of
UserDetailsService that looks up a user from a given implementation of Spitter-
Repository.

#####9.3 Intercepting requests






###Chapter 13: Caching
Caching is a way to store frequently needed information so that it’s readily available
when needed. Caching is a great way to keep your application code from having to derive, calculate,
or retrieve the same answers over and over again for the same question.

Although Spring doesn’t implement a cache solution, it offers declarative support for caching
that integrates with several popular caching implementations.

#####13.1 Enabling cache support
Spring’s cache abstraction comes in two forms:
1) Annotation-driven caching
2) XML-declared caching

```java
package com.habuma.cachefun;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableCaching
public class CachingConfig {
@Bean
public CacheManager cacheManager() {
return new ConcurrentMapCacheManager();
}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
<cache:annotation-driven />
<bean id="cacheManager" class=
"org.springframework.cache.concurrent.ConcurrentMapCacheManager" />
</beans>
```
Under the covers, @EnableCaching and <cache:annotation-driven> work the same
way. They create an aspect with pointcuts that trigger off of Spring’s caching annotations.
Depending on the annotation used and the state of the cache, that aspect will **fetch**
a value from the cache, **add** a value to the cache, or **remove** a value from the cache.

Cache managers are the heart of Spring’s cache abstraction, enabling integration with one of several
popular caching implementations.

#####13.1.1 Configuring a cache manager
```
Spring 3.1 comes with five cache-manager implementations:
1) SimpleCacheManager
2) NoOpCacheManager
3) ConcurrentMapCacheManager
4) CompositeCacheManager
5) EhCacheCacheManager
Outside of the core Spring Framework, Spring Data offers two more cache managers:
6) RedisCacheManager (from Spring Data Redis)
7) GemfireCacheManager (from Spring Data GemFire)
```

**5) EhCacheCacheManager**
```java
@Configuration
@EnableCaching
public class CachingConfig {
@Bean
public EhCacheCacheManager cacheManager(CacheManager cm) {
return new EhCacheCacheManager(cm);
}
@Bean
public EhCacheManagerFactoryBean ehcache() {
EhCacheManagerFactoryBean ehCacheFactoryBean =
new EhCacheManagerFactoryBean();
ehCacheFactoryBean.setConfigLocation(
new ClassPathResource("com/habuma/spittr/cache/ehcache.xml"));
return ehCacheFactoryBean;
}
```
configuration
```xml
<ehcache>
<cache name="spittleCache"
maxBytesLocalHeap="50m"
timeToLiveSeconds="100">
</cache>
</ehcache>
```

**6) RedisCacheManager ** (Redis  REmote DIctionary Server)
```java
@Configuration
@EnableCaching
public class CachingConfig {
@Bean
public CacheManager cacheManager(RedisTemplate redisTemplate) {
return new RedisCacheManager(redisTemplate);
}
@Bean
public JedisConnectionFactory redisConnectionFactory() {
JedisConnectionFactory jedisConnectionFactory =
new JedisConnectionFactory();
jedisConnectionFactory.afterPropertiesSet();
return jedisConnectionFactory;
}
@Bean
public RedisTemplate<String, String> redisTemplate(
RedisConnectionFactory redisCF) {
RedisTemplate<String, String> redisTemplate =
new RedisTemplate<String, String>();
redisTemplate.setConnectionFactory(redisCF);
redisTemplate.afterPropertiesSet();
return redisTemplate;
}
}
```

**WORKING WITH MULTIPLE CACHE MANAGERS: CompositeCacheManager**

CompositeCacheManager is configured with one or more cache managers and iterates
over them all as it tries to find a previously cached value. The following listing
shows how to create a CompositeCacheManager bean that iterates over a JCacheCache-
Manager, an EhCacheCacheManager, and a RedisCacheManager.
```java
@Bean
public CacheManager cacheManager(
net.sf.ehcache.CacheManager cm,
javax.cache.CacheManager jcm) {
CompositeCacheManager cacheManager = new CompositeCacheManager();
List<CacheManager> managers = new ArrayList<CacheManager>();
managers.add(new JCacheCacheManager(jcm));
managers.add(new EhCacheCacheManager(cm))
managers.add(new RedisCacheManager(redisTemplate()));
cacheManager.setCacheManagers(managers);
return cacheManager;
}
```

#####13.2 Annotating methods for caching
When you enable caching in Spring, an aspect is created that triggers off one or more
of Spring’s caching annotations.

Spring provides **four annotations** for declaring caching rules. All annotations can be placed either
on a method or on a class.
```
@Cacheable (findOrAdd, only for non-void method):  Indicates that Spring should look in a cache for the method’s return value
             before invoking the method. If the value is found, the cached value is returned.
             If not, then the method is invoked and the return value is put in the cache.
@CachePut (add, only for non-void method):   Indicates that Spring should put the method’s return value in a cache. The
             cache isn’t checked prior to method invocation, and the method is always
             invoked.
@CacheEvict (remove, both void and non-void methods ): Indicates that Spring should evict one or more entries from a cache.
@Caching (addAll):    A grouping annotation for applying multiples of the other caching annotations at once.
```

#####13.2.1 Populating the cache
@Cacheable and @CachePut share a common set of attributes.
```
Attribute:  Type:  Description
1) value: String[]:  The name(s) of the cache(s) to use
2) condition:  String:  A SpEL expression that, if it evaluates to false, results in caching not being applied to the method call
3) key:  String: A SpEL expression to calculate a custom cache key
4) unless: String: A SpEL expression that, if it evaluates to true, prevents the return value from being put in the cache
```
Example: When findOne() is called, the caching aspect intercepts the call and looks for a previously
         returned value in the cache named spittleCache. The cache key is the id
         parameter passed to the findOne() method. If a value is found for that key, the found
         value will be returned and the method won’t be invoked. On the other hand, if no
         value is found, then the method will be invoked and the returned value will be put in
         the cache, ready for the next time findOne() is called.
```java
@Cacheable("spittleCache")
public Spittle findOne(long id) {
try {
return jdbcTemplate.queryForObject(
SELECT_SPITTLE_BY_ID,
new SpittleRowMapper(),
id);
} catch (EmptyResultDataAccessException e) {
return null;
}
}
```

Commonly use:
When you annotate the interface method, the @Cacheable annotation will be inherited
by all implementations of SpittleRepository, and the same caching rules will be
applied.

**PUTTING VALUES IN THE CACHE**
```
An @CachePut-annotated method is always invoked and its return value is placed in the cache.
This offers a handy way to preload a cache before anyone comes asking.
```
```java
@CachePut("spittleCache")
Spittle save(Spittle spittle);
```
Default key:is based on the parameters to the method.
In above case, spittle object is not suit for a key.

**So how you can customize the cache key**
```java
@CachePut(value="spittleCache", key="#result.id")
Spittle save(Spittle spittle);
```
```
Spring offers several SpEL extensions specifically for defining cache rules:
#root.args:  The arguments passed in to the cached method, as an array
#root.caches:  The caches this method is executed against, as an array
#root.target:  The target object
#root.targetClass:  The target object’s class; a shortcut for #root.target.class
#root.method: The cached method
#root.methodName:  The cached method’s name; a shortcut for #root.method.name
#result: The return value from the method call (not available with @Cacheable)
#Argument: The name of any method argument (such as #argName) or argument index (such as #a0 or #p0)
```

**CONDITIONAL CACHING**
@Cacheable and @CachePut offer two attributes for conditional caching: unless and condition.

Both are given a SpEL expression. If the unless attribute’s SpEL expression evaluates to true,
then the data returned from the cached method isn’t placed in the cache.

Similarly, if the condition attribute’s SpEL expression evaluates to false, then caching is
effectively disabled for the method.

```java
@Cacheable(value="spittleCache"
unless="#result.message.contains('NoCache')")
Spittle findOne(long id);

@Cacheable(value="spittleCache"
unless="#result.message.contains('NoCache')"
condition="#id >= 10")
Spittle findOne(long id);
```

#####13.2.2 Removing cache entries
```
If an @CacheEvict annotated method is called, one or more entries are removed from the cache.

Applications: Any time a cached value is no longer valid, you should make sure it’s removed from
the cache so that future cache hits won’t return stale or otherwise nonexistent data.
```
```java
@CacheEvict("spittleCache")
void remove(long spittleId);
```
NOTE Unlike @Cacheable and @CachePut, @CacheEvict can be used on void
methods. @Cacheable and @CachePut require a non-void return value, which
is the item to place in the cache. But because @CacheEvict is only removing
items from the cache, it can be placed on any method, even a void one.

```
The @CacheEvict annotation’s attributes specify which cache entries should be removed.

1)value: String[]: The name(s) of the cache(s) to use.
2) key:  String:  A SpEL expression to calculate a custom cache key.
3) condition: String: A SpEL expression that, if it evaluates to false, results in caching not being applied to the method call.
4) allEntries :boolean: If true, all entries in the specified cache(s) should be removed.
5) beforeInvocation: boolean:  If true, the entries are removed from the cache before the method is invoked. If false (the default), the
                               entries are removed after a successful method invocation.
```


#####13.3 Declaring caching in XML
Why use xml configuration:
```
1) You don’t feel comfortable putting Spring-specific annotations in your source code.
2) You want to apply caching to beans for which you don’t own the source code.
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:cache="http://www.springframework.org/schema/cache"
xmlns:aop="http://www.springframework.org/schema/aop"
xsi:schemaLocation="http://www.springframework.org/schema/aop
http://www.springframework.org/schema/aop/spring-aop.xsd
http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/cache
http://www.springframework.org/schema/cache/spring-cache.xsd">
<aop:config>
<aop:advisor advice-ref="cacheAdvice"
pointcut=
"execution(* com.habuma.spittr.db.SpittleRepository.*(..))"/>
</aop:config>
<cache:advice id="cacheAdvice">
<cache:caching>
<cache:cacheable
cache="spittleCache"
method="findRecent" />
<cache:cacheable
cache="spittleCache" method="findOne" />
<cache:cacheable
cache="spittleCache"
method="findBySpitterId" />
<cache:cache-put
cache="spittleCache"
method="save"
key="#result.id" />
<cache:cache-evict
cache="spittleCache"
method="remove" />
</cache:caching>
</cache:advice>
<bean id="cacheManager" class=
"org.springframework.cache.concurrent.ConcurrentMapCacheManager"
/>
</beans>

```


###Chapter 14: Securing methods
By securing both the web layer of your application and the methods behind the
scenes, you can be sure that no logic will be executed unless the user is authorized.

In doing so, we’ll declare security rules that prevent a method from being executed
unless the user for whom it is being executed has the authority to execute it.

Spring Security provides three different kinds of security annotations:
```
1) Spring Security’s own @Secured
2) JSR-250’s @RolesAllowed
3) Expression-driven annotations, with @PreAuthorize, @PostAuthorize, @PreFilter, and @PostFilter
```
The @Secured and @RolesAllowed annotations are the simplest options, restricting
access based on what authorities have been granted to the user.

#####14.1.1 Restricting method access with @Secured
```java
@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true)
public class MethodSecurityConfig
extends GlobalMethodSecurityConfiguration {
}
```
One drawback of the @Secured annotation is that it’s a Spring-specific annotation.
If you’re more comfortable using annotations defined in Java standards, then perhaps
you should consider using @RolesAllowed instead.

#####14.1.2 Using JSR-250’s @RolesAllowed with Spring Security
@EnableGlobalMethodSecurity’s jsr250Enabled attribute to true:
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled=true)
public class MethodSecurityConfig
extends GlobalMethodSecurityConfiguration {
}


#####14.2 Using expressions for method-level security
Spring Security 3.0 offers four new annotations that can be used to secure methods with
SpEL expressions.
```
Annotations Description
@PreAuthorize:  Restricts access to a method before invocation based on the result of evaluating an expression
@PostAuthorize: Allows a method to be invoked, but throws a security exception if the expression evaluates to false
@PostFilter: Allows a method to be invoked, but filters the results of that method based on an expression
@PreFilter:  Allows a method to be invoked, but filters input prior to entering the method
```
```java
@PreAuthorize("hasRole('ROLE_SPITTER')")
public void addSpittle(Spittle spittle) {
// ...
}

@PreAuthorize(
"(hasRole('ROLE_SPITTER') and #spittle.text.length() <= 140)"
+"or hasRole('ROLE_PREMIUM')")
public void addSpittle(Spittle spittle) {
// ...
}

@PostAuthorize("returnObject.spitter.username == principal.username")
public Spittle getSpittleById(long id) {
// ...
}

@PreAuthorize("hasAnyRole({'ROLE_SPITTER', 'ROLE_ADMIN'})")
@PostFilter( "hasRole('ROLE_ADMIN') || "
+ "filterObject.spitter.username == principal.name")
public List<Spittle> getOffensiveSpittles() {
...
}


@PreAuthorize("hasAnyRole({'ROLE_SPITTER', 'ROLE_ADMIN'})")
@PreFilter( "hasRole('ROLE_ADMIN') || "
+ "targetObject.spitter.username == principal.name")
public void deleteSpittles(List<Spittle> spittles) { ... }
```

```java
package spittr.security;
import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import spittr.Spittle;
public class SpittlePermissionEvaluator implements PermissionEvaluator {
private static final GrantedAuthority ADMIN_AUTHORITY =
new GrantedAuthorityImpl("ROLE_ADMIN");
public boolean hasPermission(Authentication authentication,
Object target, Object permission) {
if (target instanceof Spittle) {
Spittle spittle = (Spittle) target;
String username = spittle.getSpitter().getUsername();
if ("delete".equals(permission)) {
return isAdmin(authentication) ||
username.equals(authentication.getName());
}
}
throw new UnsupportedOperationException(
"hasPermission not supported for object <" + target
+ "> and permission <" + permission + ">");
}
public boolean hasPermission(Authentication authentication,
Serializable targetId, String targetType, Object permission) {
throw new UnsupportedOperationException();
}
private boolean isAdmin(Authentication authentication) {
return authentication.getAuthorities().contains(ADMIN_AUTHORITY);
}
}
```
In this chapter, we looked at six annotations that can be placed on methods to
declare security constraints. For simple, authorities-oriented security, Spring Security’s
@Secured annotation or the standards-based @RolesAllowed come in handy. When
the security rules get more interesting, @PreAuthorize and @PostAuthorize and
SpEL provide more power. You also saw how to filter a method’s inputs and outputs
using SpEL expressions given to @PreFilter and @PostFilter.
Finally, we looked at how you can make your security rules easier to maintain, test,
and debug by defining a custom expression evaluator that works behind the scenes of
the hasPermission() function in SpEL.

##Part 4: Integrating Spring
In chapter 15, “Working with remote services,” you’ll learn how to expose
your application objects as remote services. You’ll learn how to transparently
access remote services as though they’re any other object in your application. In
doing so, you’ll explore various remoting technologies, including RMI, Hessian/
Burlap, and SOAP web services with JAX-WS.

In contrast to RPC-style remote services presented in chapter 15, chapter 16,
"Creating Rest APIs with Spring MVC," explores how to build RESTful services
that are focused on application resources using Spring MVC.

Chapter 17, “Messaging with Spring,” explores a different approach to application
integration by showing how Spring can be used with the Java Message Service
(JMS) and the Advanced Message Queuing Protocol (AMQP) to achieve
asynchronous communication between applications.

Increasingly, web applications are expected to be responsive and show near
real-time data. Chapter 18, “Messaging with WebSocket and STOMP,” showcases
Spring’s new support for building asynchronous communication between a
server and its web clients.

Another form of asynchronous communication isn’t necessarily application to
application. Chapter 19, “Sending email with Spring,” shows how to send
asynchronous messages to people in the form of email using Spring.

Management and monitoring of Spring beans is the subject of chapter 20, “Managing
Spring beans with JMX.” In this chapter, you’ll learn how Spring can automatically
expose beans configured in Spring as JMX MBeans.

Chapter 21, “Simplifying Spring development with Spring Boot,” presents an exciting
new game-changing development in Spring. You’ll see how Spring Boot takes away the
chore of writing much of the boilerplate configuration that is typical in Spring applications
and leaves you to focus on implementing business functionality.

###Chapter 15: Working with remote services
Several remoting technologies are available to you as a Java developer, including these:
```
1) Remote Method Invocation (RMI)
2) Caucho’s Hessian and Burlap
3) Spring’s own HTTP-based remoting
4) Web services with JAX-RPC and JAX-WS
```
#####15.1 An overview of Spring remoting
Remoting is a conversation between a client application and a service.

The conversation between the other applications and Spittr begins with a remote
procedure call (RPC) from the client applications. On the surface, an RPC is similar to a
call to a method on a local object. Both are **synchronous operations**, **blocking execution**
in the calling code until the called procedure is complete.

Spring supports RPC via several remoting technologies.
```
1) Remote Method Invocation (RMI): Accessing/exposing Java-based services when network constraints
                                such as firewalls aren’t a factor.
2) Hessian or Burlap: Accessing/exposing Java-based services over HTTP when network constraints
                   are a factor. Hessian is a binary protocol, whereas Burlap is XML-based.
3) HTTP invoker:  Accessing/exposing Spring-based services when network constraints
                  are a factor and you desire Java serialization over XML or proprietary serialization.
4) JAX-RPC and JAX-WS:  Accessing/exposing platform-neutral, SOAP-based web services.
```

In all models, services can be configured into your application as Spring-managed
beans. This is accomplished using a proxy factory bean that enables you to wire
remote services into properties of your other beans as if they were local objects.

The client makes calls to the proxy as if the proxy were providing the service functionality.
The proxy communicates with the remote service on behalf of the client. It handles
the details of connecting and making remote calls to the remote service.


###Chapter 16: Creating REST APIs with Spring MVC
Representational State Transfer (REST) has emerged as a popular information-centric alternative to traditional
SOAP-based web services. Whereas SOAP typically focused on actions and processing, REST’s concern is with the
data being handled.


#####16.1 Getting REST
To understand what REST is all about, it helps to break down the acronym into its constituent parts:

```
1) Representational—REST resources can be represented in virtually any form,
including XML, JavaScript Object Notation (JSON), or even HTML—whatever
form best suits the consumer of those resources.
2) State—When working with REST, you’re more concerned with the state of a resource than with the actions you
can take against resources.
3) Transfer—REST involves transferring resource data, in some representational form, from one application to another.
```
REST is about transferring the state of resources—in a representational
form that is most appropriate for the client or server—from a server to a client
(or vice versa)

These HTTP methods are often mapped to CRUD verbs as follows:
```
1) Create—POST
2) Read—GET
3) Update—PUT or PATCH
4) Delete—DELETE
```
Even though this is the common mapping of HTTP methods to CRUD verbs, it’s not a
strict requirement. There are cases where PUT can be used to create a new resource
and POST can be used to update a resource. In fact, the non-idempotent nature of
POST makes it a rogue method, capable of performing operations that don’t easily fit
the semantics of the other HTTP methods.

**How Spring supports REST**
Spring supports the creation of REST resources in the following ways:
```
1) Controllers can handle requests for all HTTP methods, including the four primary
   REST methods: GET, PUT, DELETE, and POST. Spring 3.2 and higher also supports
   the PATCH method.
2) The @PathVariable annotation enables controllers to handle requests for
   parameterized URLs (URLs that have variable input as part of their path).
3) Resources can be represented in a variety of ways using Spring views and view
   resolvers, including View implementations for rendering model data as XML,
   JSON, Atom, and RSS.
4) The representation best suited for the client can be chosen using ContentNegotiatingViewResolver.
   View-based rendering can be bypassed altogether using the @ResponseBody
   annotation and various HttpMethodConverter implementations.
5) Similarly, the @RequestBody annotation, along with HttpMethodConverter
   implementations, can convert inbound HTTP data into Java objects passed in to a controller’s handler methods.
6) Spring applications can consume REST resources using RestTemplate.
```
Throughout this chapter, we’ll explore these features that make Spring more RESTful
starting with how to produce REST resources using Spring MVC. Then in section 16.4,
we’ll switch to the client side of REST and see how to consume these resources.


#####16.2 Creating your first REST endpoint
Recommend that you at minimum support JSON. JSON is a clear winner because essentially no marshaling/demarshaling is
required to use JSON data in JavaScript.

Spring offers two options to transform a resource’s Java representation into the
representation that’s shipped to the client:
```
1) Content negotiation—A view is selected that can render the model into a representation
to be served to the client.
2) Message conversion—A message converter transforms an object returned from
the controller into a representation to be served to the client.
```
#####16.2.1 Negotiating resource representation
If the client wants JSON data, then an HTML-rendering view won’t do—even if the view
name matches.

Spring’s ContentNegotiatingViewResolver is a special view resolver that takes the
content type that the client wants into consideration.
```java
@Bean
public ViewResolver cnViewResolver() {
return new ContentNegotiatingViewResolver();
}
```
content-negotiation two-step:
```
1) Determine the requested media type(s).
2) Find the best view for the requested media type(s).
```

**DETERMINING THE REQUESTED MEDIA TYPES**
ContentNegotiatingViewResolver
```
1) URL’s file extension(.json, .xml, or .html)
2) Request header
3) Default content type

```
**INFLUENCING HOW MEDIA TYPES ARE CHOSEN**

ContentNegotiationManager can change how it behaves
```
Specify a default content type to fall back to if a content type can’t be derived from the request.
1) Specify a content type via a request parameter.
2) Ignore the request’s Accept header.
3) Map request extensions to specific media types.
4) Use the Java Activation Framework (JAF) as a fallback option for looking up media types from extensions.
```
There are three ways to configure a ContentNegotiationManager:
```
1) Directly declare a bean whose type is ContentNegotiationManager.
2) Create the bean indirectly via ContentNegotiationManagerFactoryBean.
3) Override the configureContentNegotiation() method of WebMvcConfigurerAdapter.
```

**THE BENEFITS AND LIMITATIONS OF CONTENTNEGOTIATINGVIEWRESOLVER**
**Benefits**: The key benefit of using ContentNegotiatingViewResolver is that it layers REST
resource representation on top of the Spring MVC with no change in controller code.
The same controller method that serves human-facing HTML content can also serve
JSON or XML to a non-human client.

Content negotiation is a convenient option when there’s a great deal of overlap
between your human and non-human interfaces. In practice, though, human-facing
views rarely deal at the same level of detail as a REST API. The benefit of Content-
NegotiatingViewResolver isn’t realized when there isn’t much overlap between the
human and non-human interfaces.

**Limitations**: ContentNegotiatingViewResolver also has a serious limitation. As a View-
Resolver implementation, it only has an opportunity to determine how a resource is
rendered to a client. It has no say in what representations a controller can consume
from the client. If the client is sending JSON or XML, then ContentNegotiatingViewResolver isn’t much help.
There’s one more gotcha associated with using ContentNegotiatingViewResolver.
The View chosen renders the model—not the resource—to the client. This is a subtle but important distinction.


**Summary**
Because of these limitations, I generally prefer not to use ContentNegotiating-
ViewResolver. Instead, I lean heavily toward using Spring’s message converters for
producing resource representations. Let’s see how you can employ Spring’s message
converters in your controller methods

#####16.2.2 Working with HTTP message converters
When using message conversion, DispatcherServlet doesn’t bother with ferrying model data to a view. In fact, there is
no model, and there is no view. There is only data produced by the controller and a
resource representation produced when a message converter transforms that data.


Spring provides several HTTP message converters that marshal resource representations
to and from various Java types.
```
i AtomFeedHttpMessageConverter: Converts Rome Feed objects to and from Atom feeds (media type application/atom+xml).
                                Registered if the Rome library is present on the classpath.

ii BufferedImageHttpMessageConverter: Converts BufferedImage to and from image binary data.

iii ByteArrayHttpMessageConverter: Reads and writes byte arrays. Reads from all media types (*/*), and writes as application/ octet-stream.

iv FormHttpMessageConverter: Reads content as application/x-www-form-urlencoded into a MultiValueMap<String,String>. Also writes MultiValueMap<String,String> as application/x-www-form-urlencoded and MultiValueMap<String, Object> as multipart/form-data.

v Jaxb2RootElementHttpMessageConverter: Reads and writes XML (either text/xml or application/xml) to and from JAXB2-annotated objects. Registered if JAXB v2 libraries are present on the classpath.

vi MappingJacksonHttpMessageConverter: Reads and writes JSON to and from typed objects or untyped HashMaps. Registered if the Jackson JSON library is present on the classpath.

vii MappingJackson2HttpMessageConverter: Reads and writes JSON to and from typed objects or untyped HashMaps. Registered if the Jackson 2 JSON library is present on the classpath.

viii MarshallingHttpMessageConverter: Reads and writes XML using an injected marshaler and unmarshaler. Supported (un)marshalers include Castor, JAXB2, JIBX, XMLBeans, and XStream.

ix ResourceHttpMessageConverter: Reads and writes org.springframework.core.io.Resource.

x RssChannelHttpMessageConverter Reads and writes RSS feeds to and from Rome Channel objects. Registered if the Rome library is present on the classpath.

xi SourceHttpMessageConverter Reads and writes XML to and from javax.xml.transform.Source objects.

xii StringHttpMessageConverter:  Reads all media types (*/*) into a String. Writes String to text/plain.

xiii XmlAwareFormHttpMessageConverter An extension of FormHttpMessageConverter that adds support for XML-based parts using a SourceHttpMessageConverter
```

**RETURNING RESOURCE STATE IN THE RESPONSE BODY**
```java
@RequestMapping(method=RequestMethod.GET,
produces="application/json")
public @ResponseBody List<Spittle> spittles(
@RequestParam(value="max",
defaultValue=MAX_LONG_AS_STRING) long max,
@RequestParam(value="count", defaultValue="20") int count) {
return spittleRepository.findSpittles(max, count);
}
```

**RECEIVING RESOURCE STATE IN THE REQUEST BODY**
```java
@RequestMapping(
method=RequestMethod.POST
consumes="application/json")
public @ResponseBody
Spittle saveSpittle(@RequestBody Spittle spittle) {
return spittleRepository.save(spittle);
}
```

**DEFAULTING CONTROLLERS FOR MESSAGE CONVERSION**
Using @RestController instead of @Controller, Spring applies message conversion to all handler methods in the controller.
```java
@RestController
@RequestMapping("/spittles")
public class SpittleController {
private static final String MAX_LONG_AS_STRING="9223372036854775807";
private SpittleRepository spittleRepository;
@Autowired
public SpittleController(SpittleRepository spittleRepository) {
this.spittleRepository = spittleRepository;
}
@RequestMapping(method=RequestMethod.GET)
public List<Spittle> spittles(
@RequestParam(value="max",
defaultValue=MAX_LONG_AS_STRING) long max,
@RequestParam(value="count", defaultValue="20") int count) {
return spittleRepository.findSpittles(max, count);
}
@RequestMapping(
method=RequestMethod.POST
consumes="application/json")
public Spittle saveSpittle(@RequestBody Spittle spittle) {
return spittleRepository.save(spittle);
}
}

```

#####16.3 Serving more than resources
The @ResponseBody annotation is helpful in transforming a Java object returned from
a controller to a resource representation to send to the client. As it turns out, serving
a resource’s representation to a client is only part of the story. A good REST API does
more than transfer resources between the client and server. It also gives the client
additional metadata to help the client understand the resource or know what has just
taken place in the request.

**16.3.1 Communicating errors to the client**
**16.3.2 Setting headers in the response**

#####16.3.1 Communicating errors to the client
Spring offers a few options for dealing with such scenarios:
```
1) Status codes can be specified with the @ResponseStatus annotation.
2) Controller methods can return a ResponseEntity that carries more metadata concerning the response.
3) An exception handler can deal with the error cases, leaving the handler methods to focus on the happy path.
```

**WORKING WITH ResponseEntity**
```java
@RequestMapping(value="/{id}", method=RequestMethod.GET)
public ResponseEntity<Spittle> spittleById(@PathVariable long id) {
Spittle spittle = spittleRepository.findOne(id);
HttpStatus status = spittle != null ?
HttpStatus.OK : HttpStatus.NOT_FOUND;
return new ResponseEntity<Spittle>(spittle, status);
}
```

#####16.3.2 Setting headers in the response
```java
@RequestMapping(
method=RequestMethod.POST
consumes="application/json")
public ResponseEntity<Spittle> saveSpittle(
@RequestBody Spittle spittle) {
Spittle spittle = spittleRepository.save(spittle);
HttpHeaders headers = new HttpHeaders();
URI locationUri = URI.create(
"http://localhost:8080/spittr/spittles/" + spittle.getId());
headers.setLocation(locationUri);
ResponseEntity<Spittle> responseEntity =
new ResponseEntity<Spittle>(
spittle, headers, HttpStatus.CREATED)
return responseEntity;
}
```

Using a UriComponentsBuilder to construct the location URI
```java
@RequestMapping(
method=RequestMethod.POST
consumes="application/json")
public ResponseEntity<Spittle> saveSpittle(
@RequestBody Spittle spittle,
UriComponentsBuilder ucb) {
Spittle spittle = spittleRepository.save(spittle);
HttpHeaders headers = new HttpHeaders();
URI locationUri =
ucb.path("/spittles/")
.path(String.valueOf(spittle.getId()))
.build()
.toUri();
headers.setLocation(locationUri);
ResponseEntity<Spittle> responseEntity =
new ResponseEntity<Spittle>(
spittle, headers, HttpStatus.CREATED)
return responseEntity;
}
```


#####16.4 Consuming REST resources
RestTemplate defines 11 unique operations, each of which is overloaded for a total of 36 methods.
```
delete():  Performs an HTTP DELETE request on a resource at a specified URL

exchange(): Executes a specified HTTP method against a URL, returning a ResponseEntity containing an object mapped from the response body

execute() Executes a specified HTTP method against a URL, returning an object mapped from the response body

getForEntity() Sends an HTTP GET request, returning a ResponseEntity containing an object mapped from the response body

getForObject() Sends an HTTP GET request, returning an object mapped from a response body

headForHeaders() Sends an HTTP HEAD request, returning the HTTP headers for the specified resource URL

optionsForAllow() Sends an HTTP OPTIONS request, returning the Allow header for the specified URL

postForEntity() POSTs data to a URL, returning a ResponseEntity containing an object mapped from the response body

postForLocation() POSTs data to a URL, returning the URL of the newly created resource

postForObject() POSTs data to a URL, returning an object mapped from the response body

put() PUTs resource data to the specified URL
```


###Chapter 17: Messaging in Spring
Using RMI, Hessian, Burlap, the HTTP invoker, and web services to enable synchronous communication in which a client
application directly contacts a remote service and waits for the remote procedure to complete before continuing.

Asynchronous messaging is a way of indirectly sending messages from one application to another without waiting for a
response. Asynchronous messaging (one way communication)  VS synchronous messaging (two way communication):
```
1) Synchronous communication implies waiting
2) The client is coupled to the service through the service’s interface
3) The client is coupled to the service’s location.
4) The client is coupled to the service’s availability

NO WAITING

MESSAGE ORIENTATION AND DECOUPLING

LOCATION INDEPENDENCE:
In the point-to-point model, it’s possible to take advantage of location independence
to create a cluster of services. If the client is unaware of the service’s location,
and if the service’s only requirement is that it must be able to access the message broker,
there’s no reason multiple services can’t be configured to pull messages from the
same queue. If the service is overburdened and falling behind in its processing, all you
need to do is start a few more instances of the service to listen to the same queue.

Location independence takes on another interesting side effect in the publish/
subscribe model. Multiple services could all subscribe to a single topic, receiving
duplicate copies of the same message. But each service could process that message differently.
For example, let’s say you have a set of services that together process a message
that details the new hire of an employee. One service might add the employee to
the payroll system, another adds them to the HR portal, and yet another makes sure
the employee is given access to the systems they’ll need to do their job. Each service
works independently on the same data that they all received from a topic.


GUARANTEED DELIVERY:
In order for a client to communicate with a synchronous service, the service must be
listening at the IP address and port specified. If the service were to go down or otherwise
become unavailable, the client wouldn’t be able to proceed.
But when sending messages asynchronously, the client can rest assured that its
messages will be delivered. Even if the service is unavailable when a message is sent,
the message will be stored until the service is available again.
```
Challenges:
```
1. What if delivery failed
2. What if queues reach the max number
```



**Java MessageService (JMS)**
**Advanced Message Queuing Protocol (AMQP)**

#####17.1 A brief introduction to asynchronous messaging
Indirection is the key to asynchronous messaging.

When one application sends a message to another, there’s no direct link between the two applications.
Instead, the sending application places the message in the hands of a service that will ensure delivery to the receiving application.

There are two main actors in asynchronous messaging: **message brokers** and **destinations**.

**message broker** ensures that the message is delivered to the specified destination.

Destinations are only concerned about where messages will be picked up—not who will pick them up.

There are two common types of destinations: **queues** (point to point)and **topics** (publish/subscribe).


**POINT-TO-POINT MESSAGING**
```
In the point-to-point model, each message has exactly one sender and one receiver.

Because the message is removed from the queue as it’s delivered, it’s guaranteed that
the message will be delivered to only one receiver.

Likewise, with point-to-point messaging, if multiple receivers are listening to a
queue, there’s no way of knowing which one will process a specific message.

This uncertainty is a good thing, because it enables an application to scale up message processing
by adding another listener to the queue.
```

**PUBLISH-SUBSCRIBE MESSAGING**
```
In the publish/subscribe messaging model, messages are sent to a topic.

All subscribers to a topic receive a copy of the message.

The magazine analogy breaks down when you realize that the publisher has no
idea who its subscribers are. The publisher only knows that its message will be published
to a particular topic—not who’s listening to that topic. This also implies that
the publisher has no idea how the message will be processed.
```

#####17.2 Sending messages with JMS
Using JmsTemplate, it’s easy to send messages across queues and topics from the producer
side and also to receive those messages on the consumer side.

Spring also supports the notion of message-driven POJOs: simple Java objects that react to messages
arriving on a queue or topic in an asynchronous fashion.

#####17.2.1 Setting up a message broker in Spring
**CREATING A CONNECTION FACTORY**
```xml
<bean id="connectionFactory"
class="org.apache.activemq.spring.ActiveMQConnectionFactory"
p:brokerURL="tcp://localhost:61616"/>
```

**DECLARING AN ACTIVEMQ MESSAGE DESTINATION**
```xml
<bean id="queue"
class="org.apache.activemq.command.ActiveMQQueue"
c:_="spitter.queue" />

Similarly, the following <bean> declares a topic for ActiveMQ:

<bean id="topic"
class="org.apache.activemq.command.ActiveMQTopic"
c:_="spitter.queue" />

```

#####17.2.2 Using Spring’s JMS template
NON-Spring: Sending a message using conventional JMS
```java
ConnectionFactory cf =
new ActiveMQConnectionFactory("tcp://localhost:61616");
Connection conn = null;
Session session = null;
try {
conn = cf.createConnection();
session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
Destination destination = new ActiveMQQueue("spitter.queue");
MessageProducer producer = session.createProducer(destination);
TextMessage message = session.createTextMessage();
message.setText("Hello world!");
producer.send(message);
} catch (JMSException e) {
// handle exception?
} finally {
try {
if (session != null) {
session.close();
}
if (conn != null) {
conn.close();
}
} catch (JMSException ex) {
}
}
```
**WORKING WITH JMS TEMPLATES**
```
JmsTemplate takes care of creating a connection, obtaining a session, and ultimately sending or receiving messages.
```
Spring’s JmsTemplate catches standard JMSExceptions and rethrows them as
unchecked subclasses of Spring’s own JmsException.
```
DestinationResolutionException:  Spring-specific—thrown when Spring can’t resolve a destination name

IllegalStateException:  IllegalStateException

InvalidClientIDException InvalidClientIDException

InvalidDestinationException InvalidDestinationException

InvalidSelectorException InvalidSelectorException

JmsSecurityException JmsSecurityException

ListenerExecutionFailedException Spring-specific—thrown when execution of a listener method fails

MessageConversionException Spring-specific—thrown when message conversion fails

MessageEOFException MessageEOFException

MessageFormatException MessageFormatException

MessageNotReadableException MessageNotReadableException

MessageNotWriteableException MessageNotWriteableException

ResourceAllocationException ResourceAllocationException

SynchedLocalTransactionFailedException Spring-specific—thrown when a synchronized local transaction fails to complete

TransactionInProgressException TransactionInProgressException

TransactionRolledBackException TransactionRolledBackException

UncategorizedJmsException Spring-specific—thrown when no other exception applies
```

**SENDING MESSAGES**
```java
public interface AlertService {
void sendSpittleAlert(Spittle spittle);
}

public class AlertServiceImpl implements AlertService {
private JmsOperations jmsOperations;
@Autowired
public AlertServiceImpl(JmsOperations jmsOperatons) {
this.jmsOperations = jmsOperations;
}
public void sendSpittleAlert(final Spittle spittle) {
jmsOperations.send(
"spittle.alert.queue",
new MessageCreator() {
public Message createMessage(Session session)
throws JMSException {
return session.createObjectMessage(spittle);
}
}
);
}
}
}
```

**SETTING A DEFAULT DESTINATION**
```
Instead of explicitly specifying a destination each time you send a message, you can
opt for wiring a default destination into JmsTemplate:
<bean id="jmsTemplate"
class="org.springframework.jms.core.JmsTemplate"
c:_-ref="connectionFactory"
p:defaultDestinationName="spittle.alert.queue" />
```


**CONVERTING MESSAGES WHEN SENDING**
convertAndSend() method doesn’t take a MessageCreator as an argument.
That’s because convertAndSend() uses a built-in message converter to create
the message for you.
```java
public void sendSpittleAlert(Spittle spittle) {
jmsOperations.convertAndSend(spittle);
}

public interface MessageConverter {
Message toMessage(Object object, Session session)
throws JMSException, MessageConversionException;
Object fromMessage(Message message)
throws JMSException, MessageConversionException;
}
```
Spring offers several message converters for common conversion tasks. (All of these
message converters are in the org.springframework.jms.support.converter package.)
```
1) MappingJacksonMessageConverter Uses the Jackson JSON library to convert messages to and from JSON

2) MappingJackson2MessageConverter Uses the Jackson 2 JSON library to convert messages to and from JSON

3) MarshallingMessageConverter Uses JAXB to convert messages to and from XML

4) SimpleMessageConverter Converts Strings to/from TextMessage, byte arrays to/ from BytesMessage, Maps to/from MapMessage,
   and Serializable objects to/from ObjectMessage
```

```xml
<bean id="messageConverter"
class="org.springframework.jms.support.converter.MappingJacksonMessageConverter" />

Then you can wire it into JmsTemplate like this:

<bean id="jmsTemplate"
class="org.springframework.jms.core.JmsTemplate"
c:_-ref="connectionFactory"
p:defaultDestinationName="spittle.alert.queue"
p:messageConverter-ref="messageConverter" />

```

**CONSUMING MESSAGES**
```java
public Spittle receiveSpittleAlert() {
try {
ObjectMessage receivedMessage =
(ObjectMessage) jmsOperations.receive();
return (Spittle) receivedMessage.getObject();
} catch (JMSException jmsException) {
throw JmsUtils.convertJmsAccessException(jmsException);
}
}
```
The big downside of consuming messages with JmsTemplate is that both the
receive() and receiveAndConvert() methods are synchronous.

This means the receiver must wait patiently for the message to arrive, because those methods will
block until a message is available (or until a timeout condition occurs).

Doesn’t it seem odd to synchronously consume a message that was asynchronously sent?
That’s where message-driven POJOs come in handy. Let’s see how to receive messages asynchronously using components that
react to messages rather than wait on them.

#####17.2.3 Creating message-driven POJOs
message driven bean (MDB) are EJBs that process messages asynchronously.

**CREATING A MESSAGE LISTENER**
```java
@MessageDriven(mappedName="jms/spittle.alert.queue")
public class SpittleAlertHandler implements MessageListener {
@Resource
private MessageDrivenContext mdc;
public void onMessage(Message message) {
...
}
}
```

Spring MDP that asynchronously receives and processes messages:
```
public class SpittleAlertHandler {
public void handleSpittleAlert(Spittle spittle) {
// ... implementation goes here...
}
}
```


**CONFIGURING MESSAGE LISTENERS**
The trick to empowering a POJO with message-receiving abilities is to configure it as a
message listener in Spring. Spring’s jms namespace provides everything you need to
do that. First, you must declare the handler as a <bean>:
```xml
<bean id="spittleHandler"
class="com.habuma.spittr.alerts.SpittleAlertHandler" />
```
Then, to turn SpittleAlertHandler into a message-driven POJO, you can declare the
bean to be a message listener:
```xml
<jms:listener-container connection-factory="connectionFactory">
<jms:listener destination="spitter.alert.queue"
ref="spittleHandler" method="handleSpittleAlert" />
</jms:listener-container>
```
The <jms:listener> element is used to identify a bean and a method that should
handle incoming messages. For the purposes of handling spittle alert messages, the
ref element refers to your spittleHandler bean. When a message arrives on spitter.alert.queue
(as designated by the destination attribute), the spittleHandler bean’s handleSpittleAlert()
method gets the call (per the method attribute).


#####17.2.4 Using message-based RPC
**EXPORTING JMS-BASED SERVICES**
```java
@Component("alertService")
public class AlertServiceImpl implements AlertService {
private JavaMailSender mailSender;
private String alertEmailAddress;
public AlertServiceImpl(JavaMailSender mailSender,
String alertEmailAddress) {
this.mailSender = mailSender;
this.alertEmailAddress = alertEmailAddress;
}
public void sendSpittleAlert(final Spittle spittle) {
SimpleMailMessage message = new SimpleMailMessage();
String spitterName = spittle.getSpitter().getFullName();
message.setFrom("noreply@spitter.com");
message.setTo(alertEmailAddress);
message.setSubject("New spittle from " + spitterName);
message.setText(spitterName + " says: " + spittle.getText());
mailSender.send(message);
}
}
```

**CONSUMING JMS-BASED SERVICES**
To consume the alert service, you can wire the JmsInvokerProxyFactoryBean like this:
```xml
<bean id="alertService"
class="org.springframework.jms.remoting.JmsInvokerProxyFactoryBean"
p:connectionFactory-ref="connectionFactory"
p:queueName="spittle.alert.queue"
propp:serviceInterface="com.habuma.spittr.alerts.AlertService" />
```


#####17.3 Messaging with AMQP
AMQP offers several advantages over JMS:

First, AMQP defines a wire-level protocol for messaging, whereas JMS defines an API specification. JMS’s API
specification ensures that all JMS implementations can be used through a common
API but doesn’t mandate that messages sent by one JMS implementation can be consumed
by a different JMS implementation. AMQP’s wire-level protocol, on the other
hand, specifies the format that messages will take when en route between the producer
and consumer. Consequently, AMQP is more interoperable than JMS—not only
across different AMQP implementations, but also across languages and platforms.

Another significant advantage of AMQP over JMS is that AMQP has a much more
flexible and transparent messaging model. With JMS, there are only two messaging
models to choose from: point-to-point and publish/subscribe. Both of those models
are certainly possible with AMQP, but AMQP enables you to route messages in a number
of ways, and it does this by decoupling the message producer from the queue(s) in
which the messages will be placed.



The four standard types of AMQP exchanges are as follows:
1) Direct—A message will be routed to a queue if its routing key is a direct match
for the routing key of the binding.
2 Topic—A message will be routed to a queue if its routing key is a wildcard match
for the routing key of the binding.
3) Headers—A message will be routed to a queue if the headers and values in its
table of arguments match those in the binding’s table of arguments. A special
header named x-match can specify whether all values must match or if any can
match.
4) Fanout—A message will be routed to all queues that are bound to the exchange,
regardless of the routing key or headers/values in the table of arguments.

Put simply, producers publish to an exchange with a routing key; consumers
retrieve from a queue.


#####17.3.2 Configuring Spring for AMQP messaging
```xml
<connection-factory id="connectionFactory"
host="${rabbitmq.host}"
port="${rabbitmq.port}"
username="${rabbitmq.username}"
password="${rabbitmq.password}" />
```


**DECLARING QUEUES, EXCHANGES, AND BINDINGS**
Spring AMQP’s rabbit namespace includes several elements for lazily creating queues,
exchanges, and the bindings between them.
```
<queue> Creates a queue.
<fanout-exchange> Creates a fanout exchange.
<header-exchange> Creates a headers exchange.
<topic-exchange> Creates a topic exchange.
<direct-exchange> Creates a direct exchange.
<bindings> <binding/> </bindings> The <bindings> element defines a set of one or
                                  more <binding> elements. The <binding> element
                                  creates a binding between an exchange and a queue.
```

```xml
<admin connection-factory="connectionFactory"/
> <queue id="spittleAlertQueue" name="spittle.alerts" />
```
Configure a fanout exchange and several queues like this:
```
<admin connection-factory="connectionFactory" /
> <queue name="spittle.alert.queue.1" > <queue name="spittle.alert.queue
.2" > <queue name="spittle.alert.queue.3" > <fanoutexchange
name="spittle.fanout"> <bindings> <binding queue="spittle.al
ert.queue.1" /> <binding queue="spittle.alert.queue.2" /
> <binding queue="spittle.alert.queue.3" /> </bindings> </fanoutexchange>
```

#####17.3.3 Sending messages with RabbitTemplate
```xml
<template id="rabbitTemplate" connection-factory="connectionFactory" />
```
```java
public class AlertServiceImpl implements AlertService {
private RabbitTemplate rabbit;
@Autowired
public AlertServiceImpl(RabbitTemplate rabbit) {
this.rabbit = rabbit;
}
public void sendSpittleAlert(Spittle spittle) {
rabbit.convertAndSend("spittle.alert.exchange",
"spittle.alerts",
spittle);
}
}
```

#####17.3.4 Receiving AMQP messages
**RECEIVING MESSAGES WITH RABBITTEMPLATE**
```xml
<template id="rabbitTemplate"
connection-factory="connectionFactory"
exchange="spittle.alert.exchange"
routing-key="spittle.alerts"
queue="spittle.alert.queue" />
```
```java
Message message = rabbit.receive();
```

**DEFINING MESSAGE-DRIVEN AMQP POJOS**

Notice that this is exactly the same SpittleAlertHandler that you used when consuming
Spittle messages using JMS
```java
public class SpittleAlertHandler {
public void handleSpittleAlert(Spittle spittle) {
// ... implementation goes here ...
}
}
```

```xml
<bean id="spittleListener" class="com.habuma.spittr.alert.SpittleAlertHandler" />
```
```xml
<listener-container connection-factory="connectionFactory">
<listener ref="spittleListener"
method="handleSpittleAlert"
queue-names="spittle.alert.queue" />
</listener-container>
```
Note: the only difference is destination attribute for JMS, queue-names for AMQP

#####17.4 Summary
Spring’s JMS template eliminates the boilerplate that’s commonly required by the traditional
JMS programming model.

Spring-enabled message-driven beans make it possible to declare bean methods that react to messages that
arrive in a queue or topic.


###Chapter 20: Managing Spring beans with JMX (Java Management Extensions)
Spring’s support for DI is a great way to configure bean properties in an application.
But once the application has been deployed and is running, DI alone can’t do
much to help you change that configuration.

Java Management Extensions (JMX) can dig into a running application and change its configuration on the fly.

JMX is a technology that enables you to instrument applications for **management**, **monitoring**, and **configuration**.

The key component of an application that’s instrumented for management with
JMX is the managed bean (MBean). An MBean is a JavaBean that exposes certain
methods that define the management interface.

The JMX specification defines four types of MBeans:
```
1) Standard MBeans:  MBeans whose management interface is determined by
                  reflection on a fixed Java interface that’s implemented by the bean class.

2) Dynamic MBeans: MBeans whose management interface is determined at runtime
                   by invoking methods of the DynamicMBean interface. Because the management
                   interface isn’t defined by a static interface, it can vary at runtime.

3) Open MBeans: A special kind of dynamic MBean whose attributes and operations
                are limited to primitive types, class wrappers for primitive types, and any
                type that can be decomposed into primitives or primitive wrappers.

4) Model MBeans: A special kind of dynamic MBean that bridges a management
                 interface to the managed resource. Model MBeans aren’t written as much as
                 they are declared. They’re typically produced by a factory that uses some metainformation
                 to assemble the management interface.
```

#####20.1: Exporting Spring beans as MBeans
MBean server (sometimes called an MBean agent) is a container
where MBeans live and through which the MBeans are accessed.

JMX-based management tool such as JConsole or VisualVM to peer inside a
running application to view the beans’ properties and invoke their methods

The following @Bean method declares an MBeanExporter in Spring to export the
spittleController bean as a model MBean:
```java
@Bean
public MBeanExporter mbeanExporter(SpittleController spittleController) {
MBeanExporter exporter = new MBeanExporter();
Map<String, Object> beans = new HashMap<String, Object>();
beans.put("spitter:name=SpittleController", spittleController);
exporter.setBeans(beans);
return exporter;
}
```


#####20.1.1 Exposing methods by name
To limit your MBean’s exposure,
you need to tell MethodNameBasedMBeanInfoAssembler to include only those methods
in the MBean’s interface. The following declaration of a MethodNameBasedMBean-
InfoAssembler bean singles out those methods:
```java
@Bean
public MethodNameBasedMBeanInfoAssembler assembler() {
MethodNameBasedMBeanInfoAssembler assembler =
new MethodNameBasedMBeanInfoAssembler();
assembler.setManagedMethods(new String[] {
"getSpittlesPerPage", "setSpittlesPerPage"
});
return assembler;
}
```

To put the assembler into action, you need to wire it into the MBeanExporter:
```java
@Bean
public MBeanExporter mbeanExporter(
SpittleController spittleController,
MBeanInfoAssembler assembler) {
MBeanExporter exporter = new MBeanExporter();
Map<String, Object> beans = new HashMap<String, Object>();
beans.put("spitter:name=SpittleController", spittleController);
exporter.setBeans(beans);
exporter.setAssembler(assembler);
return exporter;
}
```
Method name–based assemblers are straightforward and easy to use, but, in terms of Spring configuration, the method-name
approach doesn’t scale well when exporting multiple MBeans.

#####20.1.2 Using interfaces to define MBean operations and attributes
```java
public interface SpittleControllerManagedOperations {
int getSpittlesPerPage();
void setSpittlesPerPage(int spittlesPerPage);
}

@Bean
public InterfaceBasedMBeanInfoAssembler assembler() {
InterfaceBasedMBeanInfoAssembler assembler =
new InterfaceBasedMBeanInfoAssembler();
assembler.setManagedInterfaces(
new Class<?>[] { SpittleControllerManagedOperations.class }
);
return assembler;
}
```
The nice thing about using interfaces to select managed operations is that you can
collect dozens of methods into a few interfaces and keep the configuration of
InterfaceBasedMBeanInfoAssembler clean.

method names declared in an interface
or Spring context and method names in the implementation. This duplication exists
for no other reason than to satisfy the MBeanExporter.




#####20.1.3 Working with annotation-driven MBeans
MetadataMBeanInfoAssembler can be configured to use annotations
```xml
<context:mbean-export server="mbeanServer" />
```

```java
@Controller
@ManagedResource(objectName="spitter:name=SpittleController") //
public class SpittleController {
...
@ManagedAttribute //
public void setSpittlesPerPage(int spittlesPerPage) {
this.spittlesPerPage = spittlesPerPage;
}
@ManagedAttribute //
public int getSpittlesPerPage() {
return spittlesPerPage;
}
}
```


#####20.1.4 Handling MBean collisions
There are three ways to handle an MBean name collision via the registration Policy property:
```
1) FAIL_ON_EXISTING—Fail if an existing MBean has the same name (this is the default behavior).

2) IGNORE_EXISTING—Ignore the collision and don’t register the new MBean.

3) REPLACING_EXISTING—Replace the existing MBean with the new MBean.
```

#####20.2 Remoting MBeans
#####20.2.1 Exposing remote MBeans
```java
@Bean
public ConnectorServerFactoryBean connectorServerFactoryBean() {
        ConnectorServerFactoryBean csfb = new ConnectorServerFactoryBean();
        csfb.setServiceUrl( "service:jmx:rmi://localhost/jndi/rmi://localhost:1099/spitter");
        return csfb;
}
```

#####20.2.2 Accessing remote MBeans
```java
@Bean
public MBeanServerConnectionFactoryBean connectionFactoryBean() {
MBeanServerConnectionFactoryBean mbscfb =
new MBeanServerConnectionFactoryBean();
mbscfb.setServiceUrl(
"service:jmx:rmi://localhost/jndi/rmi://localhost:1099/spitter");
return mbscfb;
}
```



#####20.2.3 Proxying MBeans
```java
@Bean
public MBeanProxyFactoryBean remoteSpittleControllerMBean(
MBeanServerConnection mbeanServerClient) {
MBeanProxyFactoryBean proxy = new MBeanProxyFactoryBean();
proxy.setObjectName("");
proxy.setServer(mbeanServerClient);
proxy.setProxyInterface(SpittleControllerManagedOperations.class);
return proxy;
}
```


#####20.3 Handling notifications
Spring’s support for sending notifications comes in the form of the Notification-
PublisherAware interface. Any bean-turned-MBean that wishes to send notifications
should implement this interface.

```java
@Component
@ManagedResource("spitter:name=SpitterNotifier")
@ManagedNotification(
notificationTypes="SpittleNotifier.OneMillionSpittles",
name="TODO")
public class SpittleNotifierImpl
implements NotificationPublisherAware, SpittleNotifier {
private NotificationPublisher notificationPublisher;
public void setNotificationPublisher(
NotificationPublisher notificationPublisher) {
this.notificationPublisher = notificationPublisher;
}
public void millionthSpittlePosted() {
notificationPublisher.sendNotification(
new Notification(
"SpittleNotifier.OneMillionSpittles", this, 0));
}
}
```

#####20.3.1 Listening for notifications
The standard way to receive MBean notifications is to implement the javax
.management.NotificationListener interface.
```java
public class PagingNotificationListener
implements NotificationListener {
public void handleNotification(
Notification notification, Object handback) {
// ...
}
}
```
The only thing left to do is register PagingNotificationListener with the MBean Exporter:
```java
@Bean
public MBeanExporter mbeanExporter() {
MBeanExporter exporter = new MBeanExporter();
Map<?, NotificationListener> mappings =
new HashMap<?, NotificationListener>();
mappings.put("Spitter:name=PagingNotificationListener",
new PagingNotificationListener());
exporter.setNotificationListenerMappings(mappings);
return exporter;
}
```

#####20.4 Summary 539
With JMX, you can open a window into the inner workings of your application.

In this chapter, you saw how to configure Spring to automatically export Spring beans as JMX
MBeans so that their details can be viewed and manipulated through JMX-ready management
tools.

You also learned how to create and use remote MBeans for times when
those MBeans and tools are distant from each other. Finally, you saw how to use
Spring to publish and listen for JMX notifications.






