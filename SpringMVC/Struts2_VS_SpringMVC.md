## Struts 2 VS SpringMVC

Well, Apache Struts 2 is an elegant and extensible framework that is used for creating enterprise-level Java web applications. It is designed to streamline the development cycle, starting from building to deployment and maintenance of the application. In Struts, the object that is taking care of a request and routes it for further processing is known as “Action”.


On the other hand, Spring MVC is a part of a huge Spring framework stack containing other Spring modules. This means that it doesn’t allow developers to run it without Spring, but the developers can run the Spring Core without Spring MVC.


The Spring MVC (Model View Controller) is designed around a DispatcherServlet, which dispatches the requests to handler with configurable handler mappings, view resolution and theme resolution.
While the objects responsible for handling requests and routing for processing in Struts called an Action, the same object is referred as Controller in Spring Web MVC framework. This is one of the very first differences between Spring MVC and Struts2. Struts 2 Actions are initiated every time when a request is made, whereas in Spring MVC the Controllers are created only once, stored in memory and shared among all the requests.
So, Spring Web MVC framework is far efficient to handle the requests than Struts 2.


If we talk about the features, Struts 2 and Spring MVC framework caters different level of business requirements.
Let’s take a look at features offered by both of these frameworks.

###Struts 2 features

Configurable MVC components, which are stored in struts.xml file. If you want to change anything, you can easily do it in the xml file.
POJO based actions. Struts 2 action class is Plain Old Java Object, which prevents developers to implement any interface or inherit any class.
Support for Ajax, which is used to make asynchronous request. It only sends needed field data rather than providing unnecessary information, which at the end improves the performance.
Support for integration with Hibernate, Spring, Tiles and so on.
Whether you want to use JSP, freemarker, velocity or anything else, you can use different kinds of result types in Struts 2.
You can also leverage from various tags like UI tags, Data tags, control tags and more.
Brings ample support for theme and template. Struts 2 supports three different kinds of themes including xhtml, simple and css_xhtml.

On the other hand, Spring MVC framework brings totally different set of features.






###Spring MVC features

Neat and clear separation of roles. Whether it is controller, command object, form object or anything else, it can be easily fulfilled with the help of a specialized object.
Leverage from the adaptability, non-intrusiveness and flexibility with the help of controller method signature.
Now use existing business objects as command or form object rather than duplicating them to extend the specific framework base class.
Customizable binding and validation will enable manual parsing and conversion to business objects rather than using conventional string.
Flexible mode transfer enables easy integration with the latest technology.
Customizable locale and theme resolution, support for JSPs with or without Spring tag library for JSTL and so on.
Leverage from the simple, but powerful JSP tag library known as Spring tag library. It provides support for various features like data binding and themes.

Of course, Struts is one of the most powerful Java application frameworks that can be used in a variety of Java applications. It brings a gamut of services that includes enterprise level services to the POJO. On the other hand, Spring utilizes the dependency injection to achieve the simplification and enhance the testability.
 Have you use this : Bluetooth device services in Java
Both of these frameworks have their own set of pros and cons associated with it.



###Struts framework brings a whole host of benefits including:

Simplified design
Ease of using plug-in
Simplified ActionForm & annotations
Far better tag features
OGNL integration
AJAX Support

Multiple view options and more
However, the only drawback with Struts 2 framework is that it has compatibility issues and poor documentation.
On the other hand, Spring MVC provides benefits like:

Clear separation between controllers, JavaBeans models and views that is not possible in Struts.
Spring MVC is more flexible as compared to the Struts.
Spring can be used with different platforms like Velocity, XLST or various other view technologies.
There is nothing like ActionForm in Spring, but binds directly to the domain objects.
Code is also more testable as compared to the Struts.
It is a complete J2EE framework comprising of seven independent layers, which simplifies integration with other frameworks.
It doesn’t provide a framework for implementing the business domain and logic, which helps developers create a controller and a view for the application.

However, like any other technologies or platforms, Spring MVC too suffers from several criticisms related to the complexity of the Spring framework.
Final Verdict
Either framework is a great choice. However, if you’re looking for the stable framework, Struts 2 is the right choice for you. On the other hand, if you’re looking for something robust, SpringMVC is perfect. Ensure that you review your exact requirements before choosing the framework!




