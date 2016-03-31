package panov.entity;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Panov, 25.03.2016
 */

@Table("poi")
public class Poi {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long id;

    @Column
    private Map<String, String> parameters = new HashMap<>();

    public Long getId() {
        return id;
    }

    public Poi setId(Long id) {
        this.id = id;
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Poi setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    @Override
    public String toString() {
        return "Poi{" +
                "id=" + id +
                ", parameters=" + parameters +
                '}';
    }
}
