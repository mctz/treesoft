package org.springframework.base.system.service;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.base.system.dao.MonitorConfigDao;
import org.springframework.base.system.entity.MonitorConfig;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/MonitorConfigService.class */
public class MonitorConfigService {
    @Autowired
    private MonitorConfigDao monitorConfigDao;

    public Map<String, Object> selectMonitorConfigById(String id) throws Exception {
        return this.monitorConfigDao.selectMonitorConfigById(id);
    }

    public boolean monitorConfigSave(MonitorConfig monitorConfig) throws Exception {
        return this.monitorConfigDao.monitorConfigSave(monitorConfig);
    }

    public String dataSyncPushRun(Map<String, Object> monitorMap) {
        MonitorConfig monitorConfig = (MonitorConfig) JSON.parseObject(JSON.toJSONString(monitorMap), MonitorConfig.class);
        try {
            String state = monitorConfig.getState();
            if (state.equals("1")) {
                LogUtil.d("预警消息推送已停用!");
                return "预警消息推送已停用";
            }
            String style = monitorConfig.getStyle();
            if (style.equals("0")) {
                MailUtil.mailSend(monitorConfig);
            }
            if (style.equals("1")) {
                shellConfigRun(monitorConfig);
            }
            if (style.equals("2")) {
                smsConfigRun(monitorConfig);
            }
            if (style.equals("3")) {
                dingdingConfigRun(monitorConfig);
                return "预警消息发送成功";
            }
            return "预警消息发送成功";
        } catch (Exception e) {
            LogUtil.e("预警消息推送失败!" + e);
            return "预警消息发送失败";
        }
    }

    public String smsConfigRun(MonitorConfig monitorConfig) throws Exception {
        monitorConfig.getDomain();
        monitorConfig.getAccessId();
        monitorConfig.getAccessKey();
        return "发送短信功能暂未实现";
    }

    public String shellConfigRun(MonitorConfig monitorConfig) throws Exception {
        String script = monitorConfig.getScript().replace("\\", File.separator);
        String[] scriptArray = script.split("\n");
        Runtime rt = Runtime.getRuntime();
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            for (int y = 0; y < scriptArray.length; y++) {
                Thread.sleep(2000L);
                String str = "cmd.exe /C " + scriptArray[y];
                rt.exec(scriptArray);
            }
        } else {
            for (String str2 : scriptArray) {
                Thread.sleep(2000L);
                rt.exec(str2);
            }
        }
        LogUtil.i("运行shell脚本：" + script);
        return "运行结束";
    }

    public String dingdingConfigRun(MonitorConfig monitorConfig) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String webHookUrl = monitorConfig.getHookUrl();
        HttpPost httppost = new HttpPost(webHookUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        String title = monitorConfig.getTitle();
        String content = monitorConfig.getContent();
        HashMap hashMap = new HashMap();
        hashMap.put("title", title);
        hashMap.put("content", String.valueOf(content) + "\n");
        String atStr = monitorConfig.getAt();
        HashMap hashMap2 = new HashMap();
        if (atStr.indexOf("all") >= 0) {
            hashMap2.put("isAtAll", true);
        }
        String atStr2 = atStr.replace("all,", "");
        if (!StringUtils.isEmpty(atStr2)) {
            hashMap2.put("atMobiles", atStr2.split(","));
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("msgtype", "text");
        reqMap.put("text", hashMap);
        reqMap.put("at", hashMap2);
        String textMsg = JSON.toJSONString(reqMap);
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
        String result = null;
        try {
            try {
                CloseableHttpResponse execute = httpclient.execute(httppost);
                if (execute.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(execute.getEntity(), "utf-8");
                    if (result.indexOf("\"errmsg\":\"ok\"}") < 0) {
                        LogUtil.e("钉钉 推送消息失败，返回信息:" + result);
                        throw new Exception("dingding exception");
                    }
                }
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                try {
                    httpclient.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return result;
        } catch (Throwable th) {
            try {
                httpclient.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }
}
