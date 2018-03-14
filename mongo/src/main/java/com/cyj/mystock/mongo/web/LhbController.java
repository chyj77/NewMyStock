package com.cyj.mystock.mongo.web;

import com.cyj.mystock.mongo.service.LhbService;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LhbController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LhbController.class);

    @Autowired
    private LhbService lhbService;

    @RequestMapping("/lhb")
    public String findAll(HttpServletRequest request) {
        String day = request.getParameter("day");
        JSONArray jsonArray = lhbService.findAll(day);
        return  jsonArray.toJSONString();
    }
}
