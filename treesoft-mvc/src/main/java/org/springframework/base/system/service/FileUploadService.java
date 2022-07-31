package org.springframework.base.system.service;

import java.util.List;
import org.springframework.base.system.dao.FileUploadDao;
import org.springframework.base.system.entity.Attach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/FileUploadService.class */
public class FileUploadService {
    @Autowired
    private FileUploadDao fileUploadDao;

    public boolean saveAttach(Attach attach) throws Exception {
        return this.fileUploadDao.saveAttach(attach);
    }

    public boolean updateBid(List<String> attachIds, String bid) throws Exception {
        return this.fileUploadDao.updateBid(attachIds, bid);
    }

    public boolean fileDelete(String fileId) throws Exception {
        return this.fileUploadDao.fileDelete(fileId);
    }

    public Attach selectAttachById(String id) throws Exception {
        return this.fileUploadDao.selectAttachById(id);
    }

    public List<Attach> selectAttachsByBid(String bid) throws Exception {
        return this.fileUploadDao.selectAttachsByBid(bid);
    }
}
