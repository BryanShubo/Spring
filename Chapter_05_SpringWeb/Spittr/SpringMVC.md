
5.1 Spring MVC work flow
Spring moves requests between a dispatcher servlet, handler mappings, controllers, and view
resolvers.

1) A request with information goes to DispatcherServlet.
2) DispatcherServlet checks with handler mappings to figure out the selected Spring MVC controller.
3) The request drops off its payload and waits the controller processes that information.
4) Controller sends the request along with the model and view name back to the DispatcherServlet.
5) DispatcherServlet consults a view resolver to map the logical view name to a specific view implementation.
6) The view will use the model data to render output that the response object is back to client.


5.2 Spring MVC provides several ways that a client can pass data into a controller’s handler
    method. These include
     Query parameters
     Form parameters
     Path variables
    
    
    
    
    6.1
    
    At most, the controller
    methods and view implementations should agree on the contents of the model;
    apart from that, they should keep an arms-length distance from each other.
    
    ···java
    
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