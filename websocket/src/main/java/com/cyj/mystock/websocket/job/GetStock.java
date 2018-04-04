package com.cyj.mystock.websocket.job;

import com.cyj.mystock.websocket.queue.QueueSender;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GetStock {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStock.class);

    private RestTemplate restTemplate;

    private QueueSender queueSender;

    private Timer timer = new Timer(true);

    private boolean flag;

    private final long SECOND = 5 * 1000L;

    private final String URL = "http://hq.sinajs.cn/list=";

    private static GetStock instance = null;

    public static GetStock getInstance(RestTemplate restTemplate, QueueSender queueSender){
        if(instance==null){
            instance = new GetStock(restTemplate, queueSender);
        }
        return instance;
    }

    private GetStock(RestTemplate restTemplate, QueueSender queueSender) {
        this.queueSender = queueSender;
        this.restTemplate = restTemplate;
    }

    public boolean start() throws Exception {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String providerMsg = restTemplate.getForEntity("http://service-info/followStock",
                        String.class).getBody();
                CloseableHttpClient httpclient = HttpClients.createDefault();
                try {
//                        LOGGER.info("[CronJob Execute RealTime Data]:{}", providerMsg);
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
//                                org.apache.http.Header[] headers = response.getHeaders("Content-Type");
                            String content = EntityUtils.toString(response.getEntity());

                            if (StringUtils.isNotBlank(content)) {
                                String[] stockhqstrs = content.split(",");
                                String nowprice = stockhqstrs[3].trim();
                                String rq = (String) rowsJson.get("followDate");
//                                    Date ccrq = sdf.parse(rq);
//                                    int ccday = daysBetween(ccrq, now);
                                rowsJson.replace("dateDiff","");
                                rowsJson.replace("nowPrice",nowprice);
                                    /*
                                    BigDecimal d_nowprice = new BigDecimal(0);
                                    BigDecimal d_buyprice = new BigDecimal(0);
                                    BigDecimal d_zdl = new BigDecimal(0);
                                    if (StringUtils.isNotBlank(nowprice)) {
                                        d_nowprice = new BigDecimal(nowprice);
                                    }
                                    String buyprice = (String) rowsJson.get("followPrice");
                                    if (StringUtils.isNotBlank(buyprice)) {
                                        d_buyprice = new BigDecimal(buyprice);
                                    }
                                    d_zdl = (d_nowprice.subtract(d_buyprice)).divide(d_buyprice, 3, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(3);
                                    */
                                rowsJson.put("zdl","");
                                LOGGER.info("[CronJob Execute RealTime Data]:{}", rowsJson.toJSONString());
                                if(flag){
                                    HttpHeaders headers = new HttpHeaders();
                                    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                                    headers.setContentType(type);
                                    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
                                    HttpEntity<String> formEntity = new HttpEntity<String>(rowsJson.toJSONString(), headers);
                                    restTemplate.postForObject("http://SERVICE-INFO/followStock/save", formEntity, String.class);
                                }
                                queueSender.send(rowsJson.toJSONString());
                            }
                        }
                    }
                    if(flag){
                        timer.cancel();
                        timer =null;
                        timer = new Timer(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("[CronJob Execute Timer Exception]:", e);
                }
            }
        },0L, SECOND);
        return flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
