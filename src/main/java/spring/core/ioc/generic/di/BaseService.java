package spring.core.ioc.generic.di;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Shubo on 4/11/2015.
 */
public class BaseService<T> {

    @Autowired
    protected BaseRepository<T> repository;

    public void add() {
        System.out.println("add ----");
        System.out.println(repository);
    }
}
