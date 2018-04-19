package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.ZdtBean;
import com.cyj.mystock.info.config.Const;
import com.cyj.mystock.info.queue.QueueSender;
import com.cyj.mystock.info.utils.MyStringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Log4j2
public class GetZDTService {

    private final String ZDTURL = "http://q.10jqka.com.cn/api.php?t=indexflash&";
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private SpmmInfoService spmmInfoService;

    public void run(boolean flag){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(ZDTURL);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(15000)
                    .setConnectionRequestTimeout(30000).build();
            request.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(request);
            String content = EntityUtils.toString(response.getEntity());
            JSONObject zdtJson = ((JSONObject) (new JSONParser().parse(content)));
            JSONObject zdfbData = (JSONObject) zdtJson.get("zdfb_data");
            JSONObject zdtData = (JSONObject) zdtJson.get("zdt_data");
            JSONObject lastZdt = (JSONObject)zdtData.get("last_zdt");
            JSONArray zdfbJson = (JSONArray) zdfbData.get("zdfb");
            lastZdt.put("d8to10",zdfbJson.get(0));
            lastZdt.put("d6to8",zdfbJson.get(1));
            lastZdt.put("d4to6",zdfbJson.get(2));
            lastZdt.put("d2to4",zdfbJson.get(3));
            lastZdt.put("d0to2",zdfbJson.get(4));
            lastZdt.put("z0to2",zdfbJson.get(5));
            lastZdt.put("z2to4",zdfbJson.get(6));
            lastZdt.put("z4to6",zdfbJson.get(7));
            lastZdt.put("z6to8",zdfbJson.get(8));
            lastZdt.put("z8to10",zdfbJson.get(9));
            lastZdt.put("znum",zdfbData.get("znum"));
            lastZdt.put("dnum",zdfbData.get("dnum"));
            log.info(lastZdt);
            String queueName = "zdt";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Const.KEY,Const.ZDT);
            jsonObject.put("data",lastZdt);
            queueSender.send(queueName,jsonObject.toJSONString());
            if (flag) {
                ZdtBean bean = MyStringUtils.toBean(lastZdt.toJSONString(),ZdtBean.class);
                bean.setTradeDate(new Date());
                spmmInfoService.insertZdtBean(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[DXJL CronJob Execute Timer Exception]:", e);
        }

    }
}
