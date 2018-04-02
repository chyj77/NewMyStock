package com.cyj.mystock.info.web;

import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.service.ZtsjInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
public class ZtsjController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZtsjController.class);

    @Autowired
    private Registration registration;       // 服务注册

    @Autowired
    private DiscoveryClient discoveryClient; // 服务发现客户端
    @Autowired
    private ZtsjInfoService service;

    @RequestMapping("/ztgn")
    public String ztgn() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  service.getZtgn();
    }
    @RequestMapping("/ztsj")
    public String ztsj() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  service.getAll();
    }
    @RequestMapping("/ztsj/fx")
    public String ztsjFx() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  service.getZtsjFx();
    }
    @RequestMapping(value = "/ztsj/save", method = RequestMethod.POST)
    public String ztsjSave(@RequestBody ZtsjBean ztsj) {
        LOGGER.info("{}",ztsj);
        String providerMsg ="保存成功";
        try {
            service.save(ztsj);
        }catch (Exception e){
            LOGGER.error("",e);
            e.printStackTrace();
            providerMsg ="保存失败";
        }
        return providerMsg;
    }
    //删除
    @RequestMapping("/ztsj/delete")
    public String delete(HttpServletRequest request) {
        String recId = request.getParameter("recId");
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        String providerMsg ="删除成功";
        try{
            service.delete(Long.valueOf(recId));
        }catch (Exception e){
            LOGGER.error("",e);
            e.printStackTrace();
            providerMsg ="删除失败";
        }
        return  providerMsg;
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
