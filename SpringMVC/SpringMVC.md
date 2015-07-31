

1. Web.xml
```xml
 <servlet>
		<servlet-name>springDispatcherServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>springDispatcherServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>

```

2. spring-mvc.xml
```xml
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
```


3. Controller
```java
@RequestMapping("/springmvc")
@Controller
public class ControllerTest {
private static final String SUCCESS = "success";

    @RequestMapping("/testRequestMapping")
    public String testRequestMapping() {
        System.out.println("testRequestMapping");
        return SUCCESS;
    }

    @RequestMapping(value = "/testMethod", method = RequestMethod.POST)
    public String testMethod(){
        System.out.println("testMethod");
        return SUCCESS;
    }

    @RequestMapping(value = "/testParamsAndHeaders", method = RequestMethod.GET, params = {"username", "age != 21"})
    public String testParamsAndHeaders(){
     return SUCCESS;
    }

    @RequestMapping(value = "/testAntPath/*/abc")
    public String testAntPath() {
        return SUCCESS;
    }

    @RequestMapping("/testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") Integer id) {
        System.out.println("testPathVariable: " + id);
        return SUCCESS;
    }



    //RESTful methods
    @RequestMapping(value="/testRest/{id}", method = RequestMethod.GET)
        public String testRest(@PathVariable Integer id) {
            System.out.println("testRest GET: " + id);
            return SUCCESS;
        }

    @RequestMapping("/testRequestParam")
    public String testRequestParam(@RequestParam(value = "username") String un,
                                   @RequestParam(value = "age", required = false, defaultValue="0") Integer age) {
        System.out.println("testRequestParam: " +  un + " : " + age);
        return SUCCESS;
    }



    /**
         * @RequestHeader
         */
    @RequestMapping("/testRequestHeader")
    public String testRequestHeader(@RequestHeader(value = "Accept-Language") String al) {
        System.out.println("accept language: " + al);
        return SUCCESS;
    }

    /**
     * @CookieValue: mapping to one value
     * @param sessionId
     * @return
     */
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID") String sessionId) {

    System.out.println("Sessionid + " + sessionId);
    return SUCCESS;
    }


    /**
     * Spring MVC puts the data from modelAndView into request scope
     *
     */
    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView() {
        String viewName = SUCCESS;
        ModelAndView modelAndView = new ModelAndView(viewName);

        modelAndView.addObject("time", new Date());
        return modelAndView;
    }

    /*
         * @ModelAttribute will be invoked by Spring MVC before any method executes.
     *
     * 1. 有 @ModelAttribute 标记的方法, 会在每个目标方法执行之前被 SpringMVC 调用!
     * 2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
     * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
     * 2). SpringMVC 会一 value 为 key, POJO 类型的对象为 value, 存入到 request 中.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        System.out.println("Execute modelAttribute method");
        if (id != null) {
            User user = new User(1, "Tom", "123456", "Tom@gmail.com", 12);
            System.out.println("get user " + user);
            map.put("user", user);
        }
    }
}


The model presents a placeholder to hold the information you want to display on the view. It could be a string, which is in your above example, or it could be an object containing bunch of properties.

Example 1

If you have...

return new ModelAndView("welcomePage","WelcomeMessage","Welcome!");
... then in your jsp, to display the message, you will do:-

Hello Stranger! ${WelcomeMessage} // displays Hello Stranger! Welcome!
Example 2

If you have...

MyBean bean = new MyBean();
bean.setName("Mike");
bean.setMessage("Meow!");

return new ModelAndView("welcomePage","model",bean);
... then in your jsp, you can do:-

Hello ${model.name}! {model.message} // displays Hello Mike! Meow!
```
