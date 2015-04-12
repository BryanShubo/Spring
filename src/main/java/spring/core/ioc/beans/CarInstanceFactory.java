package spring.core.ioc.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Create factory first
 */
public class CarInstanceFactory {

    private Map<String, Car> cars = null;

    public CarInstanceFactory() {
        cars = new HashMap<String, Car>();
        cars.put("audi", new Car("audi", 300000));
        cars.put("ford", new Car("ford", 250000));
    }

    public Car getCar(String name) {
        return cars.get(name);
    }
}
