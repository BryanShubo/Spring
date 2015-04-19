package spring.core.transaction.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("bookShopService")
public class BookShopServiceImpl implements BookShopService {

	@Autowired
	private BookShopDao bookShopDao;
	/*
	* 1. propagation: default is REQUIRED
	* 2. isolation: common use READ_COMMITTED
	* 3. Spring rollbacks all exception by default. It can be changed via configuration
	* 4. readOnly: database engine could optimize transaction.
	* 5. timeout: transaction time before rollback
	* */

//	@Transactional(propagation=Propagation.REQUIRES_NEW,
//			isolation=Isolation.READ_COMMITTED,
//			noRollbackFor={UserAccountException.class})
	@Transactional(propagation=Propagation.REQUIRES_NEW,
			isolation=Isolation.READ_COMMITTED,
			readOnly=false,
			timeout=3)
	@Override
	public void purchase(String username, String isbn) {
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		
		//1. Get book price
		int price = bookShopDao.findBookPriceByIsbn(isbn);
		
		//2. Update book stock
		bookShopDao.updateBookStock(isbn);
		
		//3. Update user balance
		bookShopDao.updateUserAccount(username, price);
	}

}
