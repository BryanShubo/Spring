package spring.restful.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.restful.crud.dao.EmployeeDao;
import spring.restful.crud.entities.Employee;

/**
 * Created by Shubo on 4/25/2015.
 */
@Controller
public class SpringMVCTest {

    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping("/testConversionServiceConverter")
    public String testConverter(@RequestParam("employee")Employee employee) {

        System.out.println("save: " + employee.toString());
        employeeDao.save(employee);
        return "redirect:/emps";
    }
}
