package panov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;
import panov.entity.Poi;

import java.util.List;

/**
 * @author Andrey Panov, 25.03.2016
 */
@Service
public class PoiItemService {

    @Autowired
    private CassandraTemplate templ;

    public void save(Poi item) {
        templ.insert(item);
    }

    public List<Poi> selectAll() {
        return templ.selectAll(Poi.class);
    }
}
