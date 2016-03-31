package panov.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import panov.entity.Poi;
import panov.service.StaticService;

import java.util.List;
import java.util.Map;

/**
 * @author Andrey Panov, 27.03.2016
 */
@RestController
public class StaticController {

    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    @Autowired
    StaticService service;

    @RequestMapping(value = "/api/static/post_poi", method = RequestMethod.POST)
    public void poiPoi(@RequestParam Map<String, Object> map) {
        Poi poi = StaticService.convert(map);
        service.save(poi);
    }

    @RequestMapping(value = "/api/static/find", method = RequestMethod.GET)
    public List<Poi> find(@RequestParam Map<String, String> searchMap) {
        return service.findAll(searchMap);
    }

}
