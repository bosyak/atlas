package panov.entity;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author Andrey Panov, 27.03.2016
 */
@Table("param_val_poi")
public class ParamValPoi {
    @PrimaryKeyColumn(name = "key", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String key;

    @PrimaryKeyColumn(name = "param", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String param;

    @PrimaryKeyColumn(name = "value", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String value;

    @PrimaryKeyColumn(name = "poi_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private Long poi_id;

    public String getKey() {
        return key;
    }

    public ParamValPoi setKey(String key) {
        this.key = key;
        return this;
    }

    public String getParam() {
        return param;
    }

    public ParamValPoi setParam(String param) {
        this.param = param;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ParamValPoi setValue(String value) {
        this.value = value;
        return this;
    }

    public Long getPoi_id() {
        return poi_id;
    }

    public ParamValPoi setPoi_id(Long poi_id) {
        this.poi_id = poi_id;
        return this;
    }

}
