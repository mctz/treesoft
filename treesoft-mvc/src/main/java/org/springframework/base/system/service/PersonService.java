package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.PersonDao;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.entity.TempDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/PersonService.class */
public class PersonService {
    @Autowired
    private PersonDao personDao;

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page, String username, String realname) throws Exception {
        return this.personDao.personList(page, username, realname);
    }

    public boolean personInsert(Person person) throws Exception {
        return this.personDao.personInsert(person);
    }

    public boolean personUpdate(Person person) throws Exception {
        return this.personDao.personUpdate(person);
    }

    public boolean deletePerson(String[] ids) throws Exception {
        return this.personDao.deletePerson(ids);
    }

    public Map<String, Object> getPerson(String id) throws Exception {
        return this.personDao.getPerson(id);
    }

    public boolean resetPersonPass(String[] ids) throws Exception {
        return this.personDao.resetPersonPass(ids);
    }

    public boolean registerUpdate(TempDto tem) {
        try {
            return this.personDao.registerUpdate(tem);
        } catch (Exception e) {
            LogUtil.e("注册失败,," + e);
            return true;
        }
    }

    public List<Map<String, Object>> selectPersonByIds(String[] ids) {
        return this.personDao.selectPersonByIds(ids);
    }

    public boolean userNameIsExists(String userName) throws Exception {
        return this.personDao.userNameIsExists(userName);
    }

    public boolean personUpdateDatascope(Person person) throws Exception {
        return this.personDao.personUpdateDatascope(person);
    }

    public boolean personUpdateLastLoginTime(Person person) throws Exception {
        return this.personDao.personUpdateLastLoginTime(person);
    }

    public int selectPersonNum(Person person) throws Exception {
        return this.personDao.selectPersonNum(person);
    }

    public List<Map<String, Object>> selectPersonByUserName(String username) throws Exception {
        return this.personDao.selectPersonByUserName(username);
    }
}
