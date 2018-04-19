package com.cyj.mystock.info.job;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.config.Const;
import com.cyj.mystock.info.queue.QueueSender;
import com.cyj.mystock.info.service.*;
import com.cyj.mystock.info.utils.MyStringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Log4j2
public class GetStock {
    @Autowired
    private GetStockService getStockService;
    @Autowired
    private DfbService dfbService;
    @Autowired
    private ZfbService zfbService;
    @Autowired
    private GetDxjlService getDxjlService;

    private Timer timer = new Timer(true);

    private TimerTask timerTask = null;

    private boolean flag;

    private final long SECOND = 5 * 1000L;

    public boolean start() throws Exception {
        timer.schedule(timerTaskInstance(), 0L, SECOND);
        return flag;
    }

    private TimerTask timerTaskInstance() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        getStockService.run(flag);
                        getDxjlService.run();
                        zfbService.run();
                        dfbService.run();
                        if (flag) {
                            timerTask.cancel();
                            timerTask = null;
//                            timer = new Timer(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("[CronJob Execute Timer Exception]:", e);
                    }
                }
            };
        }
        return timerTask;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
