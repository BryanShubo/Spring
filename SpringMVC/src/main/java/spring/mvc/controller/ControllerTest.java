package spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Shubo on 4/19/2015.
 */

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
}
