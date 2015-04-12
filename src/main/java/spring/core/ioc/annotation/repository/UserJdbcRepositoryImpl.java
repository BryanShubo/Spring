package spring.core.ioc.annotation.repository;

import org.springframework.stereotype.Repository;

/**
 * Created by Shubo on 4/11/2015.
 */
@Repository
public class UserJdbcRepositoryImpl implements UserRepository {
    @Override
    public void save() {
        System.out.println("UserJdbcRepositoryImpl save method ----");
    }
}
