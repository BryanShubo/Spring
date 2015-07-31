package spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import spring.mvc.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by Shubo on 4/19/2015.
 */

@SessionAttributes(value = {"user"}, types={String.class})
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


    /*
    * Rest URL
    *
    * CRUD
    * create: /order  POST
    * update: /order/1 PUT
    * retrieve: /order/1 GET
    * delete: /order/1 DELETE
      *
      * old style:
      * update?id=1
      * get?id=1
      * delete?id=1
      *
      *
      * How to send DELETE and PUT request:
      * 1. Config HiddenHttpMethodFilter
      * 2. need POST request
      * 3. name="_method"  value= DELETE or PUT
      *
      * How to get id in SPRING MVC
      * Using @PathVariable annotation
    * */
    @RequestMapping(value="/testRest/{id}", method = RequestMethod.GET)
    public String testRest(@PathVariable Integer id) {
        System.out.println("testRest GET: " + id);
        return SUCCESS;
    }

    @RequestMapping(value="/testRest", method = RequestMethod.POST)
    public String testRest() {
        System.out.println("testRest POST");
        return SUCCESS;
    }

    @RequestMapping(value="/testRest/{id}", method = RequestMethod.DELETE)
    public String testRestDelete(@PathVariable Integer id) {
        System.out.println("testRest DELETE: " + id);
        return SUCCESS;
    }


    @RequestMapping(value="/testRest/{id}", method = RequestMethod.PUT)
    public String testRestPut(@PathVariable Integer id) {
        System.out.println("testRest PUT: " + id);
        return SUCCESS;
    }

    /**
     * @RequestParam  mapping parameters
     * value: param name
     * required: default is true
     * defaultValue: default value of the param
     */
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

    @RequestMapping("/testPojo")
    public String testPojo(User user) {
        System.out.println("user : " + user);
        return SUCCESS;
    }

    /**
     * User servlet API:
     * HttpServletRequest
     • HttpServletResponse
     • HttpSession
     • java.security.Principal
     • Locale
     • InputStream
     • OutputStream
     • Reader: response.getReader()
     • Writer: response.getWriter()
     */
    @RequestMapping("/testServletAPI")
    public String testServletAPI(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("test servlet API: " + request + " : " + response);
        return SUCCESS;
    }


    @RequestMapping("/testServletAPIwriter")
    public void testServletAPI(HttpServletRequest request, HttpServletResponse response, Writer out) throws IOException {
        System.out.println("test servlet API: " + request + " : " + response);
        out.write("This is from test servlet api writer");
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


    /**
     * map, model, and modelMap
     */
    @RequestMapping("/testMap")
    public String testMap(Map<String, Object> map) {

        map.put("names", Arrays.asList("Tom", "Jerry", "Zhang"));
        return SUCCESS;
    }

    /**
     * 1. Put @SessionAttribute on class.
     * 2. User will be placed into both request and session scopes. (value config)
     * 3. type config is also working.
     * @return
     */
    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Map<String, Object> map){
        User user = new User(1,"Tom", "123", "tom@gmail.com", 15);
        map.put("user", user);
        map.put("school", "Atguigu");
        return SUCCESS;
    }

    /*
     * 1. Execute @ModelAttribute method: get object from db and put the object into map as key
     * 2. Spring MVC retrieves User object from Map and assigns request params to User's corresponding
     * fields
     * 3. Spring MVC passes the User to target method.
     *
     * Note: name must match to the args of target method
     *
     * SpringMVC find target method's POJO args
     *
     *
     *
     * source code:
     * 1. Invoke @modelAttribute method and put map data into implicitModel
     * 2. WebDataBinder object target field
     * 1) create webDataBinder object (objectName, target)
     * objectName:
     * a) if attrName.equals(""), objectName is the class name with lower first letter
     * b) If target method pojo filed using @ModelAndView, attrName is @ModelAndView field value
     * target:
     * a) search attrName in implicitModel.
     * b) if attrName does not exist, check handler and see if @SessionAttribute is annotated.
     * If it does, try to get field from @SessionAttributes.
     * if Session is used but does not have this field, throws exception
     * c) If no @SessionAttribute, create POJO object by reflect way.
     *
     * 2) Spring MVC assigns form data to WebDataBinder's target field
     * 3) Spring MVC passes WebDataBinder (attrName, target) to implicitModel (request scope object)
     * 4) WebDataBinder's target field will be the "target method"'s args.
     */



    /**
     * 运行流程:
     * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象, 把对象放入到了 Map 中. 键为: user
     * 2. SpringMVC 从 Map 中取出 User 对象, 并把表单的请求参数赋给该 User 对象的对应属性.
     * 3. SpringMVC 把上述对象传入目标方法的参数.
     *
     * 注意: 在 @ModelAttribute 修饰的方法中, 放入到 Map 时的键需要和目标方法入参类型的第一个字母小写的字符串一致!
     *
     * SpringMVC 确定目标方法 POJO 类型入参的过程
     * 1. 确定一个 key:
     * 1). 若目标方法的 POJO 类型的参数木有使用 @ModelAttribute 作为修饰, 则 key 为 POJO 类名第一个字母的小写
     * 2). 若使用了  @ModelAttribute 来修饰, 则 key 为 @ModelAttribute 注解的 value 属性值.
     * 2. 在 implicitModel 中查找 key 对应的对象, 若存在, 则作为入参传入
     * 1). 若在 @ModelAttribute 标记的方法中在 Map 中保存过, 且 key 和 1 确定的 key 一致, 则会获取到.
     * 3. 若 implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰,
     * 若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所
     * 对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常.
     * 4. 若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则
     * 会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数
     * 5. SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中.
     *
     * 源代码分析的流程
     * 1. 调用 @ModelAttribute 注解修饰的方法. 实际上把 @ModelAttribute 方法中 Map 中的数据放在了 implicitModel 中.
     * 2. 解析请求处理器的目标参数, 实际上该目标参数来自于 WebDataBinder 对象的 target 属性
     * 1). 创建 WebDataBinder 对象:
     * ①. 确定 objectName 属性: 若传入的 attrName 属性值为 "", 则 objectName 为类名第一个字母小写.
     * *注意: attrName. 若目标方法的 POJO 属性使用了 @ModelAttribute 来修饰, 则 attrName 值即为 @ModelAttribute
     * 的 value 属性值
     *
     * ②. 确定 target 属性:
     * 	> 在 implicitModel 中查找 attrName 对应的属性值. 若存在, ok
     * 	> *若不存在: 则验证当前 Handler 是否使用了 @SessionAttributes 进行修饰, 若使用了, 则尝试从 Session 中
     * 获取 attrName 所对应的属性值. 若 session 中没有对应的属性值, 则抛出了异常.
     * 	> 若 Handler 没有使用 @SessionAttributes 进行修饰, 或 @SessionAttributes 中没有使用 value 值指定的 key
     * 和 attrName 相匹配, 则通过反射创建了 POJO 对象
     *
     * 2). SpringMVC 把表单的请求参数赋给了 WebDataBinder 的 target 对应的属性.
     * 3). *SpringMVC 会把 WebDataBinder 的 attrName 和 target 给到 implicitModel.
     * 近而传到 request 域对象中.
     * 4). 把 WebDataBinder 的 target 作为参数传递给目标方法的入参.
*/


    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(@ModelAttribute("user") User user){ // @ModelAttribute("") can use a different key
        System.out.println("修改: " + user);
        return SUCCESS;
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

    @RequestMapping("/testViewAndViewResolver")
    public String testViewAndViewResolver() {

        return SUCCESS;
    }

    @RequestMapping("/testView")
    public String testView() {
        return "helloView";
    }


    @RequestMapping("/testRedirect")
    public String testRedirect() {
        return "redirect:/index";
    }



    /*
    * Testing redirect
    * */
    @RequestMapping("/to-be-redirect")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://www.yahoo.com");
        return redirectView;
    }

    @RequestMapping("/to-be-redirect")
    public ModelAndView localRedirect2(){
        return new ModelAndView("redirect:" + "www.yahoo.com");
    }

    @RequestMapping("/to-be-redirect")
    public void localRedirect3(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "www.yahoo.com");
    }
 }
