package org.springframework.base.system.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.dao.DesignTableDao;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/DesignTableService.class */
public class DesignTableService {
    @Autowired
    private DesignTableDao designTableDao;

    public Page<Map<String, Object>> selectTableIndexsForMySQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.designTableDao.selectTableIndexsForMySQL(page, dto);
    }

    public boolean indexSaveForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.designTableDao.indexSaveForMySQL(request, dto, username);
    }

    public boolean indexDeleteForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.designTableDao.indexDeleteForMySQL(request, dto, username);
    }

    public Page<Map<String, Object>> selectTableTriggersForMySQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.designTableDao.selectTableTriggersForMySQL(page, dto);
    }

    public boolean triggerDeleteForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.designTableDao.triggerDeleteForMySQL(request, dto, username);
    }

    public Page<Map<String, Object>> selectTableForeignKeyForMySQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.designTableDao.selectTableForeignKeyForMySQL(page, dto);
    }

    public boolean deleteForeignKeyForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.designTableDao.deleteForeignKeyForMySQL(request, dto, username);
    }
}
