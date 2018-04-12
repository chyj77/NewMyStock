package com.cyj.mystock.page.web;

import com.cyj.mystock.page.bean.FollowStockBean;
import com.cyj.mystock.page.bean.SpmmBean;
import com.cyj.mystock.page.bean.ZtsjBean;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
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
    @RequestMapping("/ztsj")
    @ResponseBody
    public String ztsj() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/ztsj",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/ztgn")
    @ResponseBody
    public String ztgn() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/ztgn",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/thsLhb")
    @ResponseBody
    public String thsLhb(HttpServletRequest request) {
        String day = request.getParameter("day");
        Map map = new HashMap();
        map.put("day",day);
        String url = "http://SERVICE-MENU/lhb?day={day}";
        String providerMsg = (String) restTemplate.getForEntity(url,String.class,map).getBody();
        return providerMsg;
    }
    @RequestMapping(value = "/ztsj/save", method = RequestMethod.POST)
    @ResponseBody
    public String ztsjSave(@RequestBody ZtsjBean ztsj) {
        LOGGER.info("{}",ztsj);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObj = beanToJSON(ztsj);

        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
        String providerMsg = restTemplate.postForObject("http://SERVICE-INFO/ztsj/save", formEntity, String.class);
//        String providerMsg =restTemplate.postForEntity("http://SERVICE-INFO/ztsj/save",beanToJSON(ztsj).toJSONString(),String.class).getBody();
        return providerMsg;
    }
    @RequestMapping(value = "/spmm/save", method = RequestMethod.POST)
    @ResponseBody
    public String spmmSave(@RequestBody SpmmBean bean) {
        LOGGER.info("{}",bean);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObj = beanToJSON(bean);

        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
        String providerMsg = restTemplate.postForObject("http://SERVICE-INFO/spmm/save", formEntity, String.class);
//        String providerMsg =restTemplate.postForEntity("http://SERVICE-INFO/ztsj/save",beanToJSON(ztsj).toJSONString(),String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/ztsj/delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String recId = request.getParameter("recId");
        Map map = new HashMap();
        map.put("recId",recId);
        String providerMsg = (String) restTemplate.getForEntity("http://SERVICE-INFO/ztsj/delete?recId={recId}",
                String.class,map).getBody();
        return providerMsg;
    }
    @RequestMapping("/spmm/delete")
    @ResponseBody
    public String spmmDelete(HttpServletRequest request) {
        String recId = request.getParameter("recId");
        Map map = new HashMap();
        map.put("recId",recId);
        String providerMsg = (String) restTemplate.getForEntity("http://SERVICE-INFO/spmm/delete?recId={recId}",
                String.class,map).getBody();
        return providerMsg;
    }
    @RequestMapping("/spmm/luoji")
    @ResponseBody
    public String luoji() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/spmm/luoji",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/spmm/stocks")
    @ResponseBody
    public String stocks() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/spmm/stocks",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/spmm")
    @ResponseBody
    public String spmm() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/spmm",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/ccgp")
    @ResponseBody
    public String ccgp() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/followStock",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/ccgp/delete")
    @ResponseBody
    public String ccgpDelete(HttpServletRequest request) {
        String recId = request.getParameter("recId");
        Map map = new HashMap();
        map.put("recId",recId);
        String providerMsg = (String) restTemplate.getForEntity("http://SERVICE-INFO/followStock/delete?recId={recId}",
                String.class,map).getBody();
        return providerMsg;
    }
    @RequestMapping(value = "/ccgp/save", method = RequestMethod.POST)
    @ResponseBody
    public String ccgpSave(@RequestBody FollowStockBean bean) {
        LOGGER.info("{}",bean);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObj = beanToJSON(bean);

        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
        String providerMsg = restTemplate.postForObject("http://SERVICE-INFO/followStock/save", formEntity, String.class);
//        String providerMsg =restTemplate.postForEntity("http://SERVICE-INFO/ztsj/save",beanToJSON(ztsj).toJSONString(),String.class).getBody();
        return providerMsg;
    }

    @RequestMapping("/ccgp/getStock")
    @ResponseBody
    public String ccgpGetStock(HttpServletRequest request) {
        String stockcode = request.getParameter("stockcode");
        String URL = "http://hq.sinajs.cn/list=";
        StringBuffer sb = new StringBuffer();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        if (stockcode.startsWith("00") || stockcode.startsWith("30")) {
            sb.append("sz");
        } else {
            sb.append("sh");
        }
        sb.append(stockcode);
        String getUrl = URL + sb.toString();
        HttpGet httpGet = new HttpGet(getUrl);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(15000)
                .setConnectionRequestTimeout(30000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        String providerMsg="";
        try {
            response = httpclient.execute(httpGet);
        org.apache.http.Header[] headers = response.getHeaders("Content-Type");
        String content = EntityUtils.toString(response.getEntity());

        if (StringUtils.isNotBlank(content)) {
            String[] stockhqstrs = content.split(",");
            providerMsg = stockhqstrs[3].trim();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return providerMsg;
    }
    @RequestMapping("/gfjgd")
    @ResponseBody
    public String gfjgd() {
        Date date1 = new Date();
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/gfjgd",
                String.class).getBody();
        LOGGER.info("查询股票交割单耗时={}毫秒",System.currentTimeMillis()-date1.getTime());
        return providerMsg;
    }
    @RequestMapping("/gfjgd/fx")
    @ResponseBody
    public String gfjgdFx() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/gfjgd/fx",
                String.class).getBody();
        return providerMsg;
    }
    @RequestMapping("/spmm/fx")
    @ResponseBody
    public String spmmFx() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/spmm/fx",
                String.class).getBody();
        return providerMsg;
    }

    @RequestMapping("/ztsj/fx")
    @ResponseBody
    public String ztsjFx() {
        String providerMsg = restTemplate.getForEntity("http://SERVICE-INFO/ztsj/fx",
                String.class).getBody();
        return providerMsg;
    }

    @RequestMapping("/")
    public String index(HashMap<String, Object> map) {
        map.put("hello", "欢迎进入HTML页面");
        return "/index";
    }

    private JSONObject beanToJSON(Object obj) {
        JSONObject jsonObject = new JSONObject();
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Object o = (field.get(obj) == null|| field.get(obj).equals("")) ? null : field.get(obj);
                jsonObject.put(field.getName(), o);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            e.printStackTrace();
        }
        return jsonObject;
    }
}
