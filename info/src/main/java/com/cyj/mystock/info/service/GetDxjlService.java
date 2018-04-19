package com.cyj.mystock.info.service;

import com.cyj.mystock.info.config.Const;
import com.cyj.mystock.info.config.DxjlPool;
import com.cyj.mystock.info.queue.QueueSender;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//获取同花顺的短线精灵
@Component
@Log4j2
public class GetDxjlService {

    private final String DXJLURL = "http://stockpage.10jqka.com.cn/spService/000001/Header/realDxjl";
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private SpmmInfoService spmmInfoService;

    public void run() {

        log.info("获取短线精灵的时间：{}", new Date());
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(DXJLURL);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(15000)
                    .setConnectionRequestTimeout(30000).build();
            request.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(request);
            String content = EntityUtils.toString(response.getEntity());
            String newContent = DxjlPool.duplicateRemove(content);
            if (StringUtils.isNotBlank(newContent)) {
                DxjlPool.setValue(content);
                String xmlContent = parseXml(newContent);
                log.info(xmlContent);
                String queueName = "dxjl";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Const.KEY, Const.DXJL);
                jsonObject.put("data", xmlContent);
                queueSender.send(queueName, jsonObject.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[DXJL CronJob Execute Timer Exception]:", e);
        }
    }

    private String parseXml(String content) throws DocumentException, org.json.simple.parser.ParseException {
        String xmlContent = "<table>" + content + "</table>";
        Document doc = DocumentHelper.parseText(xmlContent);
        Element root = doc.getRootElement();
        List<Element> trList = root.elements();
        for (Element tr : trList) {
            List<Element> tdList = tr.elements();
            for (Element td : tdList) {
                Element a = td.element("a");
                if (a != null) {
                    String href = a.attributeValue("href").replaceAll("/", "");
                    String stockStr = spmmInfoService.getStock(href);
                    JSONObject jsonObject1 = ((JSONObject) (new JSONParser().parse(stockStr)));
                    String tag = (String) jsonObject1.get("tag");
                    Element newTd = tr.addElement("td");
                    newTd.addText(tag);
                }
            }
        }
        String returnValue = doc.asXML();
        returnValue = returnValue.replaceAll("<table>", "").replaceAll("</table>", "").replaceAll("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->", "");
        return returnValue;
    }

}
