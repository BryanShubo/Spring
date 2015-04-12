package spring.core.ioc.annotation.repository;

import org.springframework.stereotype.Repository;

/**
 * Created by Shubo on 4/11/2015.
 */
@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository{
    @Override
    public void save() {
        System.out.println("UserRepository save method---");
    }
}
