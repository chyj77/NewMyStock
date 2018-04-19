package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.config.Const;
import com.cyj.mystock.info.queue.QueueSender;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Log4j2
public class GetStockService {
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private FollowStockService followStockService;

    private final String URL="http://hq.sinajs.cn/list=";

    public void run(boolean flag){
        log.info("获取关注股票数据的时间：{}",new Date());
        String providerMsg = followStockService.getAll();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser().parse(providerMsg));
            if (jsonObject.get("Rows") != null) {
                JSONArray rowsJsonArray = (JSONArray) jsonObject.get("Rows");
                for (int i = 0; i < rowsJsonArray.size(); i++) {
                    StringBuffer sb = new StringBuffer();
                    JSONObject rowsJson = (JSONObject) rowsJsonArray.get(i);
                    String stockcode = (String) rowsJson.get("stockcode");
                    if (stockcode.startsWith("00") || stockcode.startsWith("30")) {
                        sb.append("sz");
                    } else {
                        sb.append("sh");
                    }
                    sb.append(stockcode);
                    String getUrl = URL + sb.toString();
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    HttpGet request = new HttpGet(getUrl);
                    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(15000)
                            .setConnectionRequestTimeout(30000).build();
                    request.setConfig(requestConfig);
                    CloseableHttpResponse response = httpclient.execute(request);
                    String content = EntityUtils.toString(response.getEntity());

                    if (StringUtils.isNotBlank(content)) {
                        String[] stockhqstrs = content.split(",");
                        String nowprice = stockhqstrs[3].trim();
                        String rq = (String) rowsJson.get("followDate");
//                                    Date ccrq = sdf.parse(rq);
//                                    int ccday = daysBetween(ccrq, now);
                        rowsJson.replace("dateDiff","");
                        rowsJson.replace("nowPrice",nowprice);

                        rowsJson.put("zdl","");
//                      log.info("[CronJob Execute RealTime Data]:{}", rowsJson.toJSONString());
                        if(flag){
                            FollowStockBean bean = new FollowStockBean();
                            bean.setNowPrice((String) rowsJson.get("nowPrice"));
                            bean.setId((Long) rowsJson.get("id"));
                            followStockService.save(bean);
                        }
                        rowsJson.put(Const.KEY,Const.FOLLOWSTOCK);
                        String queueName = "stock";
                        queueSender.send(queueName,rowsJson.toJSONString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[CronJob Execute Timer Exception]:", e);
        }
    }
}
