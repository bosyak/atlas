package panov.bean;

import java.util.List;
import java.util.Map;

/**
 * @author Andrey Panov, 28.03.2016
 */
public class Filter {
    private String partitionKey;
    private Map<String, String> filters;
    private List<Long> poiIds;

    public String getPartitionKey() {
        return partitionKey;
    }

    public Filter setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
        return this;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public Filter setFilters(Map<String, String> filters) {
        this.filters = filters;
        return this;
    }

    public List<Long> getPoiIds() {
        return poiIds;
    }

    public Filter setPoiIds(List<Long> poiIds) {
        this.poiIds = poiIds;
        return this;
    }
}
