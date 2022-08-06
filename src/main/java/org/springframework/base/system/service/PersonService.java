package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.entity.TempDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page, String username, String realname) throws Exception {
        return null;
    }

    public boolean personInsert(Person person) throws Exception {
        return false;
    }

    public boolean personUpdate(Person person) throws Exception {
        return false;
    }

    public boolean deletePerson(String[] ids) throws Exception {
        return false;
    }

    public Map<String, Object> getPerson(String id) throws Exception {
        return null;
    }

    public boolean resetPersonPass(String[] ids) throws Exception {
        return false;
    }

    public boolean registerUpdate(TempDto tem) {
       return false;
    }

    public List<Map<String, Object>> selectPersonByIds(String[] ids) {
        return null;
    }

    public boolean userNameIsExists(String userName) throws Exception {
        return false;
    }

    public boolean personUpdateDatascope(Person person) throws Exception {
        return false;
    }

    public boolean personUpdateLastLoginTime(Person person) throws Exception {
        return false;
    }

    public int selectPersonNum(Person person) throws Exception {
        return 0;
    }

    public List<Map<String, Object>> selectPersonByUserName(String username) throws Exception {
        return null;
    }
}
