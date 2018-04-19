package com.cyj.mystock.info.service;

import com.cyj.mystock.info.job.GetStock;
import com.cyj.mystock.info.job.GetZDT;
import com.cyj.mystock.info.queue.QueueSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WebsocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketService.class);

    @Autowired
    private RestTemplate restTemplate; // HTTP 访问操作类
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private GetStock getStock ;
    @Autowired
    private GetZDT getZDT;


    private boolean flag = true;

    public void initGetStock(){
        LOGGER.info("[WebsocketService Execute]:{}", new Date());
        flag = !flag;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String dateStr = simpleDateFormat.format(new Date());
            String[] dateStrs = dateStr.split(":");
            int hour = Integer.parseInt(dateStrs[0]);
            int minute = Integer.parseInt(dateStrs[1]);
            if(hour>=9 && hour<=15) {
                getStock.setFlag(flag);
                getZDT.setFlag(flag);
                LOGGER.info("[WebsocketService Execute flag]:{}", flag);
                flag = getStock.start();
                getZDT.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[WebsocketService Execute Exception]:", e);
        }
    }
}
