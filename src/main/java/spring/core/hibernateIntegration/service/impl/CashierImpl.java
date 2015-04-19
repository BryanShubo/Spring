package spring.core.hibernateIntegration.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.core.hibernateIntegration.service.BookShopService;
import spring.core.hibernateIntegration.service.Cashier;


@Service
public class CashierImpl implements Cashier {
	
	@Autowired
	private BookShopService bookShopService;
	
	@Override
	public void checkout(String username, List<String> isbns) {
		for(String isbn:isbns){
			bookShopService.purchase(username, isbn);
		}
	}

}
