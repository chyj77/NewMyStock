package com.cyj.mystock.info.job;

import com.cyj.mystock.info.config.Const;
import com.cyj.mystock.info.config.DxjlPool;
import com.cyj.mystock.info.queue.QueueSender;
import com.cyj.mystock.info.service.SpmmInfoService;
import lombok.extern.log4j.Log4j2;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//获取同花顺的短线精灵
@Log4j2
public class GetDxjl {

    private final String DXJLURL = "http://stockpage.10jqka.com.cn/spService/000001/Header/realDxjl";

    private final long SECOND = 5 * 1000L;

    private QueueSender queueSender;

    private SpmmInfoService spmmInfoService;

    private Timer timer = new Timer(true);

    private static GetDxjl instance = null;

    private boolean flag;

    public static GetDxjl getInstance(QueueSender queueSender,SpmmInfoService spmmInfoService) {
        if (instance == null) {
            instance = new GetDxjl(queueSender,spmmInfoService);
        }
        return instance;
    }

    private GetDxjl(QueueSender queueSender,SpmmInfoService spmmInfoService) {
        this.queueSender = queueSender;
        this.spmmInfoService = spmmInfoService;
    }

    public boolean start() throws Exception {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                try {
                    HttpGet request = new HttpGet(DXJLURL);
                    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(15000)
                            .setConnectionRequestTimeout(30000).build();
                    request.setConfig(requestConfig);
                    CloseableHttpResponse response = httpclient.execute(request);
                    String content = EntityUtils.toString(response.getEntity());
                    if(DxjlPool.contains(content)){
                        return;
                    }
                    DxjlPool.setValue(content);
                    String xmlContent = parseXml(content);
                    log.info(xmlContent);
                    String queueName = "dxjl";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Const.KEY,Const.DXJL);
                    jsonObject.put("data",xmlContent);
                    queueSender.send(queueName,jsonObject.toJSONString());
                    if (flag) {
                        timer.cancel();
                        timer = null;
                        timer = new Timer(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("[DXJL CronJob Execute Timer Exception]:", e);
                }
            }
        }, 0L, SECOND);
        return flag;
    }

    private String parseXml(String content) throws DocumentException,org.json.simple.parser.ParseException{
        String xmlContent = "<table>" + content +"</table>";
        Document doc = DocumentHelper.parseText(xmlContent);
        Element root = doc.getRootElement();
        List<Element> trList = root.elements();
        for(Element tr:trList){
            List<Element> tdList = tr.elements();
            for(Element td:tdList){
                Element a = td.element("a");
                if(a!=null){
                    String href = a.attributeValue("href").replaceAll("/","");
                    String stockStr =spmmInfoService.getStock(href);
                    JSONObject jsonObject1 = ((JSONObject) (new JSONParser().parse(stockStr)));
                    String tag = (String) jsonObject1.get("tag");
                    Element newTd = tr.addElement("td");
                    newTd.addText(tag);
                }
            }
        }
        String returnValue = doc.asXML();
        returnValue = returnValue.replaceAll("<table>","").replaceAll("</table>","");
        return returnValue;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
