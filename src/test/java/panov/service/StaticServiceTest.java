package panov.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import panov.AtlasApplication;
import panov.bean.Const;
import panov.entity.Poi;
import panov.service.PoiItemService;
import panov.service.StaticService;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Andrey Panov, 25.03.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AtlasApplication.class)
public class StaticServiceTest {
    private static final Logger log = LoggerFactory.getLogger(AtlasApplication.class);

    @Autowired
    PoiItemService poiService;

    @Autowired
    CassandraTemplate temp;

    @Autowired
    StaticService staticService;

    @Before
    public void setUp() throws Exception {
        temp.execute("truncate param_val_poi");
        temp.execute("truncate param_val");
        temp.execute("truncate param");

        long currentTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Poi item = new Poi().setId(currentTimeMillis + i);
            item.getParameters().put("latitude", "37.42242");
            item.getParameters().put("longitude", "-122.08585");
            item.getParameters().put("building", "Elektroniikkatie 8");
            item.getParameters().put("floor", "PARKING");
            item.getParameters().put("type", "parkin space");
            item.getParameters().put("descr", "parkin space #" + new Random().nextInt(1000));
            item.getParameters().put("status", i % 3 == 0 ? "free" : "busy");

            staticService.save(item);
        }

        currentTimeMillis = System.currentTimeMillis() + 1000;
        for (int i = 0; i < 100; i++) {
            Poi item = new Poi().setId(currentTimeMillis + i);
            item.getParameters().put("latitude", "37.42242");
            item.getParameters().put("longitude", "-122.08585");
            item.getParameters().put("building", "Elektroniikkatie 8");
            item.getParameters().put("floor", "1");
            item.getParameters().put("type", "chair");
            item.getParameters().put("descr", "chair #" + new Random().nextInt(1000));
            item.getParameters().put("color", i % 2 == 0 ? "white" : "green");
            if (i % 3 == 0) item.getParameters().put("color", "red");

            staticService.save(item);
        }

        currentTimeMillis = System.currentTimeMillis() + 1000;
        for (int i = 0; i < 100; i++) {
            Poi item = new Poi().setId(currentTimeMillis + i);
            item.getParameters().put("latitude", "37.42242");
            item.getParameters().put("longitude", "-122.08585");
            item.getParameters().put("building", "Elektroniikkatie 8");
            item.getParameters().put("floor", "1");
            item.getParameters().put("type", "printer");
            item.getParameters().put("descr", "printer #" + new Random().nextInt(1000));
            item.getParameters().put("model", "xerox mp-" + i);
            item.getParameters().put("color", i % 2 == 0 ? "grey" : "black");
            if (i % 3 == 0) item.getParameters().put("color", "metallic");

            staticService.save(item);
        }


    }

    @Test
    public void test_selection() throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("type", "printer");
        params.put("color", "grey");
        params.put("model", "xerox mp-2");
        params.put(Const.LATITUDE, "37.42242");
        params.put(Const.LONGITUDE, "-122.08585");

        List<Long> ids = staticService.findAllIds(params);
        assertEquals(1, ids.size());
    }

    @Test
    public void test_flor() throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("building", "Elektroniikkatie 8");
        params.put("floor", "PARKING");
        params.put(Const.LATITUDE, "37.42242");
        params.put(Const.LONGITUDE, "-122.08585");

        List<Long> ids = staticService.findAllIds(params);
        assertEquals(100, ids.size());
    }

    @Test
    public void test_predict_empty() throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("building", "no address");
        params.put(Const.LATITUDE, "37.42242");
        params.put(Const.LONGITUDE, "-122.08585");

        LinkedHashMap<String, String> map = staticService.sortSearchFilter("37.42242|-122.08585", params);
        assertTrue(map.isEmpty());
    }
}