package spring.core.ioc.annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import spring.core.ioc.annotation.repository.UserRepository;

/**
 * Created by Shubo on 4/11/2015.
 */
@Service
public class UserService {
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    
    public void add() {
        System.out.println("UserService add method-----");
        userRepository.save();
    }
}
