package org.springframework.base.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.CollectDao;
import org.springframework.base.system.dao.ConfigDao;
import org.springframework.base.system.entity.Collect;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/CollectService.class */
public class CollectService {
    @Autowired
    private CollectDao collectDao;
    @Autowired
    private ConfigDao configDao;

    public Page<Map<String, Object>> collectListSearch(Page<Map<String, Object>> page, String name, String sourceDatabase, String status) throws Exception {
        return this.collectDao.collectListSearch(page, name, sourceDatabase, status);
    }

    public boolean collectSave(Collect collect) throws Exception {
        return this.collectDao.collectSave(collect);
    }

    public boolean collectDelete(String[] ids) throws Exception {
        return this.collectDao.collectDelete(ids);
    }

    public Collect collectSelectById(Collect collect) throws Exception {
        return this.collectDao.collectSelectById(collect);
    }

    public boolean checkAliasExists(Collect collect) throws Exception {
        return this.collectDao.checkAliasExists(collect);
    }

    public String selectDataByAlias(String alias, String appKey, Map<String, String[]> paramMap) throws Exception {
        JSONObject json = new JSONObject();
        Collect collectTemp = this.collectDao.collectSelectByAlias(alias);
        String model = collectTemp.getModel();
        String sourceConfigId = collectTemp.getSourceConfigId();
        try {
            if (collectTemp.getStatus().equals("1")) {
                json.put("msg", "当前接口已停用!");
                json.put("status", "fail");
                return json.toJSONString();
            } else if (!collectTemp.getAppKey().equals(appKey)) {
                json.put("msg", "appKey有误!");
                json.put("status", "fail");
                return json.toJSONString();
            } else {
                this.configDao.getConfigById(sourceConfigId);
                List<Map<String, Object>> list = this.collectDao.executeSQLForData(collectTemp, paramMap);
                this.collectDao.addRequestNumber(collectTemp.getId());
                if (model.equals("1")) {
                    json.put("total", Integer.valueOf(list.size()));
                    if (list.size() > 0) {
                        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
                        json.put("data", jsonArray);
                    } else {
                        json.put("data", new JSONArray());
                    }
                    json.put("msg", "查询成功! ");
                    json.put("status", "success");
                    return json.toJSONString();
                } else if (model.equals("2")) {
                    if (list.size() > 0) {
                        JSONArray jsonArray2 = JSONArray.parseArray(JSON.toJSONString(list));
                        json.put("data", jsonArray2);
                    } else {
                        json.put("data", new JSONArray());
                    }
                    json.put("msg", "查询成功! ");
                    json.put("code", "200");
                    return json.toJSONString();
                } else if (model.equals("3")) {
                    if (list.size() > 0) {
                        JSONArray jsonArray3 = JSONArray.parseArray(JSON.toJSONString(list));
                        return jsonArray3.toJSONString();
                    }
                    JSONArray jsonArray4 = new JSONArray();
                    return jsonArray4.toJSONString();
                } else {
                    if (list.size() > 0) {
                        JSONArray jsonArray5 = JSONArray.parseArray(JSON.toJSONString(list));
                        json.put("data", jsonArray5);
                    } else {
                        json.put("data", new JSONArray());
                    }
                    json.put("msg", "查询成功! ");
                    json.put("ret", "200");
                    return json.toJSONString();
                }
            }
        } catch (Exception e) {
            LogUtil.e(new StringBuilder().append(e).toString());
            if (model.equals("1")) {
                json.put("msg", "查询出错! " + e.getMessage());
                json.put("status", "fail");
                return json.toJSONString();
            } else if (model.equals("2")) {
                json.put("msg", "查询出错! " + e.getMessage());
                json.put("code", "500");
                return json.toJSONString();
            } else if (model.equals("3")) {
                return new JSONArray().toJSONString();
            } else {
                json.put("msg", "查询出错! " + e.getMessage());
                json.put("ret", "500");
                return json.toJSONString();
            }
        }
    }
}
