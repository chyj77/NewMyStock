package com.cyj.mystock.info.web;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.service.FollowStockService;
import com.cyj.mystock.info.service.GfjgdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController(value = "/gfjgd")
public class GfjgdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GfjgdController.class);

    @Autowired
    private Registration registration;       // 服务注册

    @Autowired
    private DiscoveryClient discoveryClient; // 服务发现客户端
    @Autowired
    private GfjgdService gfjgdService;

    @RequestMapping(value = "/index")
    public String getAll() {
        Date date1 = new Date();
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        String result =  gfjgdService.getAll();
        LOGGER.info("查找股票交割单耗时={}毫秒",(System.currentTimeMillis()-date1.getTime()));
        return result;
    }

    @RequestMapping(value = "/fx", method = RequestMethod.GET)
    public String queryGfStock() throws Exception{
        return gfjgdService.queryGfStock();
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
