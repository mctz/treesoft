package org.springframework.base.system.dao;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.entity.MonitorConfig;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/MonitorConfigDao.class */
public class MonitorConfigDao {
    public Map<String, Object> selectMonitorConfigById(String id) throws Exception {
        DBUtil db = new DBUtil();
        String sql = " select id, create_time ,send_name as sendName, send_email as sendEmail,send_password as sendPassword,smtp_server as smtpServer, smtp_server_port as smtpServerPort,smtp_ssl as smtpSsl,start_tls as startTls , receive_email as receiveEmail,copy_email as copyEmail,title,content, type ,style,state , hook_url as hookUrl , at, weixin_at as weixinAt, phase,operator,script,domain,access_id as accessId, producer ,access_key as accessKey ,telphone  from  treesoft_monitor_config where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            return map;
        }
        return null;
    }

    public boolean monitorConfigSave(MonitorConfig monitorConfig) throws Exception {
        String sql;
        DBUtil db = new DBUtil();
        String id = monitorConfig.getId();
        if (!StringUtils.isEmpty(id)) {
            String sql2 = " update treesoft_monitor_config  set ";
            if (!StringUtils.isEmpty(monitorConfig.getTitle())) {
                sql2 = String.valueOf(sql2) + "title='" + monitorConfig.getTitle() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getContent())) {
                sql2 = String.valueOf(sql2) + "content='" + monitorConfig.getContent() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getStyle())) {
                sql2 = String.valueOf(sql2) + "style='" + monitorConfig.getStyle() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getPhase())) {
                sql2 = String.valueOf(sql2) + "phase='" + monitorConfig.getPhase() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getSendName())) {
                sql2 = String.valueOf(sql2) + "send_name='" + monitorConfig.getSendName() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getSendEmail())) {
                sql2 = String.valueOf(sql2) + "send_email='" + monitorConfig.getSendEmail() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getSendPassword())) {
                sql2 = String.valueOf(sql2) + "send_password='" + monitorConfig.getSendPassword() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getSmtpServer())) {
                sql2 = String.valueOf(sql2) + "smtp_server='" + monitorConfig.getSmtpServer() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getSmtpServerPort())) {
                sql2 = String.valueOf(sql2) + "smtp_server_port='" + monitorConfig.getSmtpServerPort() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getSmtpSsl())) {
                sql2 = String.valueOf(sql2) + "smtp_ssl='" + monitorConfig.getSmtpSsl() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getStartTls())) {
                sql2 = String.valueOf(sql2) + "start_tls='" + monitorConfig.getStartTls() + "' ,";
            }
            if (!StringUtils.isEmpty(monitorConfig.getReceiveEmail())) {
                sql2 = String.valueOf(sql2) + "receive_email='" + monitorConfig.getReceiveEmail() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getCopyEmail())) {
                sql2 = String.valueOf(sql2) + "copy_email='" + monitorConfig.getCopyEmail() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getHookUrl())) {
                sql2 = String.valueOf(sql2) + "hook_url='" + monitorConfig.getHookUrl() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getAt())) {
                sql2 = String.valueOf(sql2) + "at='" + monitorConfig.getAt() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getWeixinAt())) {
                sql2 = String.valueOf(sql2) + "weixin_at='" + monitorConfig.getWeixinAt() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getAgentId())) {
                sql2 = String.valueOf(sql2) + "agent_id='" + monitorConfig.getAgentId() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getCropId())) {
                sql2 = String.valueOf(sql2) + "crop_id='" + monitorConfig.getCropId() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getAccessToken())) {
                sql2 = String.valueOf(sql2) + "access_token='" + monitorConfig.getAccessToken() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getState())) {
                sql2 = String.valueOf(sql2) + "state='" + monitorConfig.getState() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getOperator())) {
                sql2 = String.valueOf(sql2) + "operator='" + monitorConfig.getOperator() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getScript())) {
                sql2 = String.valueOf(sql2) + "script='" + monitorConfig.getScript() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getDomain())) {
                sql2 = String.valueOf(sql2) + "domain='" + monitorConfig.getDomain() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getAccessId())) {
                sql2 = String.valueOf(sql2) + "access_id='" + monitorConfig.getAccessId() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getProducer())) {
                sql2 = String.valueOf(sql2) + "producer='" + monitorConfig.getProducer() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getAccessKey())) {
                sql2 = String.valueOf(sql2) + "access_key='" + monitorConfig.getAccessKey() + "', ";
            }
            if (!StringUtils.isEmpty(monitorConfig.getTelphone())) {
                sql2 = String.valueOf(sql2) + "telphone='" + monitorConfig.getTelphone() + "', ";
            }
            sql = String.valueOf(String.valueOf(sql2) + "create_time='" + DateUtils.getDateTime() + "' ") + "  where id='" + id + "'";
        } else {
            String tempId = StringUtil.getUUID();
            sql = " insert into treesoft_monitor_config (id, create_time ,send_name,send_email,send_password,smtp_server,smtp_server_port,smtp_ssl,start_tls ,receive_email,copy_email,title ,at ,weixin_at, content ) values ( '" + tempId + "','" + DateUtils.getDateTime() + "','" + monitorConfig.getSendName() + "','" + monitorConfig.getSendEmail() + "','" + monitorConfig.getSendPassword() + "','" + monitorConfig.getSmtpServer() + "','" + monitorConfig.getSmtpServerPort() + "','" + monitorConfig.getSmtpSsl() + "','" + monitorConfig.getStartTls() + "','" + monitorConfig.getReceiveEmail() + "','" + monitorConfig.getCopyEmail() + "','" + monitorConfig.getTitle() + "','" + monitorConfig.getAt() + "','" + monitorConfig.getWeixinAt() + "','" + monitorConfig.getContent() + "' ) ";
        }
        boolean bl = db.do_update(sql);
        return bl;
    }
}
