package com.cyj.mystock.info.job;

import com.cyj.mystock.info.queue.QueueSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component(value="getStockStopJob")
public class GetStockStopJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStockStopJob.class);

    @Autowired
    private RestTemplate restTemplate; // HTTP 访问操作类
    @Autowired
    private QueueSender queueSender;
    private boolean flag = false;

    @Scheduled(cron = "0 00 15 * * MON-FRI")
    public void cronJob() {
        LOGGER.info("[GetStockStopJob Execute]:{}", new Date());
        flag = !flag;
        try {
            GetStock getStock = GetStock.getInstance(restTemplate, queueSender);
            getStock.setFlag(flag);
            LOGGER.info("[GetStockStopJob Execute flag]:{}", flag);
            flag = getStock.start();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[GetStockStopJob Execute Exception]:", e);
        }
    }
}
