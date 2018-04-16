package com.cyj.mystock.info.web;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.service.FollowStockService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class FollowStockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FollowStockController.class);

    @Autowired
    private Registration registration;       // 服务注册

    @Autowired
    private DiscoveryClient discoveryClient; // 服务发现客户端
    @Autowired
    private FollowStockService followStockService;

    @RequestMapping("/ccgp/index")
    public String getAll() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  followStockService.getAll();
    }

    @RequestMapping(value = "/ccgp/save", method = RequestMethod.POST)
    public String doSave(@RequestBody FollowStockBean bean) {
        LOGGER.info("{}",bean);
        String providerMsg ="保存成功";
        try {
            followStockService.save(bean);
        }catch (Exception e){
            LOGGER.error("",e);
            e.printStackTrace();
            providerMsg ="保存失败";
        }
        return providerMsg;
    }
    //删除
    @RequestMapping("/ccgp/delete")
    public String delete(HttpServletRequest request) {
        String recId = request.getParameter("recId");
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        String providerMsg ="删除成功";
        try{
            followStockService.delete(Long.valueOf(recId));
        }catch (Exception e){
            LOGGER.error("",e);
            e.printStackTrace();
            providerMsg ="删除失败";
        }
        return  providerMsg;
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
    /**
     * 获取当前服务的服务实例
     *
     * @return ServiceInstance
     */
    public ServiceInstance serviceInstance() {
        List<ServiceInstance> list = discoveryClient.getInstances(registration.getServiceId());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
