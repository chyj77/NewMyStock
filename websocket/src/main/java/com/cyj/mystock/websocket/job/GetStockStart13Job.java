package com.cyj.mystock.websocket.job;

import com.cyj.mystock.websocket.queue.QueueSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

@Component(value = "getStockStart13Job")
public class GetStockStart13Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStockStart13Job.class);

    @Autowired
    private RestTemplate restTemplate; // HTTP 访问操作类
    @Autowired
    private QueueSender queueSender;



    private boolean flag = true;

    @Scheduled(cron = "0 00 13 * * MON-FRI")
    public void cronJob() {
        LOGGER.info("[GetStockStartJob Execute]:{}", new Date());
        flag = !flag;
        try {
            GetStock getStock = GetStock.getInstance(restTemplate, queueSender);
            getStock.setFlag(flag);
            LOGGER.info("[GetStockStartJob Execute flag]:{}", flag);
            flag = getStock.start();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[GetStockStartJob Execute Exception]:", e);
        }
    }

    @Deprecated
    private int daysBetween(Date ccrq, Date now) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ccrq = sdf.parse(sdf.format(ccrq));
        now = sdf.parse(sdf.format(now));
        Calendar cal = Calendar.getInstance();
        cal.setTime(ccrq);
        long time1 = cal.getTimeInMillis();
        cal.setTime(now);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /*
    @Scheduled(fixedRate = SECOND * 4)
    public void fixedRateJob() {
        LOGGER.info("[FixedRateJob Execute] {}",new Date());
    }

    @Scheduled(fixedDelay = SECOND * 2)
    public void fixedDelayJob() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        LOGGER.info("[FixedDelayJob Execute] {}",(new Date()));
    }
    */
}
