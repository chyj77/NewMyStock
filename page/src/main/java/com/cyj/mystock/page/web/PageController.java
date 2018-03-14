package com.cyj.mystock.page.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private RestTemplate restTemplate; // HTTP 访问操作类

    @RequestMapping("/menu")
    @ResponseBody
    public String menu() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-MENU/menu",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/thsLhb")
    @ResponseBody
    public String thsLhb(HttpServletRequest request) {
        String day = request.getParameter("day");
        Map map = new HashMap();
        map.put("day",day);
        String url = "http://SERVICE-MONGO/lhb?day={day}";
        String providerMsg = (String) restTemplate.getForEntity(url,String.class,map).getBody();
        return providerMsg;
    }
    @RequestMapping("/")
    public String index(HashMap<String, Object> map) {
        map.put("hello", "欢迎进入HTML页面");
        return "/index";
    }
}
