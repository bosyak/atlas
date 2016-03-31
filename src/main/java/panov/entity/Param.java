package panov.entity;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author Andrey Panov, 27.03.2016
 */
@Table("param")
public class Param {
    @PrimaryKeyColumn(name = "key", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String key;

    @PrimaryKeyColumn(name = "param", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String param;

    public String getKey() {
        return key;
    }

    public Param setKey(String key) {
        this.key = key;
        return this;
    }

    public String getParam() {
        return param;
    }

    public Param setParam(String param) {
        this.param = param;
        return this;
    }
}
