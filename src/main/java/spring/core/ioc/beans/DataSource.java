package spring.core.ioc.beans;

import java.util.Properties;

/**
 * Created by Shubo on 4/10/2015.
 */
public class DataSource {

    Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "properties=" + properties +
                '}';
    }
}
