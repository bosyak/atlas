package panov.entity;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author Andrey Panov, 27.03.2016
 */
@Table("param_val")
public class ParamVal {
    @PrimaryKeyColumn(name = "key", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String key;

    @PrimaryKeyColumn(name = "param", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String param;

    @PrimaryKeyColumn(name = "value", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String value;

    private Long cnt;

    public String getKey() {
        return key;
    }

    public ParamVal setKey(String key) {
        this.key = key;
        return this;
    }

    public String getParam() {
        return param;
    }

    public ParamVal setParam(String param) {
        this.param = param;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ParamVal setValue(String value) {
        this.value = value;
        return this;
    }

    public Long getCnt() {
        return cnt;
    }

    public ParamVal setCnt(Long cnt) {
        this.cnt = cnt;
        return this;
    }
}
