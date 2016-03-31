package panov.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import panov.bean.Const;
import panov.bean.Filter;
import panov.entity.Param;
import panov.entity.ParamVal;
import panov.entity.ParamValPoi;
import panov.entity.Poi;

import java.util.*;
import java.util.stream.Collectors;

import static panov.bean.Const.*;

/**
 * @author Andrey Panov, 27.03.2016
 */
@Service
public class StaticService {

    private static final Logger log = LoggerFactory.getLogger(StaticService.class);

    @Autowired
    CassandraTemplate templ;

    public static Poi convert(Map<String, Object> map) {
        checkCoodinates(map);
        Objects.requireNonNull(map.get(POI_ID), "poi_id can't be null");

        try {
            Poi poi = new Poi().setId(Long.valueOf(map.get(POI_ID).toString()));
            Map<String, String> poiMap = map.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(POI_ID))
                    .collect(Collectors.toMap(Map.Entry::getKey, p -> p.getValue().toString()));

            poi.setParameters(poiMap);

            return poi;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id should be type LONG");
        }
    }

    private static void checkCoodinates(Map map) {
        if (map.get(LATITUDE) == null) throw new IllegalArgumentException("latitude parameter can't be NULL");
        if (map.get(Const.LONGITUDE) == null) throw new IllegalArgumentException("longitude parameter can't be NULL");
    }

    @Async
    public void save(Poi poi) {
        Objects.requireNonNull(poi.getId());
        Objects.requireNonNull(poi.getParameters().get(LONGITUDE));
        Objects.requireNonNull(poi.getParameters().get(LATITUDE));

        templ.insert(poi);
        log.info("-->> save: {}", poi);

        updateIndexes(poi);
    }

    private void updateIndexes(Poi poi) {
        String partitionKey = getPartitionKey(poi.getParameters());

        for (Map.Entry<String, String> entry : poi.getParameters().entrySet()) {
            if (LONGITUDE.equals(entry.getKey())) continue;
            if (LATITUDE.equals(entry.getKey())) continue;

            templ.insert(new ParamValPoi()
                    .setKey(partitionKey)
                    .setParam(entry.getKey())
                    .setValue(entry.getValue())
                    .setPoi_id(poi.getId()));

            String cql = "UPDATE atlas.param_val SET cnt = cnt + 1 WHERE key = '" + partitionKey + "' " +
                    "and param = '" + entry.getKey() + "' and value = '" + entry.getValue() + "'";
            templ.getSession().execute(cql);

            templ.insert(new Param()
                    .setKey(partitionKey)
                    .setParam(entry.getKey()));
        }
    }

    public List<Long> findAllIds(Map<String, String> searchParameters) {
        checkCoodinates(searchParameters);

        Filter filter = asFilter(searchParameters);
        Map<String, String> filters = sortSearchFilter(filter.getPartitionKey(), filter.getFilters());
        if (filters.isEmpty()) return Collections.emptyList();

        List<Long> inList = new ArrayList<>();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            inList = makeSelection(filter.getPartitionKey(), entry.getKey(), entry.getValue(), inList);
            if (inList.isEmpty()) {
                break;
            }
        }

        return inList;
    }

    private List<Long> makeSelection(String key, String param, String value, List<Long> inList) {
        String paramToVal = "'" + param + "','" + value + "'";

        String cql = "SELECT poi_id from param_val_poi WHERE key = '" + key + "' and (param,value) in " +
                "((" + paramToVal + "))";

        List<Long> result = null;

        if (!inList.isEmpty()) {
            List<String> strs = inList.stream().map(Object::toString).collect(Collectors.toList());
            cql = cql + " and poi_id in (" + String.join(", ", strs) + ")";
        }

        result = templ.getSession().execute(cql).all().stream()
                .map(row -> row.getLong(0))
                .collect(Collectors.toList());

        log.info("size {} for param {}: {}", result.size(), param, cql);

        return result;
    }

    /**
     * Change the order, of query parameters.
     * param_val table count how many times parametr with certain values is used.
     * So less count give us less result for next query.
     *
     * @param key        partition key
     * @param seachParam search parameters
     * @return empty Map if prediction tell us, that there will be no result with these parameters
     */
    LinkedHashMap<String, String> sortSearchFilter(String key, Map<String, String> seachParam) {

        List<String> paramsToVals = new ArrayList<>();
        for (Map.Entry<String, String> entry : seachParam.entrySet()) {
            String paramToVal = "('" + entry.getKey() + "','" + entry.getValue() + "')";
            paramsToVals.add(paramToVal);
        }

        String cql = "SELECT * from param_val WHERE key = '" + key + "' and (param,value) in " +
                "(" + String.join(", ", paramsToVals) + ")";
        log.info("get counters: " + cql);
        List<ParamVal> select = templ.select(cql, ParamVal.class);
        select = select.stream()
                .sorted((o1, o2) -> o1.getCnt().compareTo(o2.getCnt()))
                .collect(Collectors.toList());

        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        select.forEach(paramVal -> res.put(paramVal.getParam(), paramVal.getValue()));

        if (res.size() < seachParam.size()) {
            log.info("filter is too restrictive {}", seachParam);
            return new LinkedHashMap<>();
        }

        return res;
    }


    private Filter asFilter(Map<String, String> map) {
        Filter filter = new Filter();
        filter.setPartitionKey(getPartitionKey(map));

        Map<String, String> filterMap = map.entrySet().stream()
                .filter(entry -> {
                    switch (entry.getKey()) {
                        case LATITUDE:
                            return false;
                        case Const.LONGITUDE:
                            return false;
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        filter.setFilters(filterMap);
        return filter;
    }

    public String getPartitionKey(Map<String, String> map) {
        String latitude = map.get(LATITUDE);
        String longitude = map.get(Const.LONGITUDE);

        return latitude + "|" + longitude;
    }

    public List<Poi> findAll(Map<String, String> searchMap) {
        List<Long> ids = findAllIds(searchMap);
        return findById(ids);
    }

    public List<Poi> findById(List<Long> ids) {
        String idJoin = String.join(", ", ids.stream().map(Object::toString).collect(Collectors.toList()));
        String cql = "select * from poi where id in (" + idJoin + ")";
        return templ.select(cql, Poi.class);
    }

}
