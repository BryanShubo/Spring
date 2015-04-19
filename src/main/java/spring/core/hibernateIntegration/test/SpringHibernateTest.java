package spring.core.hibernateIntegration.test;

import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.core.hibernateIntegration.service.BookShopService;
import spring.core.hibernateIntegration.service.Cashier;

public class SpringHibernateTest {

	private ApplicationContext ctx = null;
	private BookShopService bookShopService = null;
	private Cashier cashier = null;
	
	{
		ctx = new ClassPathXmlApplicationContext("hibernate-integration.xml");
		bookShopService = ctx.getBean(BookShopService.class);
		cashier = ctx.getBean(Cashier.class);
	}
	
	@Test
	public void testCashier(){
		cashier.checkout("aa", Arrays.asList("1001","1002"));
	}
	
	@Test
	public void testBookShopService(){
		bookShopService.purchase("aa", "1001");
	}
	
	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}

}
