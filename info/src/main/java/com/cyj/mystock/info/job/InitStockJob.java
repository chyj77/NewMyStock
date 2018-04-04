package com.cyj.mystock.info.job;

import com.cyj.mystock.info.service.FollowStockService;
import com.cyj.mystock.info.service.SpmmInfoService;
import com.cyj.mystock.info.service.ZtsjInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InitStockJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitStockJob.class);

    @Autowired
    private ZtsjInfoService ztsjInfoService;
    @Autowired
    private SpmmInfoService spmmInfoService;
    @Autowired
    private FollowStockService followStockService;

    @Scheduled(cron = "0 00 00 * * MON-FRI")
    public void cronJob() {
        LOGGER.info("InitStockJob 重新初始化redis的数据!");
        ztsjInfoService.setZtgn();
        ztsjInfoService.setAll();
        spmmInfoService.setLuoji();
        spmmInfoService.setStock();
        followStockService.setRedisData();
    }
}
