package com.cyj.mystock.info.job;

import com.cyj.mystock.info.queue.QueueSender;
import com.cyj.mystock.info.service.FollowStockService;
import com.cyj.mystock.info.service.SpmmInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component(value="getStockStop11Job")
public class GetStockStop11Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStockStop11Job.class);

    @Autowired
    private RestTemplate restTemplate; // HTTP 访问操作类
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private GetStock getStock ;
    @Autowired
    private GetZDT getZDT;

    private boolean flag = false;

    @Scheduled(cron = "0 30 11 * * MON-FRI")
    public void cronJob() {
        LOGGER.info("[GetStockStopJob Execute]:{}", new Date());
        flag = !flag;
        try {
            getStock.setFlag(flag);
            getZDT.setFlag(flag);
            LOGGER.info("[GetStockStopJob Execute flag]:{}", flag);
            flag = getStock.start();
            getZDT.start();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[GetStockStopJob Execute Exception]:", e);
        }
    }
}
