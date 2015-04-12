package spring.core.ioc.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubo on 4/11/2015.
 */
public class CarStaticFactory {
    private static Map<String, Car> cars = new HashMap<String, Car>();

    static {
        cars.put("audi", new Car("audi", 300000));
        cars.put("ford", new Car("ford", 250000));
    }

    public static Car getCar(String name) {
        return cars.get(name);
    }
}
