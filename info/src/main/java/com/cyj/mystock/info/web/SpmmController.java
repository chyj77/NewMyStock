package com.cyj.mystock.info.web;

import com.cyj.mystock.info.bean.SpmmBean;
import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.service.SpmmInfoService;
import com.cyj.mystock.info.service.ZtsjInfoService;
import org.json.simple.JSONObject;
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
import java.util.List;

@RestController(value = "/spmm")
public class SpmmController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpmmController.class);

    @Autowired
    private Registration registration;       // 服务注册

    @Autowired
    private DiscoveryClient discoveryClient; // 服务发现客户端
    @Autowired
    private SpmmInfoService service;

    @RequestMapping("/luoji")
    public String luoji() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());

        return  service.getLuoji();
    }
    @RequestMapping("/stocks")
    public String stocks() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  service.getStock();
    }
    @RequestMapping("/index")
    public String spmm() {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  service.getAll();
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String spmmSave(@RequestBody SpmmBean bean) {
        LOGGER.info("{}",bean);
        String providerMsg ="保存成功";
        try {
            service.save(bean);
        }catch (Exception e){
            LOGGER.error("",e);
            e.printStackTrace();
            providerMsg ="保存失败";
        }
        return providerMsg;
    }
    //删除
    @RequestMapping("/delete")
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

    @RequestMapping("/fx")
    public String querySpmmFx() throws Exception {
        ServiceInstance instance = serviceInstance();
        LOGGER.info("provider service, host = " + instance.getHost()
                + ", service_id = " + instance.getServiceId());
        return  service.querySpmmFx();
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
