package spring.core.ioc.beans;

/**
 * Created by Shubo on 4/10/2015.
 */
public class Person {

    private Car car;
    private Address address;
    private String info;

    public Person(){

    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                ", address=" + address +
                ", info='" + info + '\'' +
                '}';
    }
}
